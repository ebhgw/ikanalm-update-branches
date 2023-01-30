package it.zebco.alm.svn.adapter

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import it.zebco.alm.model.ProjectStreamInfo

/**
 * Implements mkdirs command on svn using svnmucc
 */
@Slf4j
class SvnmuccMkdirsCommandImpl {
    String repoRootUrl
    String username
    String password
    SvnLsCommandImpl svn

    SvnmuccMkdirsCommandImpl(String repoRootUrl, String username, String password) {
        this.repoRootUrl = repoRootUrl
        this.username = username
        this.password = password
        this.svn = new SvnLsCommandImpl(repoRootUrl, username, password)
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
    /**
     * Create needed directories on target svn path. Mimic mkdirs shell command on svn repo
     *
     * @param current , the path (relative to root of repo) under which we create directories
     * @param dirs , a path to be created (dir1/dir2/...)
     * @return the complete created url
     */
    def mkDirs(String baseUrl, String dirs) {
        log.debug("mkdirs ${baseUrl}, ${dirs}")
        assert svn.exists(baseUrl): "baseUrl $baseUrl does not exists"
        log.debug("mkdirs: baseUrl exists")
        String current = baseUrl // accumulator
        def dirsList = dirs.split('/')
        dirsList.each {
            def match = it =~ /(\w+[.]\w+)/  //assume that pippo.caio is a leaf file
            if (!match && it) {
                mkDir(current, it)
                current = current + '/' + it
            }
        }
        current
    }

    /**
     * Create directory under url if not exists
     *
     * @param current , the path under which we create directories
     * @param dir , the directocy to create
     * @return the created url
     */
    def mkDir(String current, String dir) {
        log.debug "mkdir repoRootUrl: ${repoRootUrl} baseUrl: ${current}, dir: ${dir}"
        if (dir != '') {
            if (!svn.exists(current + '/' + dir)) {
                log.debug "mkdir $dir as do not exists"
                List<String> args = []
                args << "-U" << "${repoRootUrl}/${current}" << "mkdir" << dir
                        << "-u" << username << "-p" << password << "--no-auth-cache" << "--non-interactive"
                        << "-m" << "\"add ${dir} under ${current}\""
                log.debug "Executing svnmucc ${args.join(' ')}"
                SvnCommandExecutor svnmuccExecutor = new SvnCommandExecutor('svnmucc')
                svnmuccExecutor.execute(args)
                int exitValue = svnmuccExecutor.exitValue
                if (exitValue == 1) {
                    String errMsg = """svnmucc ${args.join(' ')}
${svnmuccExecutor.baos}
"""
                    throw new SvnAdapterException(errMsg)
                }
            }
            current + '/' + dir
        } else {
            current
        }
    }
}
