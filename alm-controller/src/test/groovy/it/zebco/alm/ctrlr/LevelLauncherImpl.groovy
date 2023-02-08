package it.zebco.alm.ctrlr

import spock.lang.Specification

class LevelLauncherImplSpec extends Specification{
    def successfulOutput = '''
Command successfully executed
Level Request 4760 was successfully created
'''
    def "def with a stub" () {
        when:
        println "launch"
        then:
        1==1
    }
}

