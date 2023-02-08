package it.zebco.alm.model

import it.zebco.alm.source.FileCollector
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/*
 * This Spock specification was generated by the Gradle 'init' task.
 */

class SvnItemAssocFileSpec extends Specification {

    @Rule
    final TemporaryFolder testDir = new TemporaryFolder(new File('src/test/temp'))

    Map<String, String> lprops = new HashMap()
    BuildInfo bi
    ProjectStreamInfo psi

    def setup() {
        // ProjectStreamInfo
        def row1 = [project_NAME             : 'SS01OTX',
                    projectStream_VCRBRANCHID: 'SS01OTX/branches/correttiva',
                    projectStream_ISHEAD     : false,
                    projectStream_BUILDPREFIX: 'SIGE1801',
                    projectStream_BUILDSUFFIX: 'R02',
                    vcr_COMMANDPATH         : 'C:/bin',
                    vcr_NAME                 : 'IKALM_REHOST_SIGEA',
                    svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
                    projectStream_STATUS     : 5,
                    svn_REPOSITORYLAYOUT     : '0',
                    svn_TRUNKDIRECTORY       : 'Reference',
                    project_VCRPROJECTNAME   : 'SS01OTX']
        psi = new ProjectStreamInfo(row1)

        //BuildInfo
        lprops.put('alm.levelRequest.vcrTag', 'B_SIGE1801_R02_SS01OTX-EVO-305_b2')
        lprops.put('alm.project.name', 'SS01OTX')
        lprops.put('alm.project.vcrName', 'IKALM_REHOST_SIGEA')
        lprops.put('alm.project.vcrProjectName', 'SS01OTX')
        lprops.put('alm.projectStream.buildPrefix', 'SIGE1801')
        lprops.put('alm.projectStream.buildSuffix', 'R02')
        lprops.put('alm.projectStream.type', 'B')
        lprops.put('alm.projectStream.vcrBranchId', 'SS01OTX/branches/SIGE1801')
        lprops.put('svn.repository.uri', 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA')
        lprops.put('svn.repository.trunk', 'Reference')
        lprops.put('svn.repository.tag', 'TagVersioni')
        bi = new BuildInfo(lprops)
    }

    def "canonicalUri on batch/test.cbl"() {
        given:
        File working = testDir.newFolder('source', '100')
        File batchDir = testDir.newFolder('source', '100', 'batch')
        File testCbl = new File(batchDir, 'test.cbl')
        testCbl.createNewFile()

        FileCollector fc = new FileCollector(working)
        File assocFile = fc.getFiles()[0]
        SvnItem si = new SvnItem(buildInfo: bi, assocFile: assocFile, origin: psi)
        when:
        def uri = si.canonicalUri
        then:
        uri == 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA/SS01OTX/branches/SIGE1801/batch/test.cbl'
    }

    def "asUri on  on batch/test.cb"() {
        given:
        File working = testDir.newFolder('source', '200')
        File batchDir = testDir.newFolder('source', '200', 'batch')
        File testCbl = new File(batchDir, 'test.cbl')
        testCbl.createNewFile()

        FileCollector fc = new FileCollector(working)
        List<File> ffiles = fc.getFiles()
        println ffiles
        def si = new SvnItem(buildInfo: bi, assocFile: ffiles[0], origin: psi)
        when:
        def uri = si.uri
        then:
        uri == 'batch/test.cbl'
    }

    def "branchUri on  on batch/test.cbl"() {
        given:
        File working = testDir.newFolder('source', '400')
        File batchDir = testDir.newFolder('source', '400', 'batch')
        File fi1 = new File(batchDir, 'test.cbl')
        fi1.createNewFile()

        FileCollector fc = new FileCollector(working)
        File assocFile = fc.getFiles()[0]
        def si = new SvnItem(buildInfo: bi, assocFile: assocFile, origin: psi)
        when:
        def uri = si.branchUri
        then:
        uri == 'SS01OTX/branches/SIGE1801'
    }

    def "asUriRelativeProj on  on batch/test.cbl"() {
        given:
        File working = testDir.newFolder('source', '500')
        File batchDir = testDir.newFolder('source', '500', 'batch')
        File testCbl = new File(batchDir, 'test.cbl')
        testCbl.createNewFile()
        FileCollector fc = new FileCollector(working)
        File assocFile = fc.getFiles()[0]
        def si = new SvnItem(buildInfo: bi, assocFile: assocFile, origin: psi)
        println si.assocFile.toPath().toString()
        when:
        def uri = si.uriRelativeProj
        then:
        uri == 'branches/SIGE1801/batch/test.cbl'
    }

}
