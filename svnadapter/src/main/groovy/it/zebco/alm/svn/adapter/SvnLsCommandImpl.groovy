package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.ProjectStreamInfo

@Slf4j
class SvnLsCommandImpl implements SvnLsCommand {

    String repoRootUrl
    int port
    String username
    String password

    SvnLsCommandImpl(String repoRootUrl, String username, String password) {
        // root of the repository
        this.repoRootUrl = repoRootUrl
        this.username = username
        this.password = password
    }

    /**
     * Check if the url provided exists in the repository
     *
     * @param relUrl, url from repository root
     * @return boolean, true if relUrl exists, false ow
     */
    boolean exists(String relUrl) {
        List<String> args = []
        args << "ls" << repoRootUrl + '/' + relUrl << "--depth" << "empty"
        args << "--no-auth-cache" << "--non-interactive"
        args << "--username" << this.username << "--password" << this.password
        log.debug "Executing svn ${args.join(' ')}"
        SvnCommandExecutor svnCmd = new SvnCommandExecutor('svn')
        svnCmd.execute(args)
        int exitValue = svnCmd.exitValue
        if (exitValue == 1) {
            // look for W160013: URL '...' non-existent in revision ?
            // ow throw exception
            log.debug "Check for error codes in ${svnCmd.baos}"
            log.debug "Match ${svnCmd.baos =~ /W160013/ as boolean}"
            if (svnCmd.baos =~ /W160013/) {
                log.debug("Matched W160013, path does not exists") //pòath does not exists
            } else {
                log.debug "Failure due to runtime errors"
                String errMsg = """svn ${args.join(' ')}
${svnCmd.baos}
"""
                log.debug "Throwing exception"
                throw new SvnAdapterException(errMsg)
            }
        }
        log.debug "Result ${exitValue}, ${exitValue == 0}"
        exitValue == 0
    }
}
