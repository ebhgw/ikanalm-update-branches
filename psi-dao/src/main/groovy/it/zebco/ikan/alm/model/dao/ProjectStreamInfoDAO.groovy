package it.zebco.ikan.alm.model.dao

import it.zebco.ikan.alm.model.ProjectStreamInfo

interface ProjectStreamInfoDAO {


    /**
     * Returns the current project stream, current as it should be called during a level execution
     *
     * @param almProject
     * @param buildPrefix
     * @param buildSuffix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findGAProjectStream(String almProject);

    /**
     * Returns the current project stream, current as it should be called during a level execution
     *
     * @param almProject
     * @param buildPrefix
     * @param buildSuffix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public ProjectStreamInfo findProjectStreamByProjectPrefixSuffix(String almProject, String buildPrefix, String buildSuffix);

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findProjectStreamByPrefix(String almProject, String buildPrefix);

    /**
     * Query for GeneralAvailable ProjectStream, that is status=5
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findGAProjectStreamByPrefix(String almProject, String buildPrefix);

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findProjectStreamBySuffix(String almProject, String buildPrefix);

    /**
     * Query for GeneralAvailable ProjectStream, that is status=5
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<ProjectStreamInfo> findGAProjectStreamBySuffix(String almProject, String buildPrefix);

    /**
     *
     * @param almProject
     * @return the ProjectStreamInfo(s) with R02 suffix (evolutiva stream)
     */
    public List<ProjectStreamInfo> findR02ProjectStream(String almProject);

    /**
     *
     * @param almReleaseProject , is a Release project, a project stream with buildPrefix = Release
     * @return ProjectStreamInfo list with status 5 (General Available)
     */
    public List<ProjectStreamInfo> releaseProjectStreamOfRealeaseProject(String almReleaseProject);

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo releaseProjectStreamBySuffix(String almProject, String buildSuffix);

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo findReleaseBranchBySuffix(String almProject, String buildSuffix);

}
