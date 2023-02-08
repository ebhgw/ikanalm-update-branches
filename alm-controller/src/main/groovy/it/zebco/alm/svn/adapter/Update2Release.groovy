package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.SvnItem

// class valid for project base repositories
@Slf4j
class Update2Release {

    @Delegate
    SvnAccessInfo info

    // Nel caso di Reference (isHead) il vcrBranchId dal db è vuoto
    // e ci va messo il nome stesso dello stream (Reference, ovvero buildPrefix)
    void update(ProjectStreamInfo dest, List<SvnItem> lsi, String message) {
        assert lsi.size() > 0: "Empty list of updates"
        println "Updating to ${dest.canonicalBranchUri}"
        // does not automatically getSvnAdaper missing directories
        def svnmuccCmd = ['"' + commandPath + '/svnmucc' + '"', '-U', dest.canonicalBranchUri]
        lsi.each {
            svnmuccCmd.addAll(['put', it.assocFile.path, it.uri])
        }
        svnmuccCmd.addAll(['-u', info.user, '-p', info.password, '--no-auth-cache', '-m', '"' + message + '"'])
        log.info 'update: executing: ' + svnmuccCmd.join(' ')

        execute(new File(workingDirPath), svnmuccCmd.join(' '))
    }

    // update release stream, when committing add as property the projectName and projectStream of the origin
    // la uri del file è
    // isHead ? repositoryUri + '/' + vcrProjectName + '/' + svnTrunk:repositoryUri + '/' + branchId
    // per la release non andiamo ad aggiornare la head ma solo branches
    void update2Release(ProjectStreamInfo dest, List<SvnItem> lsi, String message) {
        assert lsi.size() > 0: "Empty list of updates"
        println "update2Release updating to ${dest.canonicalBranchUri}"
        String canonical = dest.canonicalBranchUri
        String svnmuccPath = '"' + info.commandPath + '/svnmucc' + '"'
        List<String> svnmuccCmd = []
        // does not automatically getSvnAdaper missing directories
        // baseUri is dest repoUri + dest branchUri
        svnmuccCmd.addAll([svnmuccPath, '-U', canonical])
        lsi.each {
            // canonical è errato, relativa alla fase e non a working
            svnmuccCmd.addAll(['put', it.assocFile.path, it.uri])
            svnmuccCmd.addAll(['propset', 'almProjectName', it.buildInfo.almProjectName, it.uri])
            svnmuccCmd.addAll(['propset', 'almProjectStreamBuildPrefix', it.buildInfo.almProjectStreamBuildPrefix, it.uri])
            svnmuccCmd.addAll(['propset', 'almProjectStreamBuildSuffix', it.buildInfo.almProjectStreamBuildSuffix, it.uri])
            //svnmuccCmd.addAll(['propset', 'originUri', it.canonicalUri, it.uri])
        }
        svnmuccCmd.addAll(['-u', info.user, '-p', info.password, '--no-auth-cache', '-m', '"' + message + '"'])
        log.info 'update2release: executing: ' + svnmuccCmd.join(' ')
        execute(new File(workingDirPath), svnmuccCmd.join(' '))
    }

    // update release stream, when committing add as property the projectName and projectStream of the origin
    // la uri del file è
    // isHead ? repositoryUri + '/' + vcrProjectName + '/' + svnTrunk:repositoryUri + '/' + branchId
    // per la release non andiamo ad aggiornare la head ma solo branches
    void update2ReleaseParamFile(List<SvnItem> lsi) {
        assert lsi.size() > 0: "Empty list of updates"
        File temp = File.createTempFile("temp", ".tmp").with {
            lsi.each {
                write 'put'
                write it.assocFile.path
                write it.uri
                write 'propset'
                write 'almProjectName'
                write it.buildInfo.almProjectName
                write it.uri
                write 'propset'
                write 'almProjectStreamBuildPrefix'
                write it.buildInfo.almProjectStreamBuildPrefix
                write it.uri
                write 'propset'
                write 'almProjectStreamBuildSuffix'
                write it.buildInfo.almProjectStreamBuildSuffix
                write it.uri
            }
        }
        temp
    }


}
