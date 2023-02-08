package it.zebco.ikan.alm.model

import groovy.transform.ToString

@ToString
class Project {

    String almProjectName
    String almProjectType
    String repositoryUri
    String svnLayout
    String svnTrunk
    //project name in SVN
    String vcrProjectName

    Project(String apn) {
        almProjectName = apn
    }

    // build a Project object from a row returned by (proper) DbWrapper query
    Project(def row) {
        almProjectName = row.project_NAME
        almProjectType = row.project_PROJECTTYPE
        repositoryUri = row.svn_REPOSITORYURL
        svnLayout = row.svn_REPOSITORYLAYOUT
        // tipically reference
        svnTrunk = row.svn_TRUNKDIRECTORY
        vcrProjectName = row.project_VCRPROJECTNAME
    }
}
