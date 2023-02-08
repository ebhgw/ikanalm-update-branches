package it.zebco.alm.svn.adapter

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import it.zebco.proc.CommandRunner
import it.zebco.proc.SvnCmdBuilder
import it.zebco.proc.SvnmuccCmdBuilder

@Slf4j
@InheritConstructors
class Dirs {

    CommandRunner cr;
    SvnCmdBuilder svnBlder
    SvnmuccCmdBuilder muccBlder

    Boolean exists(String uri) {
        cr.execute(svnBlder.getExists(uri)) == 0
    }

    // http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA/JA01SIGE/Reference
    // jmodel/GP£64T02.jmodel
    // exit code 0, path exists, exit code 1, path does not exists
    // strict check, raises exception
    /*
    Boolean exists(String dest, String dirUri) {
        def svnCmd = "${info.commandPath}/svn ls ${dest}/${dirUri} --depth empty"
        log.debug "Executing " + svnCmd
        execute4test(svnCmd)
    }
    */

    // url recursive, assume that filenames are <name>.<extension>
    // creates dir skipping filenames
    def mkDirs(String canonicalBranchUri, String dirs) {
        //println "mkDirs, dirs: ${dirs}"
        List<String> dirsList = dirs.split('/')
        String current = canonicalBranchUri
        dirsList.each {
            def match = it =~ /(\w+[.]\w+)/
            // found a filename, matches abcde.fgh
            if (!match && it) {
                mkDir(current, it)
                current = current + '/' + it
            }
        }
        current
    }

    // returns created uri or fails
    def mkDir(String uUri, String dir) {
        // println "mkDir, dir: ${dir}"
        if (dir != '') {
            //println "Checking ${uUri+'/'+dir} got " + exists(uUri + '/' + dir)
            if (!exists(uUri + '/' + dir)) {
                //println "Not exists"
                List<String> mkdirCmd = muccBlder.getMkDir(dir)
                cr.execute(mkdirCmd)
            }
            uUri + '/' + dir
        } else {
            uUri
        }
    }
}
