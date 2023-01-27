package it.zebco.alm.svn.adapter


import groovy.util.logging.Slf4j
import it.zebco.alm.model.ProjectStreamInfo

@Slf4j
class SvnLsCommandImpl implements SvnLsCommand {

    boolean exists(String uri) {
        exists(null, uri)
    }

    boolean exists(ProjectStreamInfo dest, String uri) {
        String checkUri = dest?dest.canonicalBranchUri + '/' + uri:uri
        List<String> args = []
        args << "ls" << checkUri << "--depth" << "empty"
        args << "--no-auth-cache" << "--non-interactive"
        args << "--username" << "test" << "--password" << "TestP4ss"
        log.debug "Svn command args: $args"
        println "Svn command args: $args"
        SvnCommand svnCmd = new SvnCommand()
        svnCmd.execute(args) == 0
    }
}
