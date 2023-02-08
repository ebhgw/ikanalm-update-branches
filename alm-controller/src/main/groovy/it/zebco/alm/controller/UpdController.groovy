package it.zebco.alm.controller

import groovy.util.logging.Slf4j
import it.zebco.alm.model.BuildInfo
import it.zebco.alm.model.SvnItem
import it.zebco.alm.model.dao.ProjectStreamInfoDAO
import it.zebco.alm.model.dao.mssql.MssqlProjectStreamInfoDAO
import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.source.FileCollector
import it.zebco.alm.svn.adapter.SvnAccessInfo
import it.zebco.alm.svn.adapter.SvnAdapter
import it.zebco.alm.svn.adapter.SvnAdapterFactory
import org.codehaus.groovy.GroovyException

/*
 * Pick the files from the current package, overwrite the existing files
 * on other ProjectStream evolutive with active status of the same project
 *
 */
@Slf4j
class UpdController {

    String almProject
    String buildPrefix
    String buildSuffix
    String commitMsg
    SvnAccessInfo acc
    String updDir
    // map of project properties from gradle script
    Map<String, String> props
    BuildInfo buildInfo

    List<File> lf
    List<SvnItem> lsi
    List<String> dirs

    FileCollector fc
    ProjectStreamInfo owner
    //List<ProjectStreamInfo> ldest
    ProjectStreamInfoDAO dao
    SvnAdapter sa
    //Iterable<File> jdbcjar

    // save pProps (from gradle properties), path directory with files
    void init(Map pProps, String path, Iterable<File> jjs) {

        log.debug("UpdController initting")
        // prop from gradle project
        props = pProps
        almProject = pProps.get('alm.project.name')
        buildPrefix = pProps.get('alm.projectStream.buildPrefix')
        buildSuffix = pProps.get('alm.projectStream.buildSuffix')
        assert almProject != null || almProject.length() > 0: "alm.project.name undefined or empty"
        log.debug "using project name: $almProject, build prefix: $buildPrefix, build suffix: $buildSuffix"

        String dburl = "jdbc:jtds:sqlserver://${props['rdbms.server']}:${props['rdbms.port']}/${props['rdbms.dbname']}".toString()
        String dbuser = props.get('rdbms.user').toString()
        String dbpass = props.get('rdbms.password').toString()
        String dbdriver = props.get('rdbms.driver').toString()

        owner = getProjectStreamInfo(dburl, dbuser, dbpass, dbdriver, jjs)
        buildInfo = new BuildInfo(pProps)
        commitMsg = pProps.get('svn.commit.text')

        // è pProps.'source' + '.working'
        updDir = path

        // String user, String password, String commandPath, String source, String repoName, String repoUri
        sa = SvnAdapterFactory.getSvnAdaper(
                pProps.get('svn.user').toString(), pProps.get('svn.password').toString(), pProps.get('svn.command.path').toString(),
                pProps.get('source').toString(),
                owner.repoName, owner.repoUri)
        fc = new FileCollector(new File(updDir))

        // get data do build owner ProjectStreamInfo
        log.debug("UpdController init completed")

    }

    ProjectStreamInfo getProjectStreamInfo(String dburl, String dbuser, String dbpass, String dbdriver, Iterable<File> jjs) {

        dao = new MssqlProjectStreamInfoDAO(dburl, dbuser, dbpass, dbdriver)
        dao.jdbcJarLoader(jjs)
        Map row = dao.queryByPrefixSuffix(this.almProject, this.buildPrefix, this.buildSuffix)
        if (!row) {
            // null
            log.debug "No stream found for project $almProject prefix $buildPrefix suffix $buildSuffix"
            throw new GroovyException("No stream found for project $almProject prefix $buildPrefix suffix $buildSuffix")
        }
        //println row
        this.props.put('svn.repository.uri', row.svn_REPOSITORYURL)
        this.props.put('svn.repository.trunk', row.svn_TRUNKDIRECTORY)
        this.props.put('svn.repository.tag', row.svn_TAGSDIRECTORY)
        new ProjectStreamInfo(row)
    }

    List<File> getFiles() {
        fc.getFiles()
    }


    ProjectStreamInfo getOwnerStream() {
        assert almProject && buildPrefix && buildSuffix: "getOwnerStream undefined parameters: " +
                "almProject ${almProject}, buildPrefix ${buildPrefix}, buildSuffix${buildSuffix}"
        ProjectStreamInfo psi = dao.findBranchByPrefixSuffix(almProject, buildPrefix, buildSuffix)
        assert psi: "getOwnerStream: project Stream not found for project ${almProject}:${buildPrefix},${buildSuffix}"
        psi
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
        def res = sa.getOrigin(si.canonicalTagUri)
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
        String releaseStream = props.get('alm.projectStream.buildPrefix')
        assert releaseProject && releaseStream: "UpdController.executeUpdateRelease: undefined parameters, " +
                "releaseProject $releaseProject, releaseStream $releaseStream"

        lf = getFiles()
        println "Collected files"
        lf.each { println ">> " + it.path }
        println "========="
        lsi = getSvnItem()
        log.debug "Processed SvnItem"
        dirs = getDirs()
        log.debug "Processed dirs"
        ProjectStreamInfo dest = getReleaseStream(releaseProject, releaseStream)
        mkdirsInDest(dest)
        println "Updating to " + dest.canonicalBranchUri
        sa.update2Release(dest, lsi, commitMsg)

        cleanup()
    }

    // ProjectStreamInfo defined as Sortable (with equality on project, prefix, suffix)
    List<ProjectStreamInfo> getOrigin() {
        lsi.collect {
            it.origin
        }.unique()
    }

    List<ProjectStreamInfo> getOtherDest(List<ProjectStreamInfo> lorig) {
        assert lorig.size() > 0: "UpdController.getOtherProjStream parameter list empty"
        List<ProjectStreamInfo> lod = lorig.inject([]) {
            List<ProjectStreamInfo> result, orig ->
                // get baseline
                ProjectStreamInfo baseline = dao.findBranchByPrefix(orig.almProjectName, 'Baseline')
                result.add(baseline)
                ProjectStreamInfo correttiva = dao.findBranchByPrefix(orig.almProjectName, 'correttiva')
                result.add(correttiva)
                List<ProjectStreamInfo> levolutiva = dao.findBranchesByR02(orig.almProjectName)
                result.addAll(levolutiva - orig)
                result
        }
        lod
    }

    // find other stream
    List<ProjectStreamInfo> getOtherEvoStream() {
        List<ProjectStreamInfo> levolutiva = dao.findBranchesByR02(almProject)
        levolutiva.findResults {
            ProjectStreamInfo evo ->
                // skip current stream. buildSuffix fixed to R02
                if (evo.buildPrefix != buildPrefix) {
                    evo
                }
        }
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
        List<ProjectStreamInfo> ldest = getOtherDest(lorig)
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
        List<ProjectStreamInfo> ldest = getOtherEvoStream()
        ldest.each {
            ProjectStreamInfo dest ->
                mkdirsInDest(dest)
                // filter items for that project
                sa.update(dest, lsi, commitMsg)
        }
        cleanup()
    }

    void executeUpdateBranch() {
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
        List<ProjectStreamInfo> ldest = getOtherEvoStream()
        ldest.each {
            ProjectStreamInfo dest ->
                mkdirsInDest(dest)
                // filter items for that project
                sa.update(dest, lsi, commitMsg)
        }
        cleanup()
    }

    void cleanup() {
    }
}
