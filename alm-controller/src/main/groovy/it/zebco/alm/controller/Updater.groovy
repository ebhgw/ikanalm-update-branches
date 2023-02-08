package it.zebco.alm.controller

import groovy.util.logging.Slf4j
import it.zebco.alm.model.BuildInfo
import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.RepoInfo
import it.zebco.alm.model.SvnItem
import it.zebco.alm.model.dao.ProjectStreamInfoDAO
import it.zebco.alm.model.RepoInfoFactory
import it.zebco.alm.source.FileCollector
import it.zebco.alm.svn.adapter.SvnAccessInfo
import it.zebco.alm.svn.adapter.SvnAdapter
import it.zebco.alm.svn.adapter.SvnAdapterFactory
import it.zebco.alm.model.dao.factory.DAOFactory

//assunzione, lavora su un solo repository SVN
@Slf4j
class Updater {


    String almProject
    String buildPrefix
    String buildSuffix
    String commitMsg
    SvnAccessInfo acc
    String updatesDir

    // map of project properties from gradle script
    Map<String, String> props
    BuildInfo buildInfo
    RepoInfo repoInfo
    ProjectStreamInfo owner

    List<File> lf
    List<SvnItem> lsi
    List<String> dirs

    FileCollector fc
    SvnAdapter sa

    //List<ProjectStreamInfo> ldest
    ProjectStreamInfoDAO dao

    //Iterable<File> jdbcjar

    // save pProps (from gradle properties),  path directory with files
    void init(Map pProps, String path, Iterable<File> jjs) {
        // prop from gradle project
        props = pProps
        almProject = pProps.get('alm.project.name')
        buildPrefix = pProps.get('alm.projectStream.buildPrefix')
        buildSuffix = pProps.get('alm.projectStream.buildSuffix')
        assert almProject != null || almProject.length() > 0: "alm.project.name undefined or empty"
        //println "project name: $almProject, build prefix: $buildPrefix, build suffix: $buildSuffix"

        this.dao = DAOFactory.getIknAlmDAO(pProps, jjs)
        this.owner = dao.findBranchByPrefixSuffix(this.almProject, this.buildPrefix, this.buildSuffix)
        props.put('repoName', this.owner.repoName)
        props.put('repoUri', this.owner.repoUri)
        this.buildInfo = new BuildInfo(pProps)
        // repoInfo is unique for the Updater
        this.repoInfo = RepoInfoFactory.getRepoInfo(pProps.'vcrUser', pProps.'vcrPassword', this.owner.repoUri, this.owner.repoName)
        commitMsg = pProps.get('svn.commit.text')

        // dir from which updates are collected, pProps.'source' + '.working'
        updatesDir = path

        sa = SvnAdapterFactory.getSvnAdaper(pProps)
        fc = new FileCollector(new File(updatesDir))
    }

    List<File> getFiles() {
        fc.getFiles()
    }

    List<ProjectStreamInfo> getSvnItem() {
        //println "getSvnItem: processing ${lf.size()} files"
        List<SvnItem> collected = []
        lf.each {
            collected << new SvnItem(buildInfo: this.buildInfo, assocFile: it)
        }
        collected
    }

    List<ProjectStreamInfo> getSvnItemWithOriginStream() {
        lsi = getSvnItem()
        lsi.each {
            SvnItem si ->
                ProjectStreamInfo opsi = getOriginStreamFromPropertiesInTag(si)
                println "ProjectStreamInfo:" + opsi
                si.origin = opsi
        }
        lsi
    }

    // compile a list of directory (relative to branch, ie cics, copy, dclgen, qmf/qmfquery)
    // that appears in the paths of file to be updated
    // this list will be used to check for existence and feed mkdirs command
    List<String> getDirs() {
        //println "getDirs: processing ${lsi.size()} SvnItem"
        def dirs = lsi.collect {
            SvnItem si ->
                si.dirAsUri
        }.toSet().toList()
    }

    // use svn adapter to get project, prefix, suffix of the steam
    // set when package project updated release project
    // then use dao to generate a ProjectStreamInfo object to be able
    // to compute uri that will be used when updating
    ProjectStreamInfo getOriginStreamFromPropertiesInTag(SvnItem si) {
        def res = getOrigin(si.canonicalTagUri)
        println "For " + si.canonicalTagUri
        println "got " + res.almProjectName + ':' + res.almProjectStreamBuildPrefix + ':' + res.almProjectStreamBuildSuffix
        dao.findBranchByPrefixSuffix(res.almProjectName, res.almProjectStreamBuildPrefix, res.almProjectStreamBuildSuffix)
    }

    void mkdirsInDest(ProjectStreamInfo dest) {
        dirs.each {
            sa.mkDirs(dest, it)
        }

    }

    ProjectStreamInfo getReleaseStream(String releaseProject, String releaseStream) {
        ProjectStreamInfo row = dao.findBranchByPrefixSuffix(releaseProject, 'Release', releaseStream)
        if (!row) {
            println "Stream not found for ${releaseProject} / ${releaseStream}"
        }
        row
    }

    // in Release buildPrefix is Release while buildSuffix is SIGE1803
    // to find the stream we need only the suffix. In a project pkg base, SIGE1803 is the prefix !!
    void executeUpdateRelease() {
        String releaseProject = props.get('release.project.name')
        String releaseStream = props.get('alm.projectStream.buildSuffix')

        lf = getFiles()
        println "Collected files"
        lf.each { println ">> " + it.path }
        println "========="
        lsi = getSvnItem()
        log.debug "Processed SvnItem"
        dirs = getDirs()
        log.debug "Processed dirs"
        ProjectStreamInfo dest = dao.getReleaseProjStream(releaseProject, releaseStream)
        mkdirsInDest(dest)
        println "Updating to " + dest.canonicalBranchUri
        sa.update2Release(dest.canonicalBranchUri, lsi, commitMsg)

        cleanup()
    }

    // Return a list of all origin (used when updating back from realease to (project) branches
    // ProjectStreamInfo defined as Sortable (with equality on project, prefix, suffix)
    List<ProjectStreamInfo> getOrigin() {
        lsi.collect {
            it.origin
        }.unique()
    }

    // update from release to branch
    void executeUpdate4Release2Branch() {
        lf = getFiles()
        println "Collected files"
        lf.each { println ">> " + it.path }
        println "========="
        lsi = getSvnItemWithOriginStream()
        log.debug "Processed SvnItem"
        dirs = getDirs()
        log.debug "Processed dirs"
        // ottiene la lista delle origini
        List<ProjectStreamInfo> lorig = getOrigin()
        // for every origin (ie project) get stream that should be updated
        // ie Baseline, correttiva, evolutiva streams but origin stream
        List<ProjectStreamInfo> ldest = dao.getOtherProjStream(lorig)
        /*
            TBD: ldest è una lista di stream destinazione
            Per ogni dest invece dobbiamo estrarre il progetto dello stream dest
            - trovare lo stream Baseline
            - trovare lo stream correttiva
            - trovare le evolutive (R02) e stato 5 tranne quelle in dest
            Per ognuna di queste aggiornare gli stream
        List<ProjectStreamInfo> ldest2update = getOtherProjStream(ldest)
         */
        ldest.each {
            ProjectStreamInfo dest ->
                mkdirsInDest(dest)
                // filter items for that project
                List<SvnItem> updable = lsi.findResults {
                    SvnItem si ->
                        if (si.origin.almProjectName == dest.almProjectName)
                            si
                }
                sa.update(dest.canonicalBranchUri, updable, commitMsg)
        }
        cleanup()
    }

    // updates other stream that is
    // - Baseline
    // - correttiva
    // - other active evo
    void executeUpdateOtherEvoBranches() {
        // find other evolution streams (R02)
        lf = getFiles()
        println "Collected files"
        lf.each { println ">> " + it.path }
        println "========="
        lsi = getSvnItem()
        log.debug "Processed SvnItem"
        dirs = getDirs()
        log.debug "Processed dirs"
        // for every origin (ie project) get stream that should b
        // e updated
        // ie Baseline, correttiva, evolutiva streams but origin stream
        List<ProjectStreamInfo> ldest = dao.getEvoStream(owner) - owner
        ldest.each {
            ProjectStreamInfo dest ->
                mkdirsInDest(dest)
                // filter items for that project
                sa.update(dest.getCanonicalBranchUri(), lsi, commitMsg)
        }
        cleanup()
    }

    // updates repository on branch defined by the buildPrefix
    // Pkg based project, Same project, other branch
    void executeUpdateBranch(String buildPrefix) {
        assert !this.owner.buildPrefix.equalsIgnoreCase('Release'): "Only on Pkg based project"
        lf = getFiles()
        println "Collected files"
        lf.each { println ">> " + it.path }
        println "========="
        lsi = getSvnItem()
        log.debug "Processed SvnItem"
        dirs = getDirs()
        log.debug "Processed dirs"
        // for every origin (ie project) get stream that should be updated
        // ie Baseline, correttiva, evolutiva streams but origin stream
        ProjectStreamInfo dest = dao.findBranchByPrefix(this.owner.almProjectName, buildPrefix)
        mkdirsInDest(dest)
        sa.update(dest.getCanonicalBranchUri(), lsi, commitMsg)
        cleanup()
    }

    void cleanup() {
    }
}
