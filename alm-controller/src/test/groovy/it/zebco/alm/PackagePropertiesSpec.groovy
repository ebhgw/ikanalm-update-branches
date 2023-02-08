package it.zebco.alm

import it.zebco.alm.pkg.PackageProperties
import org.junit.Rule
import spock.lang.Specification
import org.junit.rules.TemporaryFolder
import spock.lang.Shared

class PackagePropertiesSpec extends Specification {
    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder()
    @Shared File myProps
    @Shared HashMap<String, String> myPropsHash

    def setup() {
        myPropsHash = ['prop1': 'value1', 'prop2': 'value2', 'prop3': 'value3', 'prop4': 'value4']
        myProps = testProjectDir.newFile('myProps.properties')
        println "Created file ${myProps.canonicalPath}"
        println "myProps.exists() ${myProps.exists()}"
        //myPropsHash = ['prop1': 'value1', 'prop2': 'value2', 'prop3': 'value3', 'prop4': 'value4']
        myPropsHash.each {
            myProps << it.key + '=' + it.value + '\n'
        }
    }

    def "Load props" () {
        given:
        def pp = new PackageProperties()
        Properties props = new Properties(myPropsHash)
        when:
        pp.load(props)
        then:
        pp.props.getProperty('prop1') == 'value1'
    }

    def "Load file" () {
        given:
        def pp = new PackageProperties()
        when:
        pp.load(myProps)
        then:
        pp.props.getProperty('prop1') == 'value1'
    }

    def "Load Props and Create file" () {
        given:
        def pp = new PackageProperties()
        pp.load(myProps)
        when:
        pp.generateFile()
        // remove the line with timestamp commented
        def res = []
        pp.generatedPropertiesFile.readLines().eachWithIndex {
            item, idx ->
                if (idx != 1)
                   res << item
        }
        then:
        res.sort() == ['#Automatically upload to SVN', 'prop1=value1', 'prop2=value2', 'prop3=value3', 'prop4=value4']
    }

}
