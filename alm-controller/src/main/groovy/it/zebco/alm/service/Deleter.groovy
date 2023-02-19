package it.zebco.alm.controller

import groovy.util.logging.Slf4j
import it.zebco.alm.model.*
import it.zebco.alm.model.dao.ProjectStreamInfoDAO
import it.zebco.alm.model.dao.factory.DAOFactory
import it.zebco.alm.source.FileCollector
import it.zebco.alm.svn.adapter.SvnAccessInfo
import it.zebco.alm.svn.adapter.SvnAdapter
import it.zebco.alm.svn.adapter.SvnAdapterFactory

//assunzione, lavora su un solo repository SVN
@Slf4j
class Deleter {


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
        updatesDir = path + '/jmodel'

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

    // updates repository on branch defined by the buildPrefix
    // Pkg based project, Same project, other branch
    void executeDeleteJmodel4Branch(String buildPrefix) {
        assert !this.owner.buildPrefix.equalsIgnoreCase('Release'): "Only on Pkg based project"
        lf = getFiles()
        println "Collected files"
        lf.each { println ">> " + it.path }
        println "========="
        lsi = getSvnItem()
        log.debug "Processed SvnItem"
        // for every origin (ie project) get stream that should be updated
        // ie Baseline, correttiva, evolutiva streams but origin stream
        ProjectStreamInfo dest = dao.findBranchByPrefix(this.owner.almProjectName, buildPrefix)
        sa.delete(dest.getCanonicalBranchUri(), lsi, commitMsg)
        cleanup()
    }

    void cleanup() {
    }
}
