package it.zebco.alm.svn.service
/*
This class should be removed and the new UpdItem implementation should be used
 */

interface SvnUpdates {

    // raccoglie gli update
    def collectUpdates(File fromDir)

    // return a set of relative paths without ending file
    def getDirectories()

    // ritorna una lista di tuple path, relpath
    def getFiles()

    def printCollected()


}

