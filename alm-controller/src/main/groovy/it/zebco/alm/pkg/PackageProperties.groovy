package it.zebco.alm.pkg

import it.zebco.alm.utils.PropertyFileGenerator

/*
Helper class for generating a property files
The will will be uploaded to SVN
 */

class PackageProperties {

    Properties props
    Boolean loaded = false
    File generatedPropertiesFile

    void load (File ff) {
        if (ff.exists()) {
            try {
                Properties propsFromFile = new Properties()
                ff.withInputStream {
                    propsFromFile.load(it)
                }
                load(propsFromFile)
            } catch (Exception excp) {
                println "Error while loading $ff, error : ${excp.toString()}"
            }
        } else {
            println "File ${ff.canonicalPath} not found, mapping properties not loaded"
        }
    }

    void load(Map<String,String> map) {
        Properties lowerKeyProps = new Properties()
        map.keySet().each {
            String unmodKey ->
                String lowerKey = unmodKey.toString().toLowerCase()
                String value = map.get(unmodKey).toString()
                lowerKeyProps[lowerKey] = value
        }
        this.props = lowerKeyProps
        println "Loaded ${this.props.size()} entries}"
        loaded = true
    }

    void load(Properties props) {
        Properties lowerKeyProps = new Properties()
        props.keySet().each {
            String unmodKey ->
                String lowerKey = unmodKey.toString().toLowerCase()
                String value = props.getProperty(unmodKey).toString()
                lowerKeyProps[lowerKey] = value
        }
        this.props = lowerKeyProps
        println "Loaded ${this.props.size()} entries}"
        loaded = true
    }

    void generateFile() {
        if (loaded) {
            new PropertyFileGenerator().generate(props, "Upload to SVN", true)
        } else {
            println "PackageProperties.generateFile: refuse to generate an empty file. Should load properties first"
        }
    }
}
