package it.zebco.ikan.alm.model.dao


import it.zebco.ikan.alm.model.dao.h2.H2ProjectStreamInfoDAO
import it.zebco.ikan.alm.model.ProjectStreamInfo
import org.flywaydb.core.Flyway
import spock.lang.Shared
import spock.lang.Specification

class ProjectStreamInfoDAOUsingH2Spec extends Specification {
    private static final dbUser="sa"
    private static final dbPassword=""
    private static final dbUrl= "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1;MODE=MSSQLServer"
    private static final dbDriver = "org.h2.Driver"
    @Shared H2ProjectStreamInfoDAO h2dao = new H2ProjectStreamInfoDAO(dbUrl, dbUser, dbPassword, dbDriver)

    // insert data (usually the database would already contain the data)
    def setupSpec() {
        Flyway flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).loggers("auto").load();
        flyway.migrate();
    }

    def cleanupSpec() {
        h2dao.close()
    }

    def "find current project stream" (String project, String prefix, String suffix) {
        given:
        ProjectStreamInfo res = h2dao.findProjectStreamByProjectPrefixSuffix(project, prefix, suffix)
        expect:
        project == res.almProjectName
        prefix == res.buildPrefix
        suffix == res.buildSuffix
        where:
        project | prefix | suffix
        'SS01OTX' | 'Baseline' | 'R01'
        'SS01OTX' | 'SIGE1801' | 'R02'
    }

    def "find current project stream returns an empty row" () {
        when:
        ProjectStreamInfo res = h2dao.findProjectStreamByProjectPrefixSuffix('NOPRJ', 'noprefix', 'nosuffix')
        then:
        res == null
    }

    def "findBranchByPrefix on nobranch returns no row" () {
        when:
        List<ProjectStreamInfo> res = h2dao.findProjectStreamByPrefix('SS01OTX', 'nobranch')

        then:
        res == null
    }

    def "findBranchByPrefix on SS01OTX Baseline" () {
        when: "Query on Baseline on a branch of SS01OTX"
        ProjectStreamInfo first = h2dao.findProjectStreamByPrefix('SS01OTX', 'Baseline')
        then: "find the project stream data"
        first.almProjectName == 'SS01OTX'
        first.vcrProjectName == 'SS01OTX'
        first.buildPrefix == 'Baseline'
    }

    // Look for general available state
    def "findBranchesByR02 returns a list of branches with state=5" () {
        when: "Query evolutiva branches of SS01OTX"
        List<ProjectStreamInfo> rows = h2dao.findR02ProjectStream('SS01OTX')
        def branches = rows.collect{ it.buildPrefix }.toSorted()
        then: "returns 2 active (of 3)"
        rows.size() == 2
        rows[0].almProjectName == 'SS01OTX'
        rows[0].vcrProjectName == 'SS01OTX'
        rows[1].almProjectName == 'SS01OTX'
        rows[1].vcrProjectName == 'SS01OTX'
        rows[0].status == 5
        rows[1].status == 5
        rows[0].buildPrefix != rows[1].buildPrefix
        branches == ['SIGE1801', 'SIGE1803']
    }

    def "query releaseBranches on non existent project" () {
        when: "Query branches on non existent"
        List<ProjectStreamInfo> rows = h2dao.releaseProjectStreamOfRealeaseProject('NoProject')
        then: "returns 0 size result set"
        rows.size() == 0
    }

    def "query releaseBranches on release project" () {
        when: "Query branches on release"
        List<ProjectStreamInfo> rows = h2dao.releaseProjectStreamOfRealeaseProject('SIGEOTX')
        then: "returns 0 size result set"
        rows.size() == 2
        rows[0].vcrProjectName == 'SIGEOTX'
        rows[1].vcrProjectName == 'SIGEOTX'
        rows[0].status == 5
        rows[1].status == 5
        rows[0].buildPrefix == 'Release'
        rows[1].buildPrefix == 'Release'
        rows[0].buildSuffix != rows[1].buildSuffix
    }

    def "releaseBranchBySuffix does not returns a row when branch status is 0" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo row = h2dao.releaseProjectStreamBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        row == null
    }

    def "releaseBranchBySuffix returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo row = h2dao.releaseProjectStreamBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        row.almProjectName == 'SIGEOTX'
        row.vcrProjectName == 'SIGEOTX'
        row.buildSuffix == 'SIGE1801'
        row.status == 5
    }

    def "findReleaseBranchBySuffix does not returns a row when branch status is 0" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo row = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        row == null
    }

    def "findReleaseBranchBySuffix returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo row = h2dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        row.almProjectName == 'SIGEOTX'
        row.vcrProjectName == 'SIGEOTX'
        row.buildSuffix == 'SIGE1801'
        row.status == 5
    }

}
