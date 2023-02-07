package it.zebco.ikan.alm.model.dao.h2

import groovy.sql.Sql
import it.zebco.ikan.alm.model.dao.ProjectStreamInfoDAO
import it.zebco.ikan.alm.model.dao.ProjectStreamInfoDAOTrait

class H2ProjectStreamInfoDAO implements ProjectStreamInfoDAOTrait, ProjectStreamInfoDAO {

    // specific for testing
    public init() {
        String sqlTableFilePath = 'src/test/resources/V1.0__h2-table.sql'
        String sqlTableString = new File(sqlTableFilePath).text
        String sqlDataFilePath = 'src/test/resources/V2.0__h2-data.sql'
        String sqlDataString = new File(sqlDataFilePath).text

        if (!sql)
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        sql.execute(sqlTableString)
        sql.execute(sqlDataString)
    }
}

/*
    private final static dbUrl = "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1"
	private dbUser = "sa"
	private dbDriver = "org.h2.Driver"
	private dbPassword = ""
 */