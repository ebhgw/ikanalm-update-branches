package it.zebco.ikan.alm.model.dao.mssql

import groovy.sql.Sql
import org.codehaus.groovy.GroovyException
import it.zebco.ikan.alm.model.dao.ProjectStreamInfoDAO
import it.zebco.ikan.alm.model.dao.ProjectStreamInfoDAOTrait

class MssqlProjectStreamInfoDAO implements ProjectStreamInfoDAOTrait, ProjectStreamInfoDAO {

    /* inherited
    String dbUrl
    String dbUser
    String dbPassword
    String dbDriver
    Sql sql
    */
    // a configuration (list of file) passed by gradle

    private Boolean jdbcJarLoaded = false

    MssqlProjectStreamInfoDAO(String dbUrl, String dbUser, String dbPassword, String dbDriver) {
        this.dbUrl = dbUrl
        this.dbUser = dbUser
        this.dbPassword = dbPassword
        this.dbDriver = dbDriver
        this.sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
    }

    /*
    void jdbcJarLoader(Iterable<File> jdbcjar) {
        if (jdbcjar.size() == 0) {
            throw new GroovyException("No jar with jdbc driver available to load")
        }
        if (!jdbcJarLoaded) {
            def sqlClassLoader = Sql.classLoader
            jdbcjar.each { sqlClassLoader.addURL it.toURI().toURL() }
            jdbcJarLoaded = true
        }
    }
     */

}
