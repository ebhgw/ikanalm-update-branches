package it.zebco.alm.svn.service

import spock.lang.Specification

class TestEnvSpec extends Specification {

    def setup () {
        println System.getenv('TEST')
    }

    def "test file collect one file" () {
        when:
        true
        then:
        1==1
    }
}
