package it.zebco.alm.svn

import groovy.io.FileType

class SvnUpdatesImpl implements SvnUpdates {

    List<Tuple> upds = new ArrayList<Tuple>()

    def addTuple(path, relPath, relDir) {
        upds.add(new Tuple(path, relPath, relDir))
    }

    def getRelativeUrl(root, full) {
        root.toPath().relativize(full.toPath()).toString().replaceAll('\\\\', '/')
    }

    String removeLeafFilename(String relPath) {
        relPath - ~/\/(\w+[.]\w+)$/
    }

    def collectUpdates(File fromDir) {
        //println "collecting from ${fromDir.absolutePath}"
        fromDir.eachFileRecurse(FileType.FILES) {
            it ->
                //println "collecting ${it}"
                def relPath = getRelativeUrl(fromDir, it)
                addTuple(new Tuple(it, relPath, removeLeafFilename(relPath)))
        }
    }

    def getDirectories() {
        upds.collect { it[2] }.unique(false)
    }

    def getFiles() {
        upds.collect { new Tuple2(it[0], it[1]) }
    }

    def printCollected() {
        println "File to update"
        upds.each {
            println it[1]
        }
        println "=============="
    }


}

