package it.zebco.alm.model.dao.mssql

import groovy.sql.Sql
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import org.codehaus.groovy.GroovyException
import it.zebco.alm.model.dao.ProjectStreamInfoDAO

@Slf4j
@InheritConstructors
class MssqlProjectStreamInfoDAO implements ProjectStreamInfoDAO {

    /* inherited
    String dbUrl
    String dbUser
    String dbPassword
    String dbDriver
    Sql sql
    */
    // a configuration (list of file) passed by gradle

    private Boolean jdbcJarLoaded = false

    MssqlProjectStreamInfoDAO(String dburl, String dbuser, String dbpassword, String dbdriver) {
        //println ("MssqlProjectStreamInfoDAO >>")
        //println pProps
        //println ("MssqlProjectStreamInfoDAO <<")
        assert dburl && dbuser && dbpassword && dbdriver: "MssqlProjectStreamInfoDAO: undefined  parameters " +
                "dburl $dburl, dbuser $dbuser, dbpassword $dbpassword o dbdriver $dbdriver"
        this.dbUrl = dburl
        this.dbUser = dbuser
        this.dbPassword = dbpassword
        this.dbDriver = dbdriver
    }

    // get properties from gradle project.properties
    MssqlProjectStreamInfoDAO(Map pProps) {
        //println ("MssqlProjectStreamInfoDAO >>")
        //println pProps
        //println ("MssqlProjectStreamInfoDAO <<")
        MssqlProjectStreamInfoDAO(
                "jdbc:jtds:sqlserver://${pProps['rdbms.server']}:${pProps['rdbms.port']}/${pProps['rdbms.dbname']}".toString(),
                pProps.get('rdbms.user').toString(),
                pProps.get('rdbms.password').toString(),
                pProps.get('rdbms.driver').toString())
    }

    // Should load explicitly as Sql
    void jdbcJarLoader(Iterable<File> jjs) {
        if (jjs.size() == 0) {
            throw new GroovyException("No jar with jdbc driver available to load")
        }
        if (!jdbcJarLoaded) {
            log.debug "Preparing dao, loading jar"
            jjs.each { log.debug ">> " + it }
            log.debug "=========="
            jjs.each { Sql.classLoader.addURL it.toURI().toURL() }
            jdbcJarLoaded = true
        }
    }

    @Override
    String toString() {
        """class: ${this.getClass().toString()}
dbUrl: ${this.dbUrl}
dbUser: ${this.dbUser}
dbPassword: ${this.dbPassword}
dbDriver: ${this.dbDriver}"""
    }
}
