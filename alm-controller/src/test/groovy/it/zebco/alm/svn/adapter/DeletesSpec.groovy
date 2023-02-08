package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.BuildInfo
import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.SvnItem
import it.zebco.alm.source.FileCollector
import it.zebco.proc.CommandRunner
import it.zebco.proc.CommandRunnerOnProcBuilder
import it.zebco.proc.SvnmuccCmdBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/*
 * Update Branch allows updating on a branch within a pkg based project
 */

@Slf4j
class DeletesSpec extends Specification {

    @Rule final TemporaryFolder testDir = new TemporaryFolder(new File('src/test/temp'))
    Map<String,String> lprops = [:]
    BuildInfo bi
    SvnAccessInfo sai
    ByteArrayOutputStream executeCommandBuffer = new ByteArrayOutputStream()

    Deletes dels
    def row_origin
    def row_dest
    def psi_origin, psi_dest
    def fc

    def setup () {
        lprops.put('alm.levelRequest.vcrTag', 'B_SIGE1801_R02_SS01OTX-EVO-305_b2')
        lprops.put('alm.project.name', 'SS01OTX')
        lprops.put('alm.project.vcrName', 'SS01OTX')
        lprops.put('alm.project.vcrProjectName', 'SS01OTX')
        lprops.put('alm.projectStream.buildPrefix','SIGE1801' )
        lprops.put('alm.projectStream.buildSuffix','R02')
        lprops.put('alm.projectStream.type', 'B')
        lprops.put('alm.projectStream.vcrBranchId', 'SS01OTX/branches/SIGE1801')
        lprops.put('svn.repository.uri', 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA')
        lprops.put('svn.repository.trunk', 'Reference')
        lprops.put('svn.repository.tag', 'TagVersioni')
        lprops.put('svn.command.path', 'C:/Program Files/SlikSvn/bin')
        lprops.put('svn.commit.text', 'commit commit')
        lprops.put('svn.password', 'PA66TEST')
        lprops.put('svn.user', 'EY00018')
        lprops.put('source', 'D:/Workspace/IKAN-PHASE/alm-common/src/test/data/source/3002')
        sai = new SvnAccessInfo(lprops)
        bi = new BuildInfo(lprops)

        // origin
        row_origin = [project_NAME             : 'SS01OTX',
               projectStream_VCRBRANCHID: 'SS01OTX/branches/SIGE1801',
               projectStream_ISHEAD     : false,
               projectStream_BUILDPREFIX: 'correttiva',
               projectStream_BUILDSUFFIX: 'R01',
               svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
               projectStream_STATUS     : 5,
               svn_REPOSITORYLAYOUT     : '0',
               svn_TRUNKDIRECTORY       : 'Reference',
               project_VCRPROJECTNAME   : 'SS01OTX']
        psi_origin = new ProjectStreamInfo(row_origin)

        row_dest = [project_NAME        : 'SIGEOTX',
               projectStream_VCRBRANCHID: 'SIGEOTX/branches/SIGE1801',
               projectStream_ISHEAD     : false,
               projectStream_BUILDPREFIX: 'Release',
               projectStream_BUILDSUFFIX: 'R02',
               svn_REPOSITORYURL        : 'http://ws001sc1-00-alm.rmasede.grma.net:18088/svn/IKALM_REHOST_SIGEA',
               projectStream_STATUS     : 5,
               svn_REPOSITORYLAYOUT     : '0',
               svn_TRUNKDIRECTORY       : 'Reference',
               project_VCRPROJECTNAME   : 'SIGEOTX']
        psi_dest = new ProjectStreamInfo(row_dest)

        // user, password, rootUrl, svn binaries path used to set muccExe
        SvnmuccCmdBuilder muccBlder = new SvnmuccCmdBuilder(user: 'user', password: 'password')
                //.withBinPath(commandPath)
        // svn binaries path used to set svnExe
        //SvnCmdBuilder svnCmdBuilder = new SvnCmdBuilder()
        // .withBinPath(commandPath)
        // workingDir and env vars (Map<String, String>)
        CommandRunner cr = new CommandRunnerOnProcBuilder() {
            @Override
            Integer execute(List<String> command) {
                executeCommandBuffer << command.join(' ')
                0
            }
        }

        dels = new Deletes(cr: cr, muccBlder: muccBlder)
    }

    def "deletes empty list"() {
        when:
        def res = dels.delete('http://host/repo/proj/branch/correttiva', [], "commit")
        then:
        thrown(AssertionError)
    }

    def "deletes doesn't found files"() {
        given:
        File working = testDir.newFolder('source')
        File di1 = testDir.newFolder('source', 'batch')
        fc = new FileCollector(working)
        List<File> lf = fc.getFiles()
        List<SvnItem> lsi = []
        lf.each {
            lsi << new SvnItem(buildInfo: bi, assocFile: it)
        }

        when:
        def res = dels.delete('http://host/repo/proj/branches/correttiva', lsi, "commit")
        then:
        thrown(AssertionError)
    }

    def "deletes branch found files"() {
        given:
        File working = testDir.newFolder('source')
        File di1 = testDir.newFolder('source', 'batch')
        File fi1 = new File(di1, 'test.cbl')
        fi1.createNewFile()
        fc = new FileCollector(working)
        List<File> lf = fc.getFiles()
        List<SvnItem> lsi = []
        lf.each {
            lsi << new SvnItem(buildInfo: bi, assocFile: it)
        }
        when:
        def res = dels.delete('http://host/repo/proj/branches/correttiva', lsi, "commit")
        then:
        executeCommandBuffer.toString() == 'svnmucc -U http://host/repo/proj/branches/correttiva rm batch/test.cbl -u user -p password --no-auth-cache -m "commit"'
        //note file batch/test.cbl and batch\test.cbl both are equally accepted
    }

    def "deletes branch found several files"() {
        given:
        File working = testDir.newFolder('source')
        File di1 = testDir.newFolder('source', 'batch')
        File fi1 = new File(di1, 'test.cbl')
        fi1.createNewFile()
        File di2 = testDir.newFolder('source', 'cics')
        File fi2 = new File(di2, 'test.cbl')
        fi2.createNewFile()
        fc = new FileCollector(working)
        List<File> lf = fc.getFiles()
        List<SvnItem> lsi = []
        lf.each {
            lsi << new SvnItem(buildInfo: bi, assocFile: it)
        }

        when:
        def res = dels.delete('http://host/repo/proj/branches/correttiva', lsi, "commit")
        then:
        executeCommandBuffer.toString() == 'svnmucc -U http://host/repo/proj/branches/correttiva rm batch/test.cbl rm cics/test.cbl -u user -p password --no-auth-cache -m "commit"'
        //        //note file batch/test.cbl and batch\test.cbl both are equally accepted
    }

    def "deletes branch with -f option"() {
        given:
        List<SvnItem> lsi = []
        File fi = null
        (0..100).each {
            fi = new File('batch', "test${it}.cbl")
            lsi << new SvnItem(buildInfo: bi, assocFile: fi)
        }

        when:
        def res = dels.delete('http://host/repo/proj/branches/correttiva', lsi, "commit")
        String generatedCommand = executeCommandBuffer.toString()
        def filePathMatcher = generatedCommand =~ /-f (.*(temp.*\.tmp))/
        File tmpFile = new File(filePathMatcher[0][1])
        List<String> fileContent = tmpFile.readLines()
        then:
        generatedCommand.take(55) == 'svnmucc -U http://host/repo/proj/branches/correttiva -f'
        fileContent[0..1] == ['rm', 'batch/test0.cbl']
        fileContent[200..201] == ['rm','batch/test100.cbl']
        generatedCommand.drop(generatedCommand.size() - 47) == '-u user -p password --no-auth-cache -m "commit"'
        //note file batch/test.cbl and batch\test.cbl both are equally accepted
    }
}
