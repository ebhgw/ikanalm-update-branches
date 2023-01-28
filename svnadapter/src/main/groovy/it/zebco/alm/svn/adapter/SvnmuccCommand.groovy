package it.zebco.alm.svn.adapter

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.ExecuteWatchdog
import org.apache.commons.exec.Executor

class SvnmuccCommand extends it.zebco.alm.proc.AlmExecutor {

    String svnHome
    String cmd

    SvnmuccCommand () {
        super()
        svnHome = System.getenv('SVN_HOME')
        cmd = svnHome?svnHome + '/svnmucc':'svnmucc'
    }
}


