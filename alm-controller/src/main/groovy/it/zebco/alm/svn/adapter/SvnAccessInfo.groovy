package it.zebco.alm.svn.adapter

import groovy.transform.InheritConstructors

// SvnAccessInfo contains the information about the Repository
// This info is common to all items, both source and destination

@InheritConstructors
class SvnAccessInfo {

    String user
    String password
    String commandPath
    String workingDirPath

    SvnAccessInfo(Map props) {
        password = props.get("svn.password")
        user = props.get("svn.user")
        commandPath = props.get("svn.command.path")
        workingDirPath = props.get("source")
        checkInfo()
    }

    // at creation time not all attributes may be known
    def checkInfo() {
        assert user: "Attribute user not defined"
        assert password: "Attribute password not defined"
        assert commandPath: "Attribute commandPath not defined"
        assert new File(commandPath).exists(): "Not found directory for commandPath ${commandPath}"
    }
}