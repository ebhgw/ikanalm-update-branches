package it.zebco.alm.svn.adapter

interface SvnLsCommand {
    // absolute string url
    boolean exists(String dirUrl)
}
