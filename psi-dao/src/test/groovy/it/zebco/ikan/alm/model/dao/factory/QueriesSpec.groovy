package it.zebco.ikan.alm.model.dao.factory

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Ignore
import groovy.sql.Sql

class QueriesSpec extends Specification {
    @Shared sql = Sql.newInstance("jdbc:h2:mem:DB_CLOSE_DELAY=-1;MODE=MSSQLServer", "org.h2.Driver")
    File currDir = new File('.')

    // insert data (usually the database would already contain the data)
    def setupSpec() {
        String sqlTableFilePath = 'src/test/resources/V1.0__h2-table.sql'
        String sqlTableString = new File(sqlTableFilePath).text
        sql.execute(sqlTableString)
        String sqlDataFilePath = 'src/test/resources/V2.0__h2-data.sql'
        String sqlDataString = new File(sqlDataFilePath).text
        sql.execute(sqlDataString)
    }

    def "findBranchByPrefix on nobranch returns no row" () {
        expect:
        sql.firstRow(Queries.branchByPrefix, ['SS01OTX', 'nobranch']) == null
        //currDir.absolutePath == "D:/Workspace/IKAN-PHASE"
    }

    def "query branchByPrefix" () {
        when: "Query on Baseline on a branch of SS01OTX"
        def first = sql.firstRow(Queries.branchByPrefix, ['SS01OTX', 'Baseline'])
        then: "find the project stream data"
        first.project_NAME == 'SS01OTX'
        first.projectStream_BUILDPREFIX == 'Baseline'
    }

    def "query branchesByR02" () {
        when: "Query evolutiva branches of SS01OTX"
        def rows = sql.rows(Queries.branchesByR02, ['SS01OTX'])
        def branches = rows.collect{ it.projectStream_BUILDPREFIX }.toSorted()
        then: "returns 2 active (of 3)"
        rows.size() == 2
        rows[0].project_NAME == 'SS01OTX'
        rows[1].project_NAME == 'SS01OTX'
        rows[0].projectStream_STATUS == 5
        rows[1].projectStream_STATUS == 5
        rows[0].projectStream_BUILDPREFIX != rows[1].projectStream_BUILDPREFIX
        branches == ['SIGE1801', 'SIGE1803']
    }

    def "query releaseBranches on non existent project" () {
        when: "Query branches on non existent"
        def rows = sql.rows(Queries.releaseBranches, ['NoProject'])
        then: "returns 0 size result set"
        rows.size() == 0
    }

    def "query releaseBranches on release project" () {
        when: "Query branches on release"
        def rows = sql.rows(Queries.releaseBranches, ['SIGEOTX'])
        then: "returns 0 size result set"
        rows.size() == 2
        rows[0].project_NAME == 'SIGEOTX'
        rows[1].project_NAME == 'SIGEOTX'
        rows[0].projectStream_STATUS == 5
        rows[1].projectStream_STATUS == 5
        rows[0].projectStream_BUILDSUFFIX != rows[1].projectStream_BUILDSUFFIX
    }

    def "query releaseBranchBySuffix on status 0" () {
        when: "Query a given branch on release, branch status 0"
        def rows = sql.rows(Queries.releaseBranchBySuffix, ['SIGEOTX', 'SIGE1709'])
        then: "returns 0 size result set"
        rows.size() == 0
    }

    def "query releaseBranchBySuffix" () {
        when: "Query a given branch on release"
        def rows = sql.rows(Queries.releaseBranchBySuffix, ['SIGEOTX', 'SIGE1801'])
        then: "returns 1 size result set"
        rows.size() == 1
        rows[0].project_NAME == 'SIGEOTX'
        rows[0].projectStream_BUILDSUFFIX == 'SIGE1801'
        rows[0].projectStream_STATUS == 5
    }

}


/*
@Shared sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

def "maximum of two numbers"() {
    ...
    where:
    [a, b, c] << sql.rows("select a, b, c from maxdata")
}
 */