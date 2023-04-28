package it.zebco.alm.svn.adapter

import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

@Testcontainers
class SvnTestAgainstTestContainerSvnServerSpec extends Specification {
    private final static Path DOCKER_PATH = new File('../docker-svn').toPath()
    private String host
    private int port
    @Shared
    private String repoRootUrl
    @Shared
    private final String username = 'test' //preconfigured user in Dockerfile
    @Shared
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

    // Previous svn configuration accepted unauthenticated requests, now it requires authentication
    def "Check svn ls on an existing directory" (String path, boolean res) {
        given:
        SvnLsCommand ls = new SvnLsCommandImpl(this.repoRootUrl, this.username, this.password)
        expect:
        ls.exists(path) == res
        where:
        path | res
        "branches/correttiva"|true
        "branches/inexistent"|false
    }

    // svn ls command return exitcode=0 whether director exists or does not exists, the only way to tell
    // it to check for E170013 code in error stream. Implementation detect code and transform in an exception
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

    boolean checkHelper (String path) {
        SvnLsCommand ls = new SvnLsCommandImpl(repoRootUrl, this.username, this.password)
        ls.exists(path)
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
        'newdir4/pippo.caio'|'branches/newdir4'|true
    }

    def "Put a file via svnmucc" (String repo, String username, String password, String path, int exit) {
        given:
        SvnmuccPutCommandImpl mucc = new SvnmuccPutCommandImpl(this.repoRootUrl, this.username, this.password)
        File temp = File.createTempFile("tmp", ".txt");
        temp.deleteOnExit();
        temp << "text file"

        expect:
        mucc.put(temp, path) == exit

        where:
        repo|username|password|path|exit
        this.repoRootUrl|this.username|this.password|"branches/note1.txt"|0          // repoRootUrl + relative path destination
        null|this.username|this.password|"${this.repoRootUrl}/branches/note2.txt"|0  // absolute path destination
    }

    def "Raise excpetion on inexistent path" (String repo, String username, String password, String path) {
        given:
        SvnmuccPutCommandImpl mucc = new SvnmuccPutCommandImpl(this.repoRootUrl, this.username, this.password)
        File temp = File.createTempFile("tmp", ".txt");
        temp.deleteOnExit();
        temp << "text file"

        when:
        mucc.put(temp, path)

        then:
        thrown(SvnAdapterException)
        // error.message == may check message

        where:
        repo | username | password | path
        null | 'test' | 'TestP4ss' | 'http://localhost:8088/svn/test/inexistent/note3.txt'
        'http://localhost:8088/svn/test' | 'test' | 'TestPass' | 'inexistent/note3.txt'
    }

}