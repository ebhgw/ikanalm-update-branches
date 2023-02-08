package it.zebco.alm.cfg

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ConfigurationContext {

    @Delegate
    Properties props

    ConfigurationContext () {
        props = new Properties()
    }

    boolean loadFromPath(Path aPath) {
        InputStream iStream = null;
        boolean loaded = false
        // temp object to easy loading file
        try {
            // Loading properties file from the path (relative path given here)
            iStream = Files.newInputStream(aPath)
            props.load(iStream)
            loaded = true
        } catch (IOException ioe) {
            println "IOException ${ioe.getMessage()} while loading ${aPath.toString()}"
        } finally {
            try {
                if (iStream != null) {
                    iStream.close();
                }
            } catch (IOException ioe) {
                println "IOException ${ioe.getMessage()} while loading ${aPath.toString()}"
            }
        }
        loaded
    }
/*
    def init(File propFile) {
        def ant = new AntBuilder()
        // load variables will be used in expansion
        ant.loadproperties(srcFile: propFile)
        // expand using the loaded properties into xpProps
        File xpProps = File.createTempFile("tmp", ".properties")
        ant.copy(tofile: xpProps.canonicalPath, file: propFile.canonicalPath, overwrite: true) {
            filterchain() {
                expandproperties()
            }
        }
        loadFromPath(xpProps.toPath())
        Files.delete(xpProps.toPath())
    }
*/
    def init(File propFile) {
        init([propFile])
    }

    def init(List<File> pfs) {
        def ant = new AntBuilder()
        // load variables will be used in expansion
        pfs.each {
            ant.loadproperties(srcFile: it)
        }
        File unexpanded = File.createTempFile("tmp", ".properties")
        ant.echoproperties (destfile:unexpanded)
        // expand using the loaded properties into xpProps
        File expanded = File.createTempFile("tmp", ".properties")
        ant.copy(tofile: expanded.canonicalPath, file: unexpanded.canonicalPath, overwrite: true) {
            filterchain() {
                expandproperties()
            }
        }
        loadFromPath(expanded.toPath())
        Files.delete(expanded.toPath())
    }
}
