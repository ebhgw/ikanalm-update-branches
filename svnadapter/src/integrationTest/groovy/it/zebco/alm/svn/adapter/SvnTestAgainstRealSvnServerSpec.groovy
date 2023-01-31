package it.zebco.alm.svn.adapter

import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

@Ignore
class SvnTestAgainstRealSvnServerSpec extends Specification {
    private static final host = 'localhost'
    private static final port = 8088
    private final String username = 'test' //preconfigured user in Dockerfile
    private final String password = 'TestP4ss' //preconfigured password in Dockerfile
    private String repoRootUrl = "http://${host}:${port}/svn/test"

    def "Use svn ls to check path existence" (String path, boolean res) {
        given:
        SvnLsCommand ls = new SvnLsCommandImpl(repoRootUrl, username, password)
        expect:
        ls.exists(path) == res
        where:
        path | res
        "branches/correttiva"|true
        "branches/inexistent"|false
    }

    def "Check svn ls raising errors" () {
        given:
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
        SvnmuccMkdirsCommandImpl mucc = new SvnmuccMkdirsCommandImpl(repoRootUrl, username, password)
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