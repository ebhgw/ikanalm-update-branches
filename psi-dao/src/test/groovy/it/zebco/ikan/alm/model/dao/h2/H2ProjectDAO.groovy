package it.zebco.ikan.alm.model.dao.h2

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import it.zebco.ikan.alm.model.dao.ProjectDAO
import it.zebco.ikan.alm.model.dao.ProjectDAOTrait
import org.flywaydb.core.Flyway

@Slf4j
class H2ProjectDAO implements ProjectDAOTrait, ProjectDAO {

    // specific for testing
    void init() {
        String sqlTableFilePath = 'src/test/resources/h2-prj-table.sql'
        String sqlTableString = new File(sqlTableFilePath).text
        String sqlDataFilePath = 'src/test/resources/h2-prj-data.sql'
        String sqlDataString = new File(sqlDataFilePath).text

        if (!sql)
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        /*
        println "Initting"
        sql.execute(sqlTableString)
        println "Continue initting"
        sql.execute(sqlDataString)
         */
        Flyway flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).loggers("auto").load();
        flyway.migrate();
    }
}

/*
    private final static dbUrl = "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1"
	private dbUser = "sa"
	private dbDriver = "org.h2.Driver"
	private dbPassword = ""
 */