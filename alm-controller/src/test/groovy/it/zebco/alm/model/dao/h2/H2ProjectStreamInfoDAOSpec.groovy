package it.zebco.alm.model.dao.h2

import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.dao.ProjectStreamInfoDAO
import spock.lang.Shared
import spock.lang.Specification
import groovy.sql.Sql
import it.zebco.alm.model.dao.factory.QueryFactory
import it.zebco.alm.model.dao.h2.H2ProjectStreamInfoDAO

class H2ProjectStreamInfoDAOSpec extends Specification {
    @Shared ProjectStreamInfoDAO h2dao = new H2ProjectStreamInfoDAO([dbUrl: "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1", dbUser: "sa", dbPassword: "", dbDriver: "org.h2.Driver"])

    def setupSpec() {
        println "initting db"
        h2dao.init()
    }

    def cleanupSpec() {
        println "closing db"
        h2dao.close()
    }

    def "findBranchByPrefix on nobranch returns no row" () {
        expect:
        h2dao.findBranchByPrefix('SS01OTX', 'nobranch') == []
        //currDir.absolutePath == "D:/Workspace/IKAN-PHASE"
    }

    def "query branchByPrefix on Baseline" () {
        when: "Query on Baseline on a branch of SS01OTX"
        def rows = h2dao.findBranchByPrefix('SS01OTX', 'Baseline')
        ProjectStreamInfo first = rows.first()
        then: "find the project stream data"
        rows.size() == 1
        first.almProjectName == 'SS01OTX'
        first.buildPrefix == 'Baseline'
    }

    def "query branchesByPrefix on Evolutiva" () {
        when: "Query evolutiva branches of SS01OTX"
        def rows = h2dao.findBranchByPrefix('SS01OTX', 'SIGE1801')
        def branches = rows.collect{ it.buildPrefix }.toSorted()
        then: "returns 1 row"
        rows.size() == 1
        rows[0].almProjectName == 'SS01OTX'
        rows[0].status == 5
        rows[0].buildPrefix == 'SIGE1801'
    }

    def "query branchesBySuffix on R02" () {
        when: "Query evolutiva branches of SS01OTX"
        List<ProjectStreamInfo> rows = h2dao.findBranchBySuffix('SS01OTX','R02')
        def branches = rows.collect{ it.buildPrefix }.toSorted()
        then: "returns 2 active (of 3)"
        rows.size() == 3
        rows[0].almProjectName == 'SS01OTX'
        rows[1].almProjectName == 'SS01OTX'
        rows[0].status == 5
        rows[1].status == 5
        rows[0].buildPrefix != rows[1].buildPrefix
        branches == ['SIGE1801', 'SIGE1803', 'SIGE1805']
    }

}
