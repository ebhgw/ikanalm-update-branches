package it.zebco.ikan.alm.model.dao

import it.zebco.ikan.alm.model.ProjectStreamInfo

interface ProjectStreamInfoDAO {

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public ProjectStreamInfo findBranchByPrefix(String almProject, String buildPrefix);

    /**
     *
     * @param almProject
     * @return the ProjectStreamInfo(s) with R02 suffix (evolutiva stream)
     */
    public List<ProjectStreamInfo> findBranchesByR02(String almProject);

    /**
     *
     * @param almReleaseProject , is a Release projec
     * @return ProjectStreamInfo list with status 5 (General Available)
     */
    public List<ProjectStreamInfo> releaseBranches(String almReleaseProject);

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo releaseBranchBySuffix(String almProject, String buildSuffix);

    /**
     *
     * @param almProject
     * @param buildSuffix
     * @return ProjectStreamInfo
     */
    public ProjectStreamInfo findReleaseBranchBySuffix(String almProject, String buildSuffix);

}
