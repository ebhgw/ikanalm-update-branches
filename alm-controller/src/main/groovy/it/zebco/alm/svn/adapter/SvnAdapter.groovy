package it.zebco.alm.svn.adapter

import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.SvnItem

interface SvnAdapter {

    // uses a complete path (ie http://www.pippo.com/repo/project/branc/resource)
    Boolean exists(String dirUrl)
    void mkDirs(ProjectStreamInfo dest, String dir)

    // PROPERTIES
    // mimic svn propget command
    String propGet(String prop, String url)
    Map<String, String> getOrigin(String url)

    //update to a project stream
    void update(String branchUri, List<SvnItem> lsi, String message)
    //update to a release stream
    void update2Release(String branchUri, List<SvnItem> lsi, String message)


}