package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.SvnItem
import it.zebco.proc.SvnmuccCmdBuilder
import it.zebco.proc.CommandRunner

// requires user, passowrd, rootUrl
@Slf4j
class Deletes {

    CommandRunner cr;
    SvnmuccCmdBuilder muccBlder

    // Nel caso di Reference (isHead) il vcrBranchId dal db è vuoto
    // e ci va messo il nome stesso dello stream (Reference, ovvero buildPrefix)
    void delete(String destBranchUri, List<SvnItem> lsi, String message) {
        assert lsi.size() > 0: "Empty list of updates"
        assert destBranchUri: "svnmucc --root-url (-U) undefined"
        // does not automatically getSvnAdaper missing directories
        List<String> pars = []
        lsi.each {
            pars.addAll(['rm', it.uri])
        }
        List<String> cmd = muccBlder.getMucc(destBranchUri, pars, message)
        log.info 'executing: ' + cmd.join(' ')
        //from delegate
        cr.execute(cmd)
    }
}
