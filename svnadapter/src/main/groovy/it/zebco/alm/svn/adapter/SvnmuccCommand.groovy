package it.zebco.alm.svn.adapter

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor
import org.apache.commons.exec.PumpStreamHandler

class SvnmuccCommand {

    String svnHome
    String cmd

    SvnmuccCommand () {
        svnHome = System.getenv('SVN_HOME')
        cmd = svnHome?svnHome + 'bin/svnmucc':'svnmucc'
        //cmd = 'svnmucc'
        //println "this.cmd = $cmd"
    }

    int execute (List<String> args) {
        CommandLine cmdLine = new CommandLine(cmd);
        args.each {
            cmdLine.addArgument it
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PumpStreamHandler pump = new PumpStreamHandler(baos, baos)
        //baos = new ByteArrayOutputStream();
        //        this.exec.setStreamHandler(new PumpStreamHandler(baos, baos));
        //  final PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(System.out, System.err);
        //  final DefaultExecutor executor = new DefaultExecutor();
        //  executor.setStreamHandler(pumpStreamHandler);
        // run async and pick exitCode or exception
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(30*1000);
        Executor executor = new DefaultExecutor();
        executor.setStreamHandler(pump)
        executor.setWatchdog(watchdog);
        executor.execute(cmdLine, resultHandler);

// some time later the result handler callback was invoked so we
// can safely request the exit value
        resultHandler.waitFor()
        resultHandler.exitValue
    }
}


