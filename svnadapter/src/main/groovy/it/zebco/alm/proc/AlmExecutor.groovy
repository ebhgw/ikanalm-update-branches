package it.zebco.alm.proc

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor

class AlmExecutor {

    String cmd

    /**
     * Runs "cmd" and its args
     *
     * @param arguments to svn command provided as a list of strings
     * @return the exit value of command execution
     */
    int execute (List<String> args) {
        CommandLine cmdLine = new CommandLine(cmd);
        args.each {
            cmdLine.addArgument it
        }
        // run async and pick exitCode or exception
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(30*1000);
        Executor executor = new DefaultExecutor();
        executor.setWatchdog(watchdog);
        executor.execute(cmdLine, resultHandler);
// some time later the result handler callback was invoked so we
// can safely request the exit value
        resultHandler.waitFor()
        resultHandler.exitValue
    }

}
