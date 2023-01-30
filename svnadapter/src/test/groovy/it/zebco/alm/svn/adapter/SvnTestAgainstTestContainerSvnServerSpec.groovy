package it.zebco.alm.svn.adapter

import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

@Testcontainers
class SvnTestAgainstTestContainerSvnServerSpec extends Specification {
    private final static Path DOCKER_PATH = new File('../svn-docker').toPath()
    private String host
    private int port
    private String repoRootUrl
    private final String username = 'test' //preconfigured user in Dockerfile
    private final String password = 'TestP4ss' //preconfigured password in Dockerfile

    // init, expose port of the container
    @Shared
    GenericContainer testSvnServer = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withFileFromPath('.', DOCKER_PATH))
            .withExposedPorts(80, 3690)

    void setup() {
        //testSvnServer.start()
        this.host = testSvnServer.host
        this.port = testSvnServer.firstMappedPort
        this.repoRootUrl = "http://${host}:${port}/svn/test"
    }

    def "check svn ls on an existing directory" (String path, boolean res) {
        given:
        SvnLsCommand ls = new SvnLsCommandImpl(this.repoRootUrl, this.username, this.password)
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

    def "Create some directories" (String path, String res) {
        given:
        SvnmuccMkdirsCommandImpl mucc = new SvnmuccMkdirsCommandImpl(this.repoRootUrl, this.username, this.password)
        expect:
        mucc.mkDirs('branches', path) == res && checkHelper(res) == check
        where:
        path|res|check
        'newdir1'|'branches/newdir1'|true
        'newdir2/newdir3'|'branches/newdir2/newdir3'|true
        'nedir4/pippo.caio'|'branches/newdir4'|true
    }

    boolean checkHelper (String dirs) {
        SvnLsCommand ls = new SvnLsCommandImpl(repoRootUrl, this.username, this.password)
        ls.exists(dirs)
    }
}