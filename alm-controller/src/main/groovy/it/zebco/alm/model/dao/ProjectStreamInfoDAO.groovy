package it.zebco.alm.model.dao

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import it.zebco.alm.model.dao.factory.QueryFactory
import it.zebco.alm.model.ProjectStreamInfo

@Slf4j
trait ProjectStreamInfoDAO {

    String dbUrl
    String dbUser
    String dbPassword
    String dbDriver
    // implicit connection on first access
    Sql sql = null

    /**
     * Getting info about branch identified by prefix (that is Baseline, correttiva, Reference)
     *
     * @param almProject (JA01SIGE, GG01OTX etc)
     * @param buildPrefix (Reference, correttiva, Baseline, SIGE1809)
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    ProjectStreamInfo findBranchByPrefix(String almProject, String buildPrefix) {
        assert almProject && buildPrefix: "ProjectStreamInfoDAO findBranchByPrefix failing for empy parameters: " +
                "almProject ${almProject}, buildPrefix ${buildPrefix}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        def res = sql.firstRow(QueryFactory.branchByPrefix, [almProject, buildPrefix])
        res ? new ProjectStreamInfo(res) : null
    }

/**
 * Getting info about branch identified by prefix and suffix (that is evolutiva)
 *
 * @param almProject (JA01SIGE, GG01OTX etc)
 * @param buildPrefix (Reference, correttiva, Baseline, SIGE1809)
 * @param buildSuffix (R01, R02)
 * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
 *
 * Sample result row
 * [project_NAME:SS01OTX, project_VCRPROJECTNAME:SS01OTX, projectStream_STATUS:5, projectStream_ISHEAD:false, projectStream_VCRBRANCHID:SS01OTX/branches/correttiva, projectStream_BUILDPREFIX:correttiva, projectStream_BUILDSUFFIX:R01, lifecycle_NAME:Correttiva R1, vcr_NAME:IKALM_REHOST_SIGEA, vcr_COMMANDPATH:C:/Program Files/SlikSvn/bin, vcr_USERID:EY00018, vcr_PASSWORD:869462b05d7cd61d0f291876bfce2928, vcr_TIMEOUT:700, svn_REPOSITORYURL:http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA, svn_REPOSITORYLAYOUT:0, svn_TRUNKDIRECTORY:Reference, svn_TAGSDIRECTORY:TagVersioni]
 */
    ProjectStreamInfo findBranchByPrefixSuffix(String almProject, String buildPrefix, String buildSuffix) {
        assert almProject && buildPrefix && buildSuffix: "ProjectStreamInfoDAO findBranchByPrefixSuffix failing for empy parameters: " +
                "almProject ${almProject}, buildPrefix ${buildPrefix}, buildSuffix ${buildSuffix}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        log.debug "Query\n" + QueryFactory.branchByPrefixSuffix
        // se buildPrefix Reference, buildSuffix ''
        log.debug "with parametesr almProject:${almProject}, buildPrefix:${buildPrefix}, buildSuffix:${buildSuffix}"
        def res = sql.firstRow(QueryFactory.branchByPrefixSuffix, [almProject, buildPrefix, buildSuffix])
        log.debug "findBranchByPrefixSuffix found " + res
        res ? new ProjectStreamInfo(res) : null
    }
    /*
    As an example, println res returns
Project name: SS01OTX
Branch id    : SS01OTX/branches/correttiva
Build prefix : correttiva
Build suffix : R01
Is Head      : false
Repo uri     : http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA
Branch uri   : SS01OTX/branches/correttiva
Canon Branch uri: http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA/SS01OTX/branches/correttiva
Vcr project name: SS01OTX
     */

    /*
    Returns a map or null (no record found)
     */
    Map queryByPrefixSuffix(String almProject, String buildPrefix, String buildSuffix) {
        assert almProject && buildPrefix && buildSuffix: "ProjectStreamInfoDAO queryByPrefixSuffix failing for empy parameters: " +
                "almProject ${almProject}, buildPrefix ${buildPrefix}, buildSuffix ${buildSuffix}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        log.info "Query\n" + QueryFactory.branchByPrefixSuffix
        log.debug "with parametesr almProject:${almProject}, buildPrefix:${buildPrefix}, buildSuffix:${buildSuffix}"
        def res = sql.firstRow(QueryFactory.branchByPrefixSuffix, [almProject, buildPrefix, buildSuffix])
        log.debug "findBranchByPrefixSuffix found " + res
        res
    }


    /**
     * Find all project stream evolutivo
     * @param almProject
     * @return the ProjectStreamInfo(s) with R02 suffix (evolutiva stream)
     */
    List<ProjectStreamInfo> findBranchesByR02(String almProject) {
        assert almProject: "ProjectStreamInfoDAO findBranchesByR02 failing for empy parameters: " +
                "almProject ${almProject}, buildPrefix ${buildPrefix}, buildSuffix ${buildSuffix}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(QueryFactory.branchesByR02, [almProject]).findResults { it ? new ProjectStreamInfo(it) : null }
    }

    /**
     *
     * @param almReleaseProject , is a Release project
     * @return ProjectStreamInfo list with status 5 (General Available)
     */
    public List<ProjectStreamInfo> releaseBranches(String almReleaseProject) {
        assert almReleaseProject: "ProjectStreamInfoDAO releaseBranches failing for empy parameters: " +
                "almReleaseProject ${almReleaseProject}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        //println "Query is ${QueryFactory.releaseBranches}\nwith parameter ${almReleaseProject}"
        sql.rows(QueryFactory.releaseBranches, [almReleaseProject]).findResults {
            it ? new ProjectStreamInfo(it) : null
        }

    }

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo releaseBranchBySuffix(String almReleaseProject, String buildSuffix) {
        assert almReleaseProject: "ProjectStreamInfoDAO releaseBranchBySuffix failing for empy parameters: " +
                "almReleaseProject ${almReleaseProject} buildSuffix ${buildSuffix}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        def res = sql.firstRow(QueryFactory.releaseBranchBySuffix, [almReleaseProject, buildSuffix])
        res ? new ProjectStreamInfo(res) : null
    }

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo findReleaseBranchBySuffix(String almReleaseProject, String buildSuffix) {
        assert almReleaseProject: "ProjectStreamInfoDAO findReleaseBranchBySuffix failing for empy parameters: " +
                "almReleaseProject ${almReleaseProject} buildSuffix ${buildSuffix}"
        if (!sql) {
            log.debug "Creating sql " + dbUser + ':' + dbPassword + ' dbUrl ' + dbUrl + ' using driver ' + dbDriver
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        def res = sql.firstRow(QueryFactory.releaseBranchBySuffix, [almReleaseProject, buildSuffix])
        res ? new ProjectStreamInfo(res) : null
    }

    void close() {
        if (sql)
            sql.close()
    }

    // get* function uses ProjectStreamInfo as parameter
    // on pkg project we may get a (project) stream from buildPrefix (Reference, Baseline, correttiva, SIGEXXXX)
    // while on release project it is the buildSuffix to be used when searching a stream
    ProjectStreamInfo getProjStream(ProjectStreamInfo current, String buildPrefix2Upd) {
        // should verify that it is a package based project
        assert !current.buildPrefix.equalsIgnoreCase('Release'): "getProjStream not applicable to Release project"
        ProjectStreamInfo psi2upd = this.findBranchByPrefix(current.almProjectName, buildPrefix2Upd)
        if (!psi2upd) {
            println "Stream not found for ${current.almProjectName} / ${buildPrefix2Upd}"
        }
        psi2upd
    }

    // Given a project stream, return evo project streams of the same project
    List<ProjectStreamInfo> getEvoStream(ProjectStreamInfo current) {
        // should verify that it is a package based project
        assert !current.buildPrefix.equalsIgnoreCase('Release'): "getOtherEvoStream not applicable to Release project"
        this.findBranchesByR02(current.almProjectName)
    }

    // Not sure if DAO is the right place
    // find other evolution stream in same project
    // assume that current stream is a R02 stream
    // Given a project stream, return evo project streams of the same project
    List<ProjectStreamInfo> getOtherEvoStream(ProjectStreamInfo current) {
        // should verify that it is a package based project
        assert !current.buildPrefix.equalsIgnoreCase('Release'): "getOtherEvoStream not applicable to Release project"
        List<ProjectStreamInfo> levolutiva = this.findBranchesByR02(current.almProjectName)
        levolutiva.findResults {
            ProjectStreamInfo evo ->
                // skip current stream. buildSuffix fixed to R02
                if (evo.buildPrefix != current.buildPrefix) {
                    evo
                }
        }
    }

    // A list of project stream, for each project stream get all "active" project streams
    // of the project (excluding the current one). The list will be used, after production
    // to update back all project streams that should be updated
    List<ProjectStreamInfo> getOtherProjStream(List<ProjectStreamInfo> lorig) {
        assert lorig.size() > 0: "UpdController.getOtherProjStream parameter list empty"
        List<ProjectStreamInfo> lod = lorig.inject([]) {
            List<ProjectStreamInfo> result, orig ->
                // get baseline
                ProjectStreamInfo baseline = this.findBranchByPrefix(orig.almProjectName, 'Baseline')
                result.add(baseline)
                ProjectStreamInfo correttiva = this.findBranchByPrefix(orig.almProjectName, 'correttiva')
                result.add(correttiva)
                List<ProjectStreamInfo> levolutiva = this.findBranchesByR02(orig.almProjectName)
                result.addAll(levolutiva - orig)
                result
        }
        lod
    }

    //Release specific functions. In Release buildPrefix == 'Release'
    ProjectStreamInfo getReleaseProjStream(String releaseProject, String buildSuffix2Upd) {
        ProjectStreamInfo psi2upd = this.findBranchByPrefixSuffix(releaseProject, 'Release', buildSuffix2Upd)
        if (!psi2upd) {
            println "Stream not found for ${releaseProject} / ${buildSuffix2Upd}"
        }
        psi2upd
    }

}