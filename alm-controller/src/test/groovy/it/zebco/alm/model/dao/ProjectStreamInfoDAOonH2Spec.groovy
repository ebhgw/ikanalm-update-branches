package it.zebco.alm.model.dao

import it.zebco.alm.model.dao.h2.H2ProjectStreamInfoDAO
import it.zebco.alm.model.ProjectStreamInfo
import spock.lang.Shared
import spock.lang.Specification

class ProjectStreamInfoDAOonH2Spec extends Specification {
    @Shared h2dao = new H2ProjectStreamInfoDAO([dbUrl: "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1",dbUser: "sa",dbPassword: "",dbDriver: "org.h2.Driver"])

    @Shared def pkgCorrStr = [project_NAME             : 'SS01OTX',
                              projectStream_VCRBRANCHID: 'SS01OTX/branches/correttiva',
                              projectStream_ISHEAD     : false,
                              projectStream_BUILDPREFIX: 'correttiva',
                              projectStream_BUILDSUFFIX: 'R01',
                              svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
                              projectStream_STATUS     : 5,
                              svn_REPOSITORYLAYOUT     : '0',
                              svn_TRUNKDIRECTORY       : 'Reference',
                              project_VCRPROJECTNAME   : 'SS01OTX']

    @Shared def pkgEvoStr = [project_NAME             : 'SS01OTX',
                          projectStream_VCRBRANCHID: 'SS01OTX/branches/SIGE1801',
                          projectStream_ISHEAD     : false,
                          projectStream_BUILDPREFIX: 'SIGE1801',
                          projectStream_BUILDSUFFIX: 'R02',
                          svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
                          projectStream_STATUS     : 5,
                          svn_REPOSITORYLAYOUT     : '0',
                          svn_TRUNKDIRECTORY       : 'Reference',
                          project_VCRPROJECTNAME   : 'SS01OTX']

    @Shared def relStr = [project_NAME             : 'SIGEOTX',
                          projectStream_VCRBRANCHID: 'SIGEOTX/branches/SIGE1801',
                          projectStream_ISHEAD     : false,
                          projectStream_BUILDPREFIX: 'Release',
                          projectStream_BUILDSUFFIX: 'SIGE1801',
                          svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
                          projectStream_STATUS     : 5,
                          svn_REPOSITORYLAYOUT     : '0',
                          svn_TRUNKDIRECTORY       : 'Reference',
                          project_VCRPROJECTNAME   : 'SIGEOTX']

    @Shared def pkgRefStr = [project_NAME             : 'SS01OTX',
                          projectStream_VCRBRANCHID: 'SS01OTX/Reference',
                          projectStream_ISHEAD     : false,
                          projectStream_BUILDPREFIX: 'Reference',
                          projectStream_BUILDSUFFIX: '',
                          svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
                          projectStream_STATUS     : 5,
                          svn_REPOSITORYLAYOUT     : '0',
                          svn_TRUNKDIRECTORY       : 'Reference',
                          project_VCRPROJECTNAME   : 'SS01OTX']

    // insert data (usually the database would already contain the data)
    def setupSpec() {
        println "initting db"
        h2dao.init()
    }

    def cleanupSpec() {
        println "closing db"
        h2dao.close()
    }

    def "findBranchByPrefix on nobranch returns no row" () {
        when:
        println "first test"
        ProjectStreamInfo res = h2dao.findBranchByPrefix('SS01OTX', 'nobranch')
        then:
        res == null
    }

    def "findBranchByPrefix on SS01OTX Baseline" () {
        when: "Query on Baseline on a branch of SS01OTX"
        ProjectStreamInfo info = h2dao.findBranchByPrefix('SS01OTX', 'Baseline')
        then: "find the project stream data"
        info.almProjectName == 'SS01OTX'
        info.vcrProjectName == 'SS01OTX'
        info.buildPrefix == 'Baseline'
        info.getClass().toString() == 'class it.zebco.alm.model.ProjectStreamInfo'

    }

    def "findBranchByPrefixSuffix on existing branch" () {
        when:
        println "first test"
        ProjectStreamInfo first = h2dao.findBranchByPrefixSuffix('SS01OTX', 'SIGE1801', 'R02')
        then:
        first.almProjectName == 'SS01OTX'
        first.buildPrefix == 'SIGE1801'
        first.buildSuffix == 'R02'
    }

    // Look for general available state
    def "findBranchesByR02 returns a list of branches with state=5" () {
        when: "Query evolutiva branches of SS01OTX"
        List<ProjectStreamInfo> infos = h2dao.findBranchesByR02('SS01OTX')
        def branches = infos.collect{ it.buildPrefix }.toSorted()
        then: "returns 2 active (of 3)"
        infos.size() == 2
        infos[0].almProjectName == 'SS01OTX'
        infos[0].vcrProjectName == 'SS01OTX'
        infos[1].almProjectName == 'SS01OTX'
        infos[1].vcrProjectName == 'SS01OTX'
        infos[0].status == 5
        infos[1].status == 5
        infos[0].buildPrefix != infos[1].buildPrefix
        branches == ['SIGE1801', 'SIGE1803']
    }

    def "releaseBranches on non existent project" () {
        when: "Query branches on non existent"
        List<ProjectStreamInfo> infos = h2dao.releaseBranches('NoProject')
        then: "returns 0 size result set"
        infos.size() == 0
    }

    def "releaseBranches on release project" () {
        when: "Query branches on release"
        List<ProjectStreamInfo> infos = h2dao.releaseBranches('SIGEOTX')
        then: "returns 0 size result set"
        infos.size() == 2
        infos[0].vcrProjectName == 'SIGEOTX'
        infos[1].vcrProjectName == 'SIGEOTX'
        infos[0].status == 5
        infos[1].status == 5
        infos[0].buildPrefix == 'Release'
        infos[1].buildPrefix == 'Release'
        infos[0].buildSuffix != infos[1].buildSuffix
    }

    def "releaseBranchBySuffix does not returns a row" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        info == null
    }

    def "releaseBranchBySuffix on existent returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        info.almProjectName == 'SIGEOTX'
        info.vcrProjectName == 'SIGEOTX'
        info.buildSuffix == 'SIGE1801'
        info.status == 5
    }

    def "findReleaseBranchBySuffix returns a null ProjectStreamInfo" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        info == null
    }

    def "findReleaseBranchBySuffix returns one ProjectStreamInfo" () {
        when: "Query a given branch on release"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        info.almProjectName == 'SIGEOTX'
        info.vcrProjectName == 'SIGEOTX'
        info.buildSuffix == 'SIGE1801'
        info.status == 5
    }

    def "findReleaseBranchBySuffix fails on empty parameters" () {
        when: "Query a given branch on release"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix(null, null)
        then: "throw assertion error"
        thrown AssertionError
    }

    def "getStreamOnPkgProj returns null as no rows found" () {
        given:
        ProjectStreamInfo psi = new ProjectStreamInfo(pkgCorrStr)
        when: "Query a given branch on pkg project"
        ProjectStreamInfo info = h2dao.getProjStream(psi, 'NotFound')
        then: "returns 0 size result set"
        info == null
    }

    def "getStreamOnPkgProj returns one Project Stream" () {
        given:
        ProjectStreamInfo psi = new ProjectStreamInfo(pkgCorrStr)
        when: "Query a given branch on pkg project"
        ProjectStreamInfo info = h2dao.getProjStream(psi, 'SIGE1801')
        then: "returns 0 size result set"
        info.almProjectName == 'SS01OTX'
        info.buildPrefix == 'SIGE1801'
    }

    def "getReleaseProjStream returns null (empty query result)" () {
        given:
        ProjectStreamInfo psi = new ProjectStreamInfo(relStr)
        when: "Query a given branch on pkg project"
        ProjectStreamInfo info = h2dao.getReleaseProjStream('SIGEOTX', 'NotFound')
        then: "returns 0 size result set"
        info == null
    }

    def "getReleaseProjStream returns one Project Stream" () {
        given:
        ProjectStreamInfo psi = new ProjectStreamInfo(relStr)
        when: "Query a given branch on pkg project"
        ProjectStreamInfo info = h2dao.getReleaseProjStream('SIGEOTX', 'SIGE1801')
        then: "returns 0 size result set"
        info.almProjectName == 'SIGEOTX'
        info.buildSuffix == 'SIGE1801'
    }

    def "getOtherEvoStream returns ProjectStreams" () {
        given:
        ProjectStreamInfo psi = new ProjectStreamInfo(pkgCorrStr)
        when: "Query a given branch on pkg project"
        List<ProjectStreamInfo> info = h2dao.getOtherEvoStream(psi)
        def branches = info.collect{ it.buildPrefix }.toSorted()
        println "Found $branches"
        then: "returns 0 size result set"
        info.size() == 2
        branches == ['SIGE1801','SIGE1803']
    }

    def "getOtherEvoStream from Release Stream fails" () {
        given:
        ProjectStreamInfo psi = new ProjectStreamInfo(pkgEvoStr)
        when: "Query a given branch on pkg project"
        List<ProjectStreamInfo> info = h2dao.getOtherEvoStream(psi)
        def branches = info.collect{ it.buildPrefix }.toSorted()
        println "Found $branches"
        then: "returns 0 size result set"
        info.size() == 1
        branches == ['SIGE1803']
    }

}
