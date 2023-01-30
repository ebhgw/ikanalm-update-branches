package it.zebco.alm.svn.adapter

import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class SvnTestAgainstRealSvnServerSpec extends Specification {

    // init, expose port of the container
    @Shared
    GenericContainer testSvnServer = new GenericContainer<>(DockerImageName.parse("ebomitali/local-svn-server"))
            .withExposedPorts(80, 3690)

    void setup() {
        // Assume that we have Redis running locally?
        //host = testSvnServer.host
        //port = testSvnServer.firstMappedPort
        testSvnServer.start()
        String address = testSvnServer.host
        Integer port = testSvnServer.firstMappedPort
    }

    def "check svn ls on an existing directory" (String path, boolean res) {
        given:
        String host = testSvnServer.host
        int port = testSvnServer.firstMappedPort
        String repoRootUrl = "http://${host}:${port}/svn/test"
        SvnLsCommand ls = new SvnLsCommandImpl()
        expect:
        ls.exists(path) == res
        where:
        path | res
        "branches/correttiva"|true
        "branches/inexistent"|false
    }
}