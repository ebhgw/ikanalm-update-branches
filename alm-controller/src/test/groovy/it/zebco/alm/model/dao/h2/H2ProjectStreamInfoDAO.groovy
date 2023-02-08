package it.zebco.alm.model.dao.h2

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import it.zebco.alm.model.dao.ProjectStreamInfoDAO

@Slf4j
class H2ProjectStreamInfoDAO implements ProjectStreamInfoDAO {

    // specific for testing
    public init() {
        String sqlTableFilePath = 'src/test/resources/V1__h2-table.sql'
        String sqlTableString = new File(sqlTableFilePath).text
        String sqlDataFilePath = 'src/test/resources/V2__h2-data.sql'
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