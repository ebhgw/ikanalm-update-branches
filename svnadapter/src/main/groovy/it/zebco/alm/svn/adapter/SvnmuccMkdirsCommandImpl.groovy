package it.zebco.alm.svn.adapter

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import it.zebco.alm.model.ProjectStreamInfo

/**
 * Implements mkdirs command on svn using svnmucc
 */
@Slf4j
@InheritConstructors
class SvnmuccMkdirsCommandImpl  {

    SvnmuccCommand svnmuccCmd = new SvnmuccCommand()
    SvnLsCommandImpl svn = new SvnLsCommandImpl()

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
     * @param current, the path under which we create directories
     * @param dirs, a path to be created (dir1/dir2/...)
     * @return the complete created url
     */
    def mkDirs(String baseUrl, String dirs) {
        assert svn.exists(baseUrl)
        String current = baseUrl // accumulator
        def dirsList = dirs.split('/')
        log.debug "Mkdirs on ${dirList}"
        dirsList.each {
            def match = it =~ /(\w+[.]\w+)/ //assume that pippo.caio is a leaf file
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
     * @param baseUrl, the path under which we create directories
     * @param dir, the directocy to create
     * @return the created url
     */
    def mkDir(String baseUrl, String dir) {
        log.debug "mkdir baseUrl: $baseUrl, dir: $dir"
        if (dir != '') {
            if (!svn.exists(baseUrl + '/' + dir)) {
                log.debug "mkdir $dir as do not exists"
                // def svnmuccCmd = "${info.commandPath}/svnmucc -U ${baseUrl} mkdir ${dir} -u ${info.user} -p ${info.password} --no-auth-cache -m \"add ${dir} under ${baseUrl}\""
                List<String> args = []
                args << "-U" << baseUrl << "mkdir" << dir << "-u" << "test" << "-p"
                        << "TestP4ss" << "--no-auth-cache" << "-m" << "add ${dir} under ${baseUrl}"
                log.debug "Executing svnmucc ${args.join(' ')}"
                svnmuccCmd.execute(args) == 0
            }
            baseUrl + '/' + dir
        } else {
            baseUrl
        }
    }
}
