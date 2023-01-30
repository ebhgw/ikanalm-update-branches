package it.zebco.alm.svn.adapter

import it.zebco.alm.model.ProjectStreamInfo

interface SvnLsCommand {
    // absolute string url
    boolean exists(String dirUrl)
}
