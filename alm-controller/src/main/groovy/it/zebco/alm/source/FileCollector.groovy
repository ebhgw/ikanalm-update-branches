package it.zebco.alm.source

import groovy.io.FileType
import groovy.transform.CompileStatic

/*
Collect all files fromDir, then relativize path
Returns a list of files
 */
@CompileStatic
class FileCollector {
    // FileType const ANY, DIRECTORIES,FILES
    File fromDir
    List<File> collected = []

    FileCollector(File fromDir) {
        assert fromDir: "File fromDir is undefined"
        assert fromDir.exists(): "File fromDir ${fromDir} does not exists"
        this.fromDir = fromDir
    }

    // collect file from a given directory and generates a file with a relative path
    // ie batch/ABCDEF.cbl
    List<File> getFiles() {
        //println "getFiles: ${fromDir}"
        fromDir.eachFileRecurse(FileType.FILES) {
            def rel = fromDir.toPath().relativize(it.toPath()).toFile()
            collected << rel
        }
        collected
    }

    /*
    def getFilenames() {
        collected.findResults  {
            File it ->
                if (it.isFile())
                    it.getName()
        }.unique(false)
    }
    */

    def printCollected() {
        collected.eachWithIndex { File entry, int i ->
            println "${i} : ${entry.path}"
        }
    }
}
