package it.zebco.alm.utils

class PropertyFileGenerator {

    void generate(Properties props, String comment, Boolean deleteOnExit = true) {
        File generatedPropertiesFile = null
        if (!props.isEmpty()) {
            generatedPropertiesFile = File.createTempFile("tmp", ".properties")
            generatedPropertiesFile.withOutputStream {
                props.store(it, comment)
            }
        } else {
            println "PackageProperties.generateFile: refuse to generate an empty file. Should load properties first"
        }
        if (deleteOnExit)
            generatedPropertiesFile.deleteOnExit()
        generatedPropertiesFile
    }

}
