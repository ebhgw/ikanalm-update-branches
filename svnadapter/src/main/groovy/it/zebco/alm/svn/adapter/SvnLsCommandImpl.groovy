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
        log.debug "Executing svn ${args.join(' ')}"
        SvnCommand svnCmd = new SvnCommand()
        svnCmd.execute(args)
        int exitValue = svnCmd.exitValue
        if (exitValue == 1) {
            // look for W160013: URL '...' non-existent in revision ?
            // ow throw exception
            if (!svnCmd.baos =~ /W160013/)  {
                String errMsg = """svn ${args.join(' ')}
${svnCmd.baos}
"""
                throw new SvnAdapterException(errMsg)
            }

        }
        log.debug "Result ${exitValue}, ${exitValue == 0}"
        exitValue == 0
    }
}
