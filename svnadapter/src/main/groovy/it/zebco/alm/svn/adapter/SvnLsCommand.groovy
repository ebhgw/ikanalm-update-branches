package it.zebco.alm.svn.adapter

import it.zebco.alm.model.ProjectStreamInfo

interface SvnLsCommand {
    // absolute string url
    boolean exists(String dirUrl)
    // relative uri to canonical branch uri
    boolean exists(ProjectStreamInfo dest, String dirUrl)
}
