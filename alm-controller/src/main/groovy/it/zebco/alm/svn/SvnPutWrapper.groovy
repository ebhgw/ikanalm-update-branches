package it.zebco.alm.svn

import it.zebco.alm.proc.ExecuteWrapper

class SvnPutWrapper implements ExecuteWrapper {

    // pointer to the svnmucc command
    final String svnmuccCmdPath

    SvnPutWrapper(svnmuccCmdPath) {
        this.svnmuccCmdPath = svnmuccCmdPath
    }

    /*
    info contiene i parametri di connessione (repo, user, password, message)
    ff è il file da caricare
    dest è la target url del file su svn
    Nota, se è presente baseUrl in info, dest è interpretato come un path relativo
     */
    Tuple2<Integer, String> put (Map<String, String> info, File ff, String dest) {
        List<String> cmd = []
        // does not automatically create missing directories
        // baseUri is dest repoUri + dest branchUri
        cmd.add(svnmuccCmdPath)
        if (info.get('baseUrl', null)) {
            cmd.addAll(['-U', info.baseUrl])
        }
        // local path - destination uri
        cmd.addAll(['put', '"' + ff.canonicalPath + '"', dest])
        String message = info.get('message',null)?message:"Put ${ff.name} to $dest"
        //svnmuccCmd.addAll(['propset', 'almProjectStreamBuildSuffix', it.buildInfo.almProjectStreamBuildSuffix, it.uri])
        cmd.addAll(['-u', info.user, '-p', info.password, '--no-auth-cache', '-m', '"' + message + '"'])
        Tuple2<Integer, String> res = execute(cmd.join(' '))
        println "Outupt is ${res.second}"
        new Tuple2(res.first, cmd.join(' '))
    }
}
