package it.zebco.ikan.alm.model.dao

import groovy.sql.Sql
import it.zebco.ikan.alm.model.Project

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
    public Project projectsByRepo (String repoName) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.firstRow(QueryFactory.projectOnRepo, [repoName]).collect { new Project(it) }
    }

    void close() {
        if (sql)
            sql.close()
    }

}