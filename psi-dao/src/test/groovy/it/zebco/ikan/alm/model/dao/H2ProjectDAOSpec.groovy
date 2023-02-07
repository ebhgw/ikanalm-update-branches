package it.zebco.ikan.alm.model.dao

import it.zebco.ikan.alm.model.Project
import it.zebco.ikan.alm.model.dao.h2.H2ProjectDAO
import spock.lang.Shared
import spock.lang.Specification

class H2ProjectDAOSpec extends Specification {
    @Shared H2ProjectDAO h2dao = new H2ProjectDAO([dbUrl: "jdbc:h2:mem:psidao;DB_CLOSE_DELAY=-1",dbUser: "sa",dbPassword: "",dbDriver: "org.h2.Driver"])

    // insert data (usually the database would already contain the data)
    def setupSpec() {
        println "initting db"
        h2dao.init()
    }

    def cleanupSpec() {
        println "closing db"
        h2dao.close()
    }

    def "projectsByRepo on unknown repo returns no row" () {
        when:
        Project res = h2dao.projectsByRepo('NO_REPO')
        then:
        res == null
    }

    def "projectsByRepo on IKALM_REHOST_SIGEA" () {
        when:
        Project res = h2dao.projectsByRepo('IKALM_REHOST_SIGEA')
        then:
        res.size() == 18
    }

}
