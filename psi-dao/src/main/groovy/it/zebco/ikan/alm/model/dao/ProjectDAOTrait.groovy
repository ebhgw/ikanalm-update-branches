package it.zebco.ikan.alm.model.dao

import groovy.sql.Sql
import it.zebco.ikan.alm.model.Project
import it.zebco.ikan.alm.model.ProjectStreamInfo
import it.zebco.ikan.alm.model.dao.factory.Queries

trait ProjectDAOTrait implements ProjectDAO {

    String dbUrl
    String dbUser
    String dbPassword
    String dbDriver
    // implicit connection on first access
    Sql sql = null

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<Project> projectsByRepo (String repoName) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.projectOnRepo, [repoName]).collect { it -> new Project(it) }
    }

    void close() {
        if (sql)
            sql.close()
    }

}