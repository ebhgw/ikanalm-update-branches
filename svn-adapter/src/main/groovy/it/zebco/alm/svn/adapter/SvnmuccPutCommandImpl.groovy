package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j

@Slf4j
class SvnmuccPutCommandImpl implements SvnmuccPutCommand {

    String repoRootUrl
    String username
    String password

    SvnmuccPutCommandImpl(String repoRootUrl, String username, String password) {
        this.repoRootUrl = repoRootUrl
        this.username = username
        this.password = password
    }

    int put (File ff, String dest) {
        put (ff, dest, "Put ${ff.name} to $dest")
    }

    /**
     * Puts a file to a destination using repo, user and possword provided
     * If info contains a baseUrl, destination url is baseUrl + dest, ow est
     * Does not create missing directories in path
     *
     * @param info, a map should contains repoUrl, username, password, optional baseUrl
     * @param ff file to load
     * @param dest relative destination url
     * @return
     */
    int put (File ff, String dest, String message) {
        List<String> args = []
        // does not automatically create missing directories, should use mkdirs
        // baseUri is dest repoUri + dest branchUri
        // String baseUrl = info.get('baseUrl', null)
        if (repoRootUrl) {
            args << '-U' << repoRootUrl
        }
        // local path - destination uri
        args << 'put' << ff.canonicalPath << dest
        args << '-u' << username << '-p' << password << '--no-auth-cache'
        args << '-m' << "$message"
        log.debug "Executing svnmucc ${args.join(' ')}"
        SvnCommandExecutor svnmuccCmd = new SvnCommandExecutor('svnmucc')
        svnmuccCmd.execute(args)
        int exitValue = svnmuccCmd.exitValue
        if (exitValue != 0) {
            log.debug "Failure due to runtime errors: exit value $exitValue " + svnmuccCmd.baos
            String errMsg = """svnmucc ${args.join(' ')}
${svnmuccCmd.baos}
"""
            throw new SvnAdapterException(errMsg)
        }
        exitValue
    }
}
