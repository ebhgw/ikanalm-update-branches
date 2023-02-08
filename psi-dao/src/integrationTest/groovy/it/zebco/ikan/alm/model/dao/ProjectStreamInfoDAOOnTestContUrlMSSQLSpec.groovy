package it.zebco.ikan.alm.model.dao

import it.zebco.ikan.alm.model.ProjectStreamInfo
import it.zebco.ikan.alm.model.dao.mssql.MssqlProjectStreamInfoDAO
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

@Testcontainers
class ProjectStreamInfoDAOOnTestContUrlMSSQLSpec extends Specification {
    private final static Path DOCKER_PATH = new File('../docker-mssql').toPath()
    @Shared private String host
    @Shared private int port
    @Shared private ProjectStreamInfoDAO dao

    /**
     * Container with MSSQL and data
     */
    @Shared
    GenericContainer mssqlServer = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromPath('.', DOCKER_PATH))
            .withExposedPorts(1433)
            .withReuse(true)

    void setupSpec() {
        host = mssqlServer.host
        port = mssqlServer.firstMappedPort
        def dbServer= host
        def dbDriver='net.sourceforge.jtds.jdbc.Driver'
        def dbUser='sa'
        def dbPassword='mssql1Ipw'
        def dbName='IKANALM'
        def dbPort= port
        //def dbUrl = "jdbc:jtds:sqlserver://${dbServer}:${dbPort}/${dbName}"
        def dbUrl = "jdbc:tc:sqlserver:latest:///"
        dao = new MssqlProjectStreamInfoDAO(dbUrl, dbUser, dbPassword, dbDriver) as ProjectStreamInfoDAO
    }

    def "findBranchByPrefix on nobranch returns no row" () {
        when:
        ProjectStreamInfo res = dao.findBranchByPrefix('SS01OTX', 'nobranch')
        then:
        res == null
    }

    def "findBranchByPrefix on SS01OTX Baseline" () {
        when: "Query on Baseline on a branch of SS01OTX"
        ProjectStreamInfo first = dao.findBranchByPrefix('SS01OTX', 'Baseline')
        then: "find the project stream data"
        first.almProjectName == 'SS01OTX'
        first.vcrProjectName == 'SS01OTX'
        first.buildPrefix == 'Baseline'
    }

    // Look for general available state
    def "findBranchesByR02 returns a list of branches with state=5" () {
        when: "Query evolutiva branches of SS01OTX"
        List<ProjectStreamInfo> rows = dao.findBranchesByR02('SS01OTX')
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
        List<ProjectStreamInfo> rows = dao.releaseBranches('NoProject')
        then: "returns 0 size result set"
        rows.size() == 0
    }

    def "query releaseBranches on release project" () {
        when: "Query branches on release"
        List<ProjectStreamInfo> rows = dao.releaseBranches('SIGEOTX')
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

    def "releaseBranchBySuffix does not returns a row" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo row = dao.releaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        row == null
    }

    def "releaseBranchBySuffix returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo row = dao.releaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        row.almProjectName == 'SIGEOTX'
        row.vcrProjectName == 'SIGEOTX'
        row.buildSuffix == 'SIGE1801'
        row.status == 5
    }

    def "findReleaseBranchBySuffix does not returns a row" () {
        when: "Query a given branch on release, branch status 0"
        ProjectStreamInfo row = dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1709')
        then: "returns 0 size result set"
        row == null
    }

    def "findReleaseBranchBySuffix returns a row" () {
        when: "Query a given branch on release"
        ProjectStreamInfo row = dao.findReleaseBranchBySuffix('SIGEOTX', 'SIGE1801')
        then: "returns 1 size result set"
        row.almProjectName == 'SIGEOTX'
        row.vcrProjectName == 'SIGEOTX'
        row.buildSuffix == 'SIGE1801'
        row.status == 5
    }
}