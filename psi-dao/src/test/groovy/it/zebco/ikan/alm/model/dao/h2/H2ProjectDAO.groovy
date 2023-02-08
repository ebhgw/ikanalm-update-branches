package it.zebco.ikan.alm.model.dao.h2

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import it.zebco.ikan.alm.model.dao.ProjectDAO
import it.zebco.ikan.alm.model.dao.ProjectDAOTrait
import org.flywaydb.core.Flyway

@Slf4j
class H2ProjectDAO implements ProjectDAOTrait, ProjectDAO {

    H2ProjectDAO(String dbUrl, String dbUser, String dbPassword, String dbDriver) {
        this.dbUrl = dbUrl
        this.dbUser = dbUser
        this.dbPassword = dbPassword
        this.dbDriver = dbDriver
        log.debug("Creating H2ProjectDAO $dbUrl, $dbUser, $dbPassword, $dbDriver")
        this.sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
    }
}

/*
    private final static dbUrl = "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1"
	private dbUser = "sa"
	private dbDriver = "org.h2.Driver"
	private dbPassword = ""
 */