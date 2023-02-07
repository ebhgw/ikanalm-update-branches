package it.zebco.ikan.alm.model

import groovy.transform.Sortable

@Sortable(excludes = ['parameters4TuxedoSigeaBuild', 'parameters4TuxedoSigeaDeploy'])
class ProjectStreamInfo {
    String almProjectName
    //branchId, as provided by alm properties, includes the vcr project name
    String branchId
    String buildPrefix
    String buildSuffix
    Boolean isHead
    String repositoryUri
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
        isHead = row.projectStream_ISHEAD
        buildPrefix = row.projectStream_BUILDPREFIX
        buildSuffix = row.projectStream_BUILDSUFFIX
        repositoryUri = row.svn_REPOSITORYURL
        status = row.projectStream_STATUS
        svnLayout = row.svn_REPOSITORYLAYOUT
        // tipically reference
        svnTrunk = row.svn_TRUNKDIRECTORY
        vcrProjectName = row.project_VCRPROJECTNAME
    }

    String getCanonicalBranchUri() {
        isHead ? repositoryUri + '/' + vcrProjectName + '/' + svnTrunk : repositoryUri + '/' + branchId
    }

    String getBranchUri() {
        isHead ? vcrProjectName + '/' + svnTrunk : branchId
    }

    // returns a dynamic bean with parameters need for calling SIGEA_Build_Cobol.sh
    Expando getParameters4TuxedoSigeaBuild() {
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

    Expando getParameters4TuxedoSigeaDeploy() {
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
Repo uri     : ${repositoryUri}
Branch uri   : ${this.getCanonicalBranchUri()}
Vcr project name: ${vcrProjectName}
"""
    }
}
