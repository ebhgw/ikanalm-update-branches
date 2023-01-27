package it.zebco.alm.model
/*
Model info as collected by Ikan Alm db.
 */
import groovy.transform.Sortable
import groovy.transform.ToString

@Sortable(includes = ['almProjectName', 'buildPrefix', 'buildSuffix'])
@ToString
class ProjectStreamInfo {
    String almProjectName
    //branchId, as provided by alm properties, includes the vcr project name
    String branchId
    String buildPrefix
    String buildSuffix
    String commandPath
    Boolean isHead
    String repoName
    String repoUri
    Integer status
    String svnLayout
    String svnTrunk
    //project name in SVN
    String vcrProjectName

    ProjectStreamInfo(String apn, String pfx, String sfx) {
        almProjectName = apn
        buildPrefix = pfx
        buildSuffix = sfx
    }

    // build a ProjectStreamInfo object from a row returned by (proper) DbWrapper query
    ProjectStreamInfo(def row) {
        almProjectName = row.project_NAME
        branchId = row.projectStream_VCRBRANCHID
        buildPrefix = row.projectStream_BUILDPREFIX
        buildSuffix = row.projectStream_BUILDSUFFIX
        commandPath = row.vcr_COMMANDPATH
        isHead = row.projectStream_ISHEAD
        repoName = row.vcr_NAME
        repoUri = row.svn_REPOSITORYURL
        status = row.projectStream_STATUS
        svnLayout = row.svn_REPOSITORYLAYOUT
        // tipically Reference
        svnTrunk = row.svn_TRUNKDIRECTORY
        vcrProjectName = row.project_VCRPROJECTNAME
    }

    // complete uri inclusive of repository
    String getCanonicalBranchUri() {
        isHead ? repoUri + '/' + vcrProjectName + '/' + svnTrunk : repoUri + '/' + branchId
    }

    // relative uri. branchId includes project
    String getBranchUri() {
        isHead ? vcrProjectName + '/' + svnTrunk : branchId
    }

    // returns a dynamic bean with parameters need for calling SIGEA_Build_Cobol.sh
    def getParameters4TuxedoSigeaBuild() {
        // alm.projectStream.buildSuffix=R01 or alm.projectStream.buildPrefix=correttiva
        if (buildPrefix.toString().trim().equalsIgnoreCase('correttiva')) {
            new Expando(projectStreamType: 'correttiva',
                    streamId: 'ND',
                    envName: 'INTR1')
        } else {
            new Expando(projectStreamType: 'evolutiva',
                    streamId: buildPrefix,
                    envName: 'INTR2')
        }
    }

    def getParameters4TuxedoSigeaDeploy() {
        // alm.projectStream.buildSuffix=R01 or alm.projectStream.buildPrefix=correttiva
        if (buildPrefix.toString().trim().equalsIgnoreCase('correttiva')) {
            new Expando(projectStreamType: 'correttiva',
                    ReleaseTemp: 'ND')
        } else {
            new Expando(projectStreamType: 'evolutiva',
                    ReleaseTemp: buildSuffix)
        }
    }

    String toString() {
        """Project name: ${almProjectName}
Branch id    : ${branchId}
Build prefix : ${buildPrefix}
Build suffix : ${buildSuffix}
Is Head      : ${isHead}
Repo uri     : ${repoUri}
Branch uri   : ${this.getBranchUri()}
Canon Branch uri: ${this.getCanonicalBranchUri()}
Vcr project name: ${vcrProjectName}
"""
    }

    /*
    @Override
    public String toString() {
        ToStringBuilder.reflectionToString(this);
    }
    */
}
