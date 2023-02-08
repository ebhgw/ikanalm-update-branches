package it.zebco.alm.model

class RepoInfoFactory {
    static getRepoInfo(String user, String password, String name, String url) {
        new RepoInfoImpl(user: user, password: password, name: name, url: url)
    }
}
