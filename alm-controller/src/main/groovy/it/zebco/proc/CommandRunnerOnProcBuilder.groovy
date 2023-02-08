package it.zebco.proc

import groovy.util.logging.Slf4j
import org.codehaus.groovy.GroovyException
import java.nio.file.Path

@Slf4j
class CommandRunnerOnProcBuilder implements CommandRunner {

    Path workingDir = null
    Map<String, String> env = null // or [:] ?

    CommandRunner withWorkingDir(Path workingDir) {
        this.workingDir = workingDir
        this
    }

    CommandRunner withEnv(Map<String, String> env) {
        if (!env.isEmpty()) {
            this.env = env
        }
        this
    }

    Integer execute(List<String> command) {
        log.info('execute ' + command.join(' '))
        ProcessBuilder procBldr = new ProcessBuilder(command)
                .redirectErrorStream(true) // join stdout to stderr
        if (workingDir)
            procBldr.directory(workingDir.toFile())
        if (env) {
            Map<String, String> pbEnv = procBldr.environment()
            pbEnv.putAll(env)
        }
        Process runner = procBldr.start()

        runner.inputStream.eachLine {
            log.info it
        } // input to wrapper, output from comand
        runner.waitFor();
        runner.exitValue()
    }

    // capture output assume a finite aumount of data that could be
    // handled safely by process.text
    String executeCaptureOutput(List<String> command) {
        ProcessBuilder procBldr = new ProcessBuilder(command)
                .redirectErrorStream(true) // join stdout to stderr
        if (workingDir)
            procBldr.directory(workingDir.toFile())
        if (env) {
            Map<String, String> pbEnv = procBldr.environment()
            pbEnv.putAll(env)
        }
        Process runner = procBldr.start()
        runner.waitFor()
        def exitValue = runner.exitValue()
        def res = runner.in.text.trim()
        if (exitValue) {
            log.error "Got exitValue ${exitValue} while executing ${cmd}"
            log.error res
            log.error runner.err.text
            throw new GroovyException("Got exitValue ${exitValue} while executing ${cmd}")
        } else {
            //log.info "Got projectName: " + res
        }
        res
    }


}
