package it.zebco.alm.service
/**
 * Pick the files from the current package, overwrite the existing files
 * on other ProjectStream evolutive with active status of the same project
 */
interface SameProjectRepoUpdater {

    /**
     * Perform update of current package on other R02 project stream of same proejct
     */
    void doUpdateGeneralAvailableR02()
}
