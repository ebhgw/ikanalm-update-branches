package it.zebco.ikan.alm.model.dao.h2

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import it.zebco.ikan.alm.model.dao.ProjectStreamInfoDAO
import it.zebco.ikan.alm.model.dao.ProjectStreamInfoDAOTrait

@Slf4j
class H2ProjectStreamInfoDAO implements ProjectStreamInfoDAOTrait, ProjectStreamInfoDAO {

    H2ProjectStreamInfoDAO(String dbUrl, String dbUser, String dbPassword, String dbDriver) {
        this.dbUrl = dbUrl
        this.dbUser = dbUser
        this.dbPassword = dbPassword
        this.dbDriver = dbDriver
        log.debug("Creating H2ProjectDAO $dbUrl, $dbUser, $dbPassword, $dbDriver")
        this.sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
    }
}