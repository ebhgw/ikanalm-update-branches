package it.zebco.alm.svn

import it.zebco.alm.proc.ExecuteWrapper

class SvnmuccWrapper implements ExecuteWrapper {

    // pointer to the svnmucc command
    final String svnmuccCmdPath
    final String svnCmdPath

    private static String extractPrintedUriFromOutput (String message) {
        java.util.regex.Matcher mm = message =~ /file:[\/]{2}.+/
        String uriString = null
        // never able to make conditional expression mm?mm[0]:null working
        if (mm.find()) {
            uriString = message.substring(mm.start(), mm.end())
        }
        uriString
    }

    def put (Map<String, String> info) {
        List<String> cmd = []
        // does not automatically create missing directories
        // baseUri is dest repoUri + dest branchUri
        cmd.addAll([svnmuccCmdPath, '-U', info.canonical])
        cmd.addAll(['put', info.path, info.uri])
        //svnmuccCmd.addAll(['propset', 'almProjectStreamBuildSuffix', it.buildInfo.almProjectStreamBuildSuffix, it.uri])
        cmd.addAll(['-u', info.user, '-p', info.password, '--no-auth-cache', '-m', '"' + info.message + '"'])
        def res = execute(cmd.join(' '))
        res.first
    }

    Boolean exists(String uri) {
        def cmd = "${svnCmdPath} ls ${uri} --depth empty"
        //println "Executing " + svnCmd
        execute(cmd).first == 0
    }

    // url recursive, assume that filenames are <name>.<extension>
    // creates dir skipping filenames
    def mkDirs(Map<String, String> dest, String dirs) {
        //println "mkDirs, dirs: ${dirs}"
        def dirsList = dirs.split('/')
        def current = dest.canonicalBranchUri
        dirsList.each {
            def match = it =~ /(\w+[.]\w+)/
            if (!match && it) {
                mkDir(current, it)
                current = current + '/' + it
            }
        }
        current
    }

    // returns created of fail, call through mkDirs
    def mkDir(String uUri, String dir) {
        // println "mkDir, dir: ${dir}"
        if (dir != '') {
            //println "Checking ${uUri+'/'+dir} got " + exists(uUri + '/' + dir)
            if (!exists(uUri + '/' + dir)) {
                //println "Not exists"
                def cmd = "${svnmuccCmdPath} -U ${uUri} mkdir ${dir} -u ${info.user} -p ${info.password} --no-auth-cache -m \"add ${dir} under ${uUri}\""
                execute(cmd)
            }
            uUri + '/' + dir
        } else {
            uUri
        }
    }

}
