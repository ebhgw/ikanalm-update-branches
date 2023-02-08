package it.zebco.alm.model

class SvnItem {
    // owner build + repo uri, tag etc
    BuildInfo buildInfo
    // associated file
    File assocFile
    // meaningful only when used for item from a Release
    ProjectStreamInfo origin

    String getUri() {
        //URLEncoder.encode(fi.path.toString().replaceAll('\\\\', '/'), "UTF-8")
        List<String> pathElements = assocFile.path.toString().replaceAll('\\\\', '/').split('/')
        List<String> encoded = pathElements.collect {
            URLEncoder.encode(it, "UTF-8")
        }
        encoded.join('/')
    }

    // previous attempt where using isFile
    // and in case return parent (isDirtectory)
    // won't work with abstract file,
    // so return to path matching
    String getDirAsUri() {
        String path = getUri()
        path - ~/\/(\w+[.]\w+)$/
    }

    String getBranchUri() {
        buildInfo.branchUri
    }

    String getCanonicalBranchUri() {
        buildInfo.svnRepositoryUri + '/' + buildInfo.branchUri
    }

    String getCanonicalUri() {
        buildInfo.canonicalBranchUri + '/' + this.uri
    }

    // canonical is the complete uri that you may use to query the file in svn
    String getCanonicalTagUri() {
        buildInfo.canonicalTagUri + '/' + this.uri
    }

    // should remove project name and trailing '/'
    String getUriRelativeProj() {
        //println "buildInfo.branchUri ${buildInfo.branchUri}"
        //println "this.uri ${this.uri}"
        //println "buildInfo.almProjectVcrProjectName ${buildInfo.almProjectVcrProjectName}, length ${buildInfo.almProjectVcrProjectName.length()}"
        (buildInfo.branchUri + '/' + this.uri).drop(buildInfo.almProjectVcrProjectName.length() + 1)
    }

    // should remove project name and trailing '/'
    String getOriginUriRelativeProj() {
        (origin.branchUri + '/' + this.uri).drop(origin.vcrProjectName.length() + 1)
    }

    //String canonicalPath of file
    String getCanonicalPath() {
        assocFile.canonicalPath
    }

    def printItem() {
        println "File " + this.assocFile
        println "Stream " + this.buildInfo

    }

}