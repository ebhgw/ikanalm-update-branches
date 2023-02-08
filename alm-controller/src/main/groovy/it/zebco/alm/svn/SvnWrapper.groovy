package it.zebco.alm.svn

import it.zebco.alm.proc.ExecuteWrapper

class SvnWrapper implements ExecuteWrapper {

    // pointer to the svn command
    final String svnCmdPath

    // This may be used only if svn is configured to allow unauthenticated read
    Boolean exists(String uri) {
        def cmd = "${svnCmdPath} ls ${uri} --depth empty"
        //println "Executing " + svnCmd
        execute(cmd).first == 0
    }
}
