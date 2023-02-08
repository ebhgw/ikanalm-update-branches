package it.zebco.alm.cfg

import it.zebco.alm.pkg.PackageProperties
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

class ConfigurationContextSpec extends Specification {
    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder()

    def "Load from Path" () {
        given:
        File myProps = testProjectDir.newFile('myProps.properties')
        HashMap<String, String> myPropsHash = ['prop1': 'value1']
        //myPropsHash = ['prop1': 'value1', 'prop2': 'value2', 'prop3': 'value3', 'prop4': 'value4']
        myPropsHash.each {
            myProps << it.key + '=' + it.value + '\n'
        }
        ConfigurationContext context = new ConfigurationContext()
        when:
        context.loadFromPath(myProps.toPath())
        then:
        context.getProperty('prop1') == 'value1'
    }

    def "Init load properties" () {
        given:
        File myProps = testProjectDir.newFile('myProps.properties')
        HashMap<String, String> myPropsHash = ['prop1': 'value1']
        //myPropsHash = ['prop1': 'value1', 'prop2': 'value2', 'prop3': 'value3', 'prop4': 'value4']
        myPropsHash.each {
            myProps << it.key + '=' + it.value + '\n'
        }
        ConfigurationContext context = new ConfigurationContext()
        when:
        context.init(myProps)
        then:
        context.getProperty('prop1') == 'value1'
    }

    def "Init load properties and expand" () {
        given:
        File myProps = testProjectDir.newFile('myProps.properties')
        HashMap<String, String> myPropsHash = ['prop1': 'value1', 'prop2': '${prop1}/test']
        //myPropsHash = ['prop1': 'value1', 'prop2': 'value2', 'prop3': 'value3', 'prop4': 'value4']
        myPropsHash.each {
            myProps << it.key + '=' + it.value + '\n'
        }
        ConfigurationContext context = new ConfigurationContext()
        when:
        context.init(myProps)
        then:
        context.getProperty('prop2') == 'value1/test'
    }

    def "Init load properties with multiple files" () {
        given:
        File myProps1 = testProjectDir.newFile('myProps1.properties')
        HashMap<String, String> myPropsHash1 = ['prop1': 'value1', 'prop2': 'value1']
        //myPropsHash = ['prop1': 'value1', 'prop2': 'value2', 'prop3': 'value3', 'prop4': 'value4']
        myPropsHash1.each {
            myProps1 << it.key + '=' + it.value + '\n'
        }
        File myProps2 = testProjectDir.newFile('myProps2.properties')
        HashMap<String, String> myPropsHash2 = ['prop2': 'value2', 'prop3': 'value3']
        myPropsHash2.each {
            myProps2 << it.key + '=' + it.value + '\n'
        }
        ConfigurationContext context = new ConfigurationContext()
        when:
        context.init([myProps1, myProps2])
        then:
        context.getProperty('prop1') == 'value1'
        context.getProperty('prop2') == 'value1'
        context.getProperty('prop3') == 'value3'
    }
}
