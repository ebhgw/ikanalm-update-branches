package it.zebco.alm.svn.adapter

import it.zebco.alm.controller.UpdController
import it.zebco.alm.svn.adapter.SvnAccessInfo
import spock.lang.Specification

/*
 * This Spock specification was generated by the Gradle 'init' task.
 */

class SvnAccessInfoSpec extends Specification {

    Map<String,String> projectProperties = [:]

    def setup () {
        projectProperties << ['svn.command.path': 'C:/Program Files/SlikSvn/bin']
        projectProperties << ['svn.commit.text': 'commit commit']
        projectProperties << ['svn.password': 'PA66TEST']
        projectProperties << ['svn.user': 'EY00018']
    }


    def "create SvnAccesInfo" () {
        when:
        def sai = new SvnAccessInfo(projectProperties)
        then:
        noExceptionThrown()
    }

}
