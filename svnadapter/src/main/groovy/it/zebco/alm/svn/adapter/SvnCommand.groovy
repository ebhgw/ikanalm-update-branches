package it.zebco.alm.svn.adapter

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor
import org.apache.commons.exec.PumpStreamHandler

/**
 * SvnCommand is a wrapper for executing a "svn" command
 */
class SvnCommand {

    String svnHome
    String cmd
    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    int exitValue

    SvnCommand() {
        svnHome = System.getenv('SVN_HOME')
        cmd = svnHome?svnHome + '/bin/svn':'svn'
    }

    /**
     * Runs "svn" and its args
     *
     * @param arguments to svn command provided as a list of strings
     * @return the exit value of command execution
     */
    int execute (List<String> args) {
        CommandLine cmdLine = new CommandLine(cmd);
        args.each {
            cmdLine.addArgument it
        }
        PumpStreamHandler pump = new PumpStreamHandler(baos, baos)
        // run async and pick exitCode or exception
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(30*1000);
        Executor executor = new DefaultExecutor();
        executor.setWatchdog(watchdog)
        executor.setStreamHandler(pump)
        executor.execute(cmdLine, resultHandler);
// some time later the result handler callback was invoked so we
// can safely request the exit value
        resultHandler.waitFor()
        exitValue = resultHandler.exitValue
    }
}


