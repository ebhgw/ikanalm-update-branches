package it.zebco.alm.svn.adapter

import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class SvnTestAgainstTestContainerSvnServerSpec extends Specification {
    private final static dockerPath = new File('../svn-docker').toPath()
    private String host
    private int port

    // init, expose port of the container
    @Shared
    GenericContainer testSvnServer = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromPath('.', dockerPath))
            .withExposedPorts(80, 3690)

    void setup() {
        //testSvnServer.start()
        this.host = testSvnServer.host
        this.port = testSvnServer.firstMappedPort
    }

    def "check svn ls on an existing directory" (String path, boolean res) {
        given:
        String repoRootUrl = "http://${host}:${port}/svn/test"
        SvnLsCommand ls = new SvnLsCommandImpl("http://$host:$port/svn/test", "test", "TestP4ss")
        expect:
        ls.exists(path) == res
        where:
        path | res
        "branches/correttiva"|true
        "branches/inexistent"|false
    }

    def "Check svn ls raising errors" () {
        given:
        String repoRootUrl = "http://${host}:${port}/svn/test"
        SvnLsCommand ls = new SvnLsCommandImpl(repoRootUrl, 'unknown', 'unknown')
        when:
        ls.exists('branches/correttiva')
        then:
        final SvnAdapterException rte = thrown(SvnAdapterException)
        // errorcode for wrong password
        rte.message =~ /E170013/
    }
}