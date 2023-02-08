package it.zebco.ikan.alm.model.dao

import it.zebco.ikan.alm.model.Project
import it.zebco.ikan.alm.model.dao.h2.H2ProjectDAO
import org.flywaydb.core.Flyway
import spock.lang.Shared
import spock.lang.Specification

class H2ProjectDAOSpec extends Specification {
    private static final dbUser="sa"
    private static final dbPassword=""
    private static final dbUrl= "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1;MODE=MSSQLServer"
    private static final dbDriver = "org.h2.Driver"
    @Shared H2ProjectDAO h2dao = new H2ProjectDAO(dbUrl, dbUser, dbPassword, dbDriver)

    // insert data (usually the database would already contain the data)
    def setupSpec() {
        Flyway flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).loggers("auto").load();
        flyway.migrate();
    }

    def cleanupSpec() {
        h2dao.close()
    }

    def "projectsByRepo on unknown repo returns no row" () {
        when:
        List<Project> res = h2dao.projectsByRepo('NO_REPO')
        then:
        !res
    }

    def "projectsByRepo on IKALM_REHOST_SIGEA" () {
        when:
        List<Project> res = h2dao.projectsByRepo('IKALM_REHOST_SIGEA')
        println res.first()
        then:
        res.size() == 5
    }
}
