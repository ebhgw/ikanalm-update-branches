package it.zebco.alm.svn.adapter

interface SvnmuccPutCommand {
    /**
     * Puts a file to a destination using repo, user and possword provided
     * If info contains a baseUrl, destination url is baseUrl + dest, ow est
     * Does not create missing directories in path
     *
     * @param ff file to load
     * @param dest absolute or relative destination url
     * @return
     */
    int put(File ff, String dest)
    /**
     * Puts a file to a destination using (optional) repo, user and possword provided
     * If repo is not null, target is repo + dest
     * Does not create missing directories in path
     *
     * @param ff file to load
     * @param dest absolute or relative destination url
     * @param message
     * @return
     */
    int put(File ff, String dest, String message)
}
