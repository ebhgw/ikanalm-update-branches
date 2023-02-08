package it.zebco.alm.model.dao.mssql

import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.dao.mssql.MssqlProjectStreamInfoDAO
import spock.lang.Shared
import spock.lang.Specification

class MssqlProjectStreamInfoDAOSpec extends Specification {

    static MssqlProjectStreamInfoDAO getMssqlProjectStreamInfoDAO(Map props, Iterable<File> jdbcjar) {
        // get
        String dbServer = props['rdbms.server']
        String dbPort = props['rdbms.port']
        String dbName = props['rdbms.dbname']
        String dbUser = props['rdbms.user']
        String dbPassword = props['rdbms.password']
        String dbDriver = props['rdbms.driver']
        if (!dbServer || !dbPort || !dbName || !dbUser || !dbPassword || !dbDriver || jdbcjar.size() == 0) {
            throw GroovyException("""Invalid db connection parameters while creating MssqlProjectStreamInfoDAO from project.properties
rdbms.server=${dbServer}<
rdbms.port=${dbPort}<
rdbms.dbname=${dbName}<
rdbms.user=${dbUser}<
rdbms.password=${dbPassword ? '*****' : dbPassword}<
rdbms.driver=${dbDriver}
driver jar=${jdbcjar.size() == 0 ? '<empty>' : jdbcjar}
        """)
        }
        String dbUrl = "jdbc:jtds:sqlserver://${dbServer}:${dbPort}/${dbName}"
        def dao = new MssqlProjectStreamInfoDAO(dbUrl, dbUser, dbPassword, dbDriver)
        dao.jdbcJarLoader(jdbcjar)
    }

    def "create MssqlProjectStreamInfoDAO" () {
        given:
        String dburl = 'dburl'
        String dbuser = 'dbuser'
        String dbpassword =  'dbpass'
        String dbdriver = 'dbdriver'
        when:
        MssqlProjectStreamInfoDAO dao = new MssqlProjectStreamInfoDAO(dburl, dbuser, dbpassword, dbdriver)
        then:
        dao.getClass() == 'class it.zebco.alm.model.dao.mssql.MssqlProjectStreamInfoDAO'
    }

    def "create MssqlProjectStreamInfoDAO fail with empty pars" () {
        given:
        String dburl = 'dburl'
        String dbuser = 'dbuser'
        String dbpassword =  'dbpass'
        String dbdriver = ''
        when:
        MssqlProjectStreamInfoDAO dao = new MssqlProjectStreamInfoDAO(dburl, dbuser, dbpassword, dbdriver)
        then:
        thrown AssertionError
    }
}
