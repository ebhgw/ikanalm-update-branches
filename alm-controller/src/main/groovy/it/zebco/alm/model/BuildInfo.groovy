package it.zebco.alm.model

// BuildInfo stores the build properties

class BuildInfo {
    String almLevelRequestVcrTag        // 'B_SIGE1801_R02_SS01OTX-EVO-305_b2'
    String almProjectName               // 'SS01OTX'
    String almProjectVcrName            // 'IKALM_REHOST_SIGEA''
    String almProjectVcrProjectName     // 'SS01OTX'
    String almProjectStreamBuildPrefix  // 'SIGE1801'
    String almProjectStreamBuildSuffix  // 'R02'
    String almProjectStreamType         // 'B'
    String almProjectStreamVcrBranchId  // 'SS01OTX/branches/SIGE1801'
    String svnRepositoryUri             // http://svn_sigea.rmasede.grma.net:8088/svn/IKALM_SIGEA
    String svnRepositoryTrunk           // Reference
    String svnRepositoryTag             // TagVersioni

    BuildInfo(Map props) {
        almLevelRequestVcrTag = props.get('alm.levelRequest.vcrTag')
        almProjectName = props.get('alm.project.name')
        almProjectVcrName = props.get('alm.project.vcrName')
        almProjectVcrProjectName = props.get('alm.project.vcrProjectName')
        almProjectStreamBuildPrefix = props.get('alm.projectStream.buildPrefix')
        almProjectStreamBuildSuffix = props.get('alm.projectStream.buildSuffix')
        almProjectStreamType = props.get('alm.projectStream.type')
        almProjectStreamVcrBranchId = props.get('alm.projectStream.vcrBranchId')
        svnRepositoryUri = props.get('svn.repository.uri')
        svnRepositoryTrunk = props.get('svn.repository.trunk')
        svnRepositoryTag = props.get('svn.repository.tag')
    }

    // complete uri inclusive of repository
    String getCanonicalBranchUri() {
        almProjectStreamType == 'H' ? svnRepositoryUri + '/' + almProjectVcrProjectName + '/' + svnRepositoryTrunk : svnRepositoryUri + '/' + almProjectStreamVcrBranchId
    }

    // relative uri. branchId includes project
    String getBranchUri() {
        almProjectStreamType == 'H' ? almProjectVcrProjectName + '/' + svnRepositoryTrunk : almProjectStreamVcrBranchId
    }

    // relative uri. branchId includes project
    String getCanonicalTagUri() {
        svnRepositoryUri + '/' + almProjectVcrProjectName + '/' + svnRepositoryTag + '/' + almLevelRequestVcrTag
    }

}
