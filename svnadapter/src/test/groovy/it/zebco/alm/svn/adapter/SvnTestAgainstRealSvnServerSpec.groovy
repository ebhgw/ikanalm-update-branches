package it.zebco.alm.svn.adapter

import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

class SvnTestAgainstRealSvnServerSpec extends Specification {
    private static final host = 'localhost'
    private static final port = 8088

    def "Use svn ls to check path existence" (String path, boolean res) {
        given:
        String repoRootUrl = "http://${host}:${port}/svn/test"
        //String repoRootUrl, String username, String password
        SvnLsCommand ls = new SvnLsCommandImpl(repoRootUrl, 'test', 'TestP4ss')
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
        //String repoRootUrl, String username, String password
        SvnLsCommand ls = new SvnLsCommandImpl(repoRootUrl, 'unknown', 'unknown')
        when:
        ls.exists('branches/correttiva')
        then:
        final SvnAdapterException rte = thrown(SvnAdapterException)
        // errorcode for wrong password
        rte.message =~ /E170013/
    }
}