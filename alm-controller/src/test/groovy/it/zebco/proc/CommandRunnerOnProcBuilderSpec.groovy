package it.zebco.proc

import groovy.util.logging.Slf4j
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import java.nio.file.Paths

class CommandRunnerOnProcBuilderSpec extends Specification {
    //@Rule final TemporaryFolder testDir = new TemporaryFolder(new File('src/test/temp'))
    CommandRunnerOnProcBuilder rnr = new CommandRunnerOnProcBuilder()
            .withWorkingDir(Paths.get('src/test/temp'))

    //@Ignore
    def "execute windows command" () {
        when:
        Integer res = rnr.execute(cmd)
        then:
        res == 0
        where:
        cmd << [["cmd", "/C", "dir"], ["java", "-version"]]
    }

    def "fail windows command" () {
        expect:
        res == rnr.execute(cmd)
        where:
        res|cmd
        1|["cmd", "/C", "cd", "pippo"]
        1|["cmd", "/C", "xyz", "--version"]
        //9009|["xyz", "-version"]
    }

    //@Ignore
    def "creating file in working dir" () {
        given:
        List<String> cmd = []
        cmd.add("cmd")
        cmd.add("/C")
        cmd.add("echo")
        cmd.add("pippo")
        cmd.add(">")
        cmd.add("pippo.txt")
        //rnr has a working dir set in initializazione phase, see line 14
        File pippoFile = new File('src/test/temp', 'pippo.txt')
        when: 'echo to pippo.txt file'
        Integer res = rnr.execute(cmd)
        then:
        res == 0
        pippoFile.exists()

    }

    //@Ignore
    def "use env variable" () {
        given: 'set %GRADLE_HOME%'
        HashMap<String,String> environVars = new HashMap<String,String>()
        environVars.put("ENV_VAR", 'ENV_VALUE')
        rnr.withEnv(environVars)
        List<String> cmd = []
        cmd.add("cmd")
        cmd.add("/C")
        cmd.add("echo")
        cmd.add("%ENV_VAR%")
        when: 'echo %ENV_VAR%'
        println "Running ${cmd.join(' ')}"
        String res = rnr.withEnv(environVars).executeCaptureOutput(cmd)
        then:
        res == 'ENV_VALUE'
    }

}