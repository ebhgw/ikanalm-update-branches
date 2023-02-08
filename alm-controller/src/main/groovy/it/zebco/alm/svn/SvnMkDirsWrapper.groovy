package it.zebco.alm.svn

import groovy.util.logging.Slf4j
import it.zebco.alm.proc.ExecuteWrapper

@Slf4j
class SvnMkDirsWrapper implements ExecuteWrapper {

    // pointer to the svnmucc command
    final String svnmuccCmdPath
    final String svnCmdPath

    SvnMkDirsWrapper(String svnmuccCmdPath,String svnCmdPath) {
        this.svnCmdPath = svnCmdPath
        this.svnmuccCmdPath = svnmuccCmdPath
    }

    Boolean exists(String uri) {
        exists(null, uri)
    }

    Boolean exists(Map<String, String> info, String uri) {
        String cmd = "${svnCmdPath} ls ${uri} --depth empty "
        if (info && info.get('authRequired', false)) {
            cmd = cmd + "--username ${info.user} --password ${info.password} --non-interactive"
        }
        log.debug("Executing $cmd")
        this.execute(cmd).first == 0
    }

    // url recursive, assume that filenames are <name>.<extension>
    // creates dir skipping filenames
    def mkDirs(Map<String, String> info, String dirs) {
        //println "mkDirs, dirs: ${dirs}"
        def dirsList = dirs.split('/')
        def current = info.baseUrl
        dirsList.each {
            def match = it =~ /(\w+[.]\w+)/
            if (!match && it) {
                mkDir(info, current, it)
                current = current + '/' + it
            }
        }
        current
    }

    // returns created of fail, call through mkDirs
    def mkDir(Map<String, String> info, String uUri, String dir) {
        log.debug "mkDir $uUri $dir"
        if (dir != '') {
            //println "Checking ${uUri+'/'+dir} got " + exists(uUri + '/' + dir)
            if (!exists(info, uUri + '/' + dir)) {
                String cmd = "${svnmuccCmdPath} -U ${uUri} mkdir ${dir} -u ${info.user} -p ${info.password} " +
                          "--no-auth-cache -m \"add ${dir} under ${uUri}\""
                log.debug("Executing $cmd")
                this.execute(cmd)
            }
            uUri + '/' + dir
        } else {
            uUri
        }
    }

}
