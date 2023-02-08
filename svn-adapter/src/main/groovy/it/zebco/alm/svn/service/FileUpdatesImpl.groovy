package it.zebco.alm.svn.service

import groovy.io.FileType
import groovy.util.logging.Slf4j

/**
 * Collect file under a directory returing
 * - absolute path
 * - relative path to the collection directory
 * - relative dir
 */
@Slf4j
class FileUpdatesImpl implements FileUpdates {

    List<Tuple> upds = new ArrayList<Tuple>()

    def addTuple(path, relPath, relDir) {
        upds.add(new Tuple(path, relPath, relDir))
    }

    /**
     * Remove root from full and return the relative path
     * so that root + relativeUrl == full
     *
     * @param root
     * @param full
     * @return
     */
    def getRelativeUrl(root, full) {
        root.toPath().relativize(full.toPath()).toString().replaceAll('\\\\', '/')
    }

    /**
     * Remove leaf if <filename>.<ext>
     *
     * @param relPath
     * @return relPath - leaf
     */
    String removeLeafFilename(String relPath) {
        relPath - ~/\/(\w+[.]\w+)$/
    }

    /**
     * Collect files under a root directory and accumulates to "upds" instance var
     * a tuple with (file fullpath, relative path, parent path)
     *
     * @param fromDir
     * @return
     */
    def collectUpdates(File fromDir) {
        fromDir.eachFileRecurse(FileType.FILES) {
            it ->
                //println "collecting ${it}"
                def relPath = getRelativeUrl(fromDir, it)
                addTuple(new Tuple(it, relPath, removeLeafFilename(relPath)))
        }
    }

    /**
     * Extract directories (path without leaf)
     *
     * @return a new list
     */
    def getDirectories() {
        upds.collect { it[2].toString() }.unique(false)
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

