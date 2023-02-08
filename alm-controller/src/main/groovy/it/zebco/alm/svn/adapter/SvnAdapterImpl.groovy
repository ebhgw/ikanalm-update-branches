package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.SvnItem
import it.zebco.proc.CommandRunner
import it.zebco.proc.SvnCmdBuilder
import it.zebco.proc.SvnmuccCmdBuilder

// Façade, delega ai singoli comandi

@Slf4j
class SvnAdapterImpl implements SvnAdapter {

    // not really needed but useful
    String repoName
    String repoUri
    String user
    String password

    Deletes dels
    Dirs dirs
    Props props
    Updates updates
    CommandRunner cr
    SvnmuccCmdBuilder muccBlder
    SvnCmdBuilder svnBlder


    Boolean exists(String dirUrl) {
        dirs.exists(dirUrl)
    }

    void mkDirs(ProjectStreamInfo dest, String dir) {
        dirs.mkDirs(dest.canonicalBranchUri, dir)
    }


    String propGet(String prop, String url) {
        props.propGet(url, prop)
    }
    Map<String, String> getOrigin(String url) {
        props.getOrigin(url)
    }

    //update to a project stream
    void update(String branchUri, List<SvnItem> lsi, String message) {
        updates.update(branchUri, lsi, message)
    }
    //update to a release stream
    void update2Release(String branchUri, List<SvnItem> lsi, String message) {
        updates.update2Release(branchUri, lsi, message)
    }

    void delete (String branchUri, List<SvnItem> lsi, String message) {
        dels.delete(branchUri, lsi, message)
    }
}