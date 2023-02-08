package it.zebco.proc

import groovy.util.logging.Slf4j

/*
Dump svn mucc command/pars to a file, one per entry
Call command with -f
svnmuccCmd.addAll([svnmuccPath, '-U', canonical])
        lsi.each {
            // canonical è errato, relativa alla fase e non a working
            svnmuccCmd.addAll(['put', it.assocFile.path, it.uri])
            svnmuccCmd.addAll(['propset', 'almProjectName', it.buildInfo.almProjectName, it.uri])
            svnmuccCmd.addAll(['propset', 'almProjectStreamBuildPrefix', it.buildInfo.almProjectStreamBuildPrefix, it.uri])
            svnmuccCmd.addAll(['propset', 'almProjectStreamBuildSuffix', it.buildInfo.almProjectStreamBuildSuffix, it.uri])
            //svnmuccCmd.addAll(['propset', 'originUri', it.canonicalUri, it.uri])
        }
        svnmuccCmd.addAll(['-u', info.user, '-p', info.password, '--no-auth-cache', '-m', '"' + message + '"'])
 */

@Slf4j
class SvnmuccCmdBuilder {

    String muccExe = 'svnmucc'
    String user
    String password
    String rootUrl

    SvnmuccCmdBuilder withBinPath(String svnBinDir) {
        if (svnBinDir) {
            if (svnBinDir.endsWith('/')) {
                this.muccExe = "\"${svnBinDir}svnmucc\""
            } else {
                this.muccExe = "\"${svnBinDir}/svnmucc\""
            }
        }
        this
    }

    SvnmuccCmdBuilder withRootUrl(String rootUrl) {
        if (rootUrl) {
            this.rootUrl = rootUrl
        }
        this
    }

    List<String> getMucc(List<String> muccAction, String commitMessage) {
        assert !muccAction.isEmpty(): "Empty list of svnmucc commands"
        assert user: "User not defined"
        assert password: "Password not defined"
        assert rootUrl: "rootUrl not defined"

        List<String> muccBody = []
        Boolean usingFileOption = false
        File tmpFile = null
        if (muccAction.join().length() > 1200) {
            tmpFile = File.createTempFile("temp", ".tmp")
            muccAction.each {
                String par -> tmpFile << par << '\n'
            }
            muccBody = ['-f', tmpFile.canonicalPath]
            usingFileOption = true
        } else {
            muccBody = muccAction
        }
        def svnmuccCmd = [muccExe, '-U', rootUrl]
        svnmuccCmd.addAll muccBody
        svnmuccCmd.addAll(['-u', this.user, '-p', this.password, '--no-auth-cache', '-m', '"' + commitMessage + '"'])
        log.info 'Executing: ' + svnmuccCmd.join(' ')
        if (usingFileOption) {
            log.info 'using svnmucc command from file content' //+ tmpFile.text.substring(0, 99) + '...'
        }
        svnmuccCmd
    }

    List<String> getMucc(String rootUrl, List<String> muccAction, String commitMessage) {
        assert !muccAction.isEmpty(): "Empty list of svnmucc commands"
        assert user: "User not defined"
        assert password: "Password not defined"
        assert rootUrl: "rootUrl not defined"

        List<String> muccBody = []
        Boolean usingFileOption = false
        File tmpFile = null
        if (muccAction.join().length() > 1200) {
            tmpFile = File.createTempFile("temp", ".tmp")
            muccAction.each {
                String par -> tmpFile << par << '\n'
            }
            muccBody = ['-f', tmpFile.canonicalPath]
            usingFileOption = true
        } else {
            muccBody = muccAction
        }
        def svnmuccCmd = [muccExe, '-U', rootUrl]
        svnmuccCmd.addAll muccBody
        svnmuccCmd.addAll(['-u', user, '-p', password, '--no-auth-cache', '-m', '"' + commitMessage + '"'])
        log.info 'Executing: ' + svnmuccCmd.join(' ')
        if (usingFileOption) {
            log.info 'using svnmucc command from file content' //+ tmpFile.text.substring(0, 99) + '...'
        }
        svnmuccCmd
    }

    // svnmucc mkdir command, fails if branchUri does not exists
    List<String> getMkDir(String rootUrl, String dir) {
        this.getMucc(rootUrl, ['mkdir', dir, "add ${dir} under ${this.rootUrl}"])
    }
}
