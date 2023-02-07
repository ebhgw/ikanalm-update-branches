package it.zebco.ikan.alm.model.dao

import groovy.sql.Sql
import it.zebco.ikan.alm.model.dao.factory.Queries
import it.zebco.ikan.alm.model.ProjectStreamInfo

trait ProjectStreamInfoDAOTrait implements ProjectStreamInfoDAO {

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
    public ProjectStreamInfo findBranchByPrefix(String almProject, String buildPrefix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.firstRow(QueryFactory.branchByPrefix, [almProject, buildPrefix]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @return the ProjectStreamInfo(s) with R02 suffix (evolutiva stream)
     */
    public List<ProjectStreamInfo> findBranchesByR02(String almProject) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(QueryFactory.branchesByR02, [almProject]).collect { it -> new ProjectStreamInfo(it) }
    }

    /**
     *
     * @param almReleaseProject , is a Release project
     * @return ProjectStreamInfo list with status 5 (General Available)
     */
    public List<ProjectStreamInfo> releaseBranches(String almReleaseProject) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        //println "Query is ${QueryFactory.releaseBranches}\nwith parameter ${almReleaseProject}"
        sql.rows(Queries.releaseBranches, [almReleaseProject]).collect { new ProjectStreamInfo(it) }

    }

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo releaseBranchBySuffix(String almReleaseProject, String buildSuffix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.firstRow(QueryFactory.releaseBranchBySuffix, [almReleaseProject, buildSuffix]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo findReleaseBranchBySuffix(String almReleaseProject, String buildSuffix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.firstRow(QueryFactory.releaseBranchBySuffix, [almReleaseProject, buildSuffix]) as ProjectStreamInfo
    }

    void close() {
        if (sql)
            sql.close()
    }

}