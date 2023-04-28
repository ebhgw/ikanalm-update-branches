package it.zebco.alm.svn.adapter

class SvnAdapter {
    @Delegate SvnLsCommandImpl lsCmd
    @Delegate SvnmuccMkdirsCommandImpl mkdirsCmd
    @Delegate SvnmuccPutCommand putCmd

    SvnAdapter(String repoRootUrl, String username, String password) {
        this.lsCmd = new SvnLsCommandImpl(repoRootUrl, username, password)
        this.mkdirsCmd = new SvnmuccMkdirsCommandImpl(repoRootUrl, username, password)
        this.putCmd = new SvnmuccPutCommandImpl(repoRootUrl, username, password)
    }
}
