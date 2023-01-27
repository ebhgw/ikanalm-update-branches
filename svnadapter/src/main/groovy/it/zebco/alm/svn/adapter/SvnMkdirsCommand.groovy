package it.zebco.alm.svn.adapter

import it.zebco.alm.model.ProjectStreamInfo

interface SvnMkdirsCommand {
    // absolute string url
    def exists(String dirUrl)
    // relative uri to canonical branch uri
    def exists(ProjectStreamInfo dest, String dirUrl)

    def mkDirs(ProjectStreamInfo dest, String dirs)

    def mkDir(String uUri, String dir)
}

