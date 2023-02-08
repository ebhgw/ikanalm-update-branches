package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.proc.CommandRunner
import it.zebco.proc.CommandRunnerOnProcBuilder
import it.zebco.proc.SvnmuccCmdBuilder
import it.zebco.proc.SvnCmdBuilder

// class valid for project base repositories
@Slf4j
class SvnAdapterFactory {

    // project contains properties from gradle project
    static SvnAdapter getSvnAdaper(String user, String password, String commandPath, String source, String repoName, String repoUri) {
        assert user && password: "SvnAdapter getSvnAdaper: undefined user, password"
        assert repoUri: "SvnAdapter getSvnAdaper: undefined repository uri"
        assert source: "SvnAdapter getSvnAdaper: undefined source"

        SvnAdapter sa = new SvnAdapterImpl(user: user, password: password, repoName: repoName, repoUri: repoUri)

        CommandRunner cr = new CommandRunnerOnProcBuilder()
                .withWorkingDir(new File(source).toPath())
        sa.cr = cr
        SvnmuccCmdBuilder muccBlder = sa.muccBlder = new SvnmuccCmdBuilder(user: user, password: password)
                .withBinPath(commandPath)
        SvnCmdBuilder svnBlder = sa.svnBlder = new SvnCmdBuilder()
                .withBinPath(commandPath)
        // Deletes, Updates use SvnFctry, Dirs, Props use SvnmuccFctry
        sa.dels = new Deletes(cr: cr, muccBlder: muccBlder)
        sa.dirs = new Dirs(cr: cr, muccBlder: muccBlder)
        sa.props = new Props(cr: cr, svnBlder: svnBlder)
        sa.updates = new Updates(cr: cr, muccBlder: muccBlder)

        sa
    }
}
