package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.SvnItem
import it.zebco.proc.CommandRunner
import it.zebco.proc.SvnmuccCmdBuilder

// requires user, passowrd, rootUrl
@Slf4j
class Updates {

    CommandRunner cr
    SvnmuccCmdBuilder muccBlder

    // Nel caso di Reference (isHead) il vcrBranchId dal db è vuoto
    // e ci va messo il nome stesso dello stream (Reference, ovvero buildPrefix)
    // branchUri è il path relativo del branch sotto il progetto (ie branch/correttiva, Reference etc)
    void update(String destBranchUri, List<SvnItem> lsi, String message) {
        assert lsi.size() > 0: "Empty list of updates"
        assert destBranchUri: "Undefined destination URI"
        // does not automatically getSvnAdaper missing directories
        List<String> pars = []
        lsi.each {
            pars.addAll(['put', it.assocFile.path, it.uri])
        }
        List<String> cmd = muccBlder.getMucc(destBranchUri, pars, message)
        log.info 'update: executing: ' + cmd.join(' ')
        //from delegate
        cr.execute(cmd)
    }

    // update release stream, when committing add as property the projectName and projectStream of the origin
    // la uri del file è
    // isHead ? repositoryUri + '/' + vcrProjectName + '/' + svnTrunk:repositoryUri + '/' + branchId
    // per la release non andiamo ad aggiornare la head ma solo branches
    void update2Release(String destBranchUri, List<SvnItem> lsi, String message) {
        assert lsi.size() > 0: "Empty list of updates"
        assert destBranchUri: "Undefined destination URI"
        println "update2Release updating to ${destBranchUri}"
        String canonical = destBranchUri
        List<String> pars = []
        // does not automatically getSvnAdaper missing directories
        // baseUri is dest repoUri + dest branchUri
        lsi.each {
            // canonical è errato, relativa alla fase e non a working
            pars.addAll(['put', it.assocFile.path, it.uri])
            pars.addAll(['propset', 'almProjectName', it.buildInfo.almProjectName, it.uri])
            pars.addAll(['propset', 'almProjectStreamBuildPrefix', it.buildInfo.almProjectStreamBuildPrefix, it.uri])
            pars.addAll(['propset', 'almProjectStreamBuildSuffix', it.buildInfo.almProjectStreamBuildSuffix, it.uri])
        }
        List<String> cmd = muccBlder.getMucc(destBranchUri, pars, message)
        log.info 'update2release: executing: ' + cmd.join(' ')
        // from delegate
        cr.execute(cmd)
    }

    void update2Release(String user, String password, String destBranchUri, List<SvnItem> lsi, String message) {
        assert lsi.size() > 0: "Empty list of updates"
        assert muccBlder.rootUrl: "svnmucc --root-url (-U) undefined"
        assert muccBlder.user: "svnmucc --user undefined"
        assert muccBlder.password: "svnmucc --password undefined"
        log.info "update2Release updating to ${destUri}"
        String canonical = destUri
        List<String> pars = []
        // does not automatically getSvnAdaper missing directories
        // baseUri is dest repoUri + dest branchUri
        lsi.each {
            // canonical è errato, relativa alla fase e non a working
            pars.addAll(['put', it.assocFile.path, it.uri])
            pars.addAll(['propset', 'almProjectName', it.buildInfo.almProjectName, it.uri])
            pars.addAll(['propset', 'almProjectStreamBuildPrefix', it.buildInfo.almProjectStreamBuildPrefix, it.uri])
            pars.addAll(['propset', 'almProjectStreamBuildSuffix', it.buildInfo.almProjectStreamBuildSuffix, it.uri])
        }
        SvnmuccCmdBuilder muccFctry = new SvnmuccCmdBuilder(user: user, password: password, rootUrl: destBranchUri)
        List<String> cmd = muccFctry.getMucc(pars, message)
        log.info 'update2release: executing: ' + cmd.join(' ')
        // from delegate
        cr.execute(cmd)
    }
}
