package it.zebco.alm.model.dao.factory;

class QueryFactory {

    private static String base_select = """
            SELECT
            project.NAME as project_NAME,
            project.VCRPROJECTNAME as project_VCRPROJECTNAME,
            projectStream.STATUS as projectStream_STATUS,
            projectStream.ISHEAD as projectStream_ISHEAD,
            projectStream.VCRBRANCHIDENTIFICATION as projectStream_VCRBRANCHID,
            projectStream.BUILDPREFIX as projectStream_BUILDPREFIX,
            projectStream.BUILDSUFFIX as projectStream_BUILDSUFFIX,
            lifecycle.NAME as lifecycle_NAME,
            ISNULL(svn.NAME,'') as vcr_NAME,
            ISNULL(svn.COMMANDPATH,'') as vcr_COMMANDPATH,
            ISNULL(svn.USERID,'') as vcr_USERID,
            ISNULL(svn.PASSWORD,'') as vcr_PASSWORD,
            ISNULL(svn.TIMEOUT,'') as vcr_TIMEOUT,
            svn.REPOSITORYURL as svn_REPOSITORYURL,
            svn.REPOSITORYLAYOUT as svn_REPOSITORYLAYOUT,
            svn.TRUNKDIRECTORY as svn_TRUNKDIRECTORY,
            svn.TAGSDIRECTORY as svn_TAGSDIRECTORY
            FROM PROJECT project
            INNER JOIN PROJECTSTREAM projectStream ON projectStream.PROJECTOID = project.OID
            INNER JOIN LIFECYCLE lifecycle ON projectStream.LIFECYCLEOID = lifecycle.OID
            LEFT OUTER JOIN SUBVERSION svn ON project.VCROID = svn.OID
            WHERE project.NAME = ?"""

    // for Baseline, Reference, correttiva, one record
    static String branchQuery = base_select + """
            and projectStream.BUILDPREFIX = ?
            """

    // for evolutiva, a collection of all active project stream (status = 5)
    static String evoQuery = base_select + """
            and projectStream.BUILDSUFFIX = 'R02'
            and projectStream.STATUS = 5
            """

    // given release project, a collection of all active project stream (status = 5)
    static String releaseQuery = base_select + """
            and projectStream.BUILDPREFIX = 'Release'
            and projectStream.BUILDSUFFIX = ?
            and projectStream.STATUS = 5
            """

    // for Baseline, Reference, correttiva, one record
    static String branchByPrefix = base_select + """
            and projectStream.BUILDPREFIX = ?
            """

    static String branchByPrefixSuffix = base_select + """
            and projectStream.BUILDPREFIX = ?
            and projectStream.BUILDSUFFIX = ?
            """

    // for evolutiva, a collection of all active project stream (status = 5)
    static String branchesByR02 = base_select + """
            and projectStream.BUILDSUFFIX = 'R02'
            and projectStream.STATUS = 5
            """

    // given release project, a collection of all active project stream (status = 5)
    static String releaseBranches = base_select + """
            and projectStream.BUILDPREFIX = 'Release'
            and projectStream.STATUS = 5
            """

    // given release project, a collection of all active project stream (status = 5)
    static String releaseBranchBySuffix = base_select + """
            and projectStream.BUILDPREFIX = 'Release'
            and projectStream.BUILDSUFFIX = ?
            and projectStream.STATUS = 5
            """
}