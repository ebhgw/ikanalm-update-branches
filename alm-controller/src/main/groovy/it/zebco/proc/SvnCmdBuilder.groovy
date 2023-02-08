package it.zebco.proc
/*
Create svn command for specific purpouse
 */

//@Slf4j
class SvnCmdBuilder {

    //String user
    //String password
    String svnExe = 'svn'

    SvnCmdBuilder withBinPath(String svnBinDir) {
        if (svnBinDir) {
            if (svnBinDir.endsWith('/')) {
                this.svnExe = svnBinDir + 'svn'
            } else {
                this.svnExe = svnBinDir + '/svn'
            }
        }
        this
    }

    List<String> getExists(String uri) {
        // why this assert ?
        // see https://objectpartners.com/2015/09/02/groovy-gotcha-passing-zero-arguments-to-a-method-that-expects-one/
        assert uri: "svn exists command creation has an empty uri"
        [svnExe, 'ls', uri, '--depth', 'empty']
    }

    List<String> getPropGet(String prop, String uri) {
        [svnExe, 'propget', prop, uri]
    }
}
