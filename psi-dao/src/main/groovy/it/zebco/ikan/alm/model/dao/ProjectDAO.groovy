package it.zebco.ikan.alm.model.dao

import it.zebco.ikan.alm.model.Project

interface ProjectDAO {

    /**
     *
     * @param almProject
     * @param buildPrefix
     * @return the ProjectStreamInfo with given prefix (Baseline, correttiva, Reference)
     */
    public List<Project> projectsByRepo (String repoName);

}
