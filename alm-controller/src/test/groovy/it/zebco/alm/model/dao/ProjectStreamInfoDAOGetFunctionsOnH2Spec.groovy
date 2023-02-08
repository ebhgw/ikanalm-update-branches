package it.zebco.alm.model.dao

import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.dao.h2.H2ProjectStreamInfoDAO
import spock.lang.Shared
import spock.lang.Specification
import java.lang.AssertionError

class ProjectStreamInfoDAOGetFunctionsOnH2Spec extends Specification {
    //@Shared h2dao = new H2ProjectStreamInfoDAO([dbUrl: "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1",dbUser: "sa",dbPassword: "",dbDriver: "org.h2.Driver"])

    // insert data (usually the database would already contain the data)
    /*
    def setupSpec() {
        println "initting db"
        h2dao.init()
    }

    def cleanupSpec() {
        println "closing db"
        h2dao.close()
    }
    */

    def emptyTest() {
        expect:
        1==1
    }




    /*
    def "findBranchByPrefixSuffix on nobranch returns no row" () {
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
        ProjectStreamInfo info = h2dao.releaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        info == null
    }

    def "releaseBranchBySuffix returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo info = h2dao.releaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        info.almProjectName == 'SIGEOTX'
        info.vcrProjectName == 'SIGEOTX'
        info.buildSuffix == 'SIGE1801'
        info.status == 5
    }

    def "findReleaseBranchBySuffix does not returns a row" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        info == null
    }

    def "findReleaseBranchBySuffix returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo info = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        info.almProjectName == 'SIGEOTX'
        info.vcrProjectName == 'SIGEOTX'
        info.buildSuffix == 'SIGE1801'
        info.status == 5
    }
*/
}
