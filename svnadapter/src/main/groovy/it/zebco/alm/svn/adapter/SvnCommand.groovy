package it.zebco.alm.svn.adapter

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor

class SvnCommand {

    String svnHome
    String cmd

    SvnCommand() {
        svnHome = System.getenv('SVN_HOME')
        cmd = svnHome?svnHome + '/svn':'svn'
    }

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


