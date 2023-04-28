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
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo findProjectStreamByProjectPrefixSuffix(String almProject, String buildPrefix, String buildSuffix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.firstRow(Queries.prefixSuffixQuery, [almProject, buildPrefix, buildSuffix]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findProjectStreamByPrefix(String almProject, String buildPrefix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.prefixQuery, [almProject, buildPrefix]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findGAProjectStreamByPrefix(String almProject, String buildPrefix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.gaPrefixQuery, [almProject, buildPrefix]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given suffix (R02 or release name for realeases)
     */
    public List<ProjectStreamInfo> findProjectStreamBySuffix(String almProject, String buildSuffix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.suffixQuery, [almProject, buildSuffix]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given suffix (R02 or release name for realeases)
     */
    public List<ProjectStreamInfo> findGAProjectStreamBySuffix(String almProject, String buildSuffix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.gaSuffixQuery, [almProject, buildSuffix]) as ProjectStreamInfo
    }

    /**
     * Returns projectStream of project with status GeneralAvailable
     *
     * @param almProject
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference for package projecst
     *  Release for release projects)
     */
    public List<ProjectStreamInfo> findGAProjectStream(String almProject) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.gaProjectStreamQuery, [almProject]) as ProjectStreamInfo
    }

    /**
     *
     * @param almProject
     * @return the ProjectStreamInfo(s) with R02 suffix (evolutiva stream)
     */
    public List<ProjectStreamInfo> findR02ProjectStream(String almProject) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.rows(Queries.branchesByR02, [almProject]).collect { it -> new ProjectStreamInfo(it) }
    }

    /**
     *
     * @param almReleaseProject , is a Release project
     * @return ProjectStreamInfo list with status 5 (General Available)
     */
    public List<ProjectStreamInfo> releaseProjectStreamOfRealeaseProject(String almReleaseProject) {
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
    public ProjectStreamInfo releaseProjectStreamBySuffix(String almReleaseProject, String buildSuffix) {
        if (!sql) {
            sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
        }
        sql.firstRow(Queries.releaseBranchBySuffix, [almReleaseProject, buildSuffix]) as ProjectStreamInfo
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
        sql.firstRow(Queries.releaseBranchBySuffix, [almReleaseProject, buildSuffix]) as ProjectStreamInfo
    }

    void close() {
        if (sql)
            sql.close()
    }

}