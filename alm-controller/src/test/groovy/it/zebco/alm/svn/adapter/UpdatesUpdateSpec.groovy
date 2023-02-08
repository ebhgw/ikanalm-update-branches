package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.alm.model.BuildInfo
import it.zebco.alm.model.ProjectStreamInfo
import it.zebco.alm.model.SvnItem
import it.zebco.alm.source.FileCollector
import it.zebco.proc.CommandRunner
import it.zebco.proc.CommandRunnerOnProcBuilder
import it.zebco.proc.SvnCmdBuilder
import it.zebco.proc.SvnmuccCmdBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Specification

/*
 * Update function from Updates class
 *
 * Resulting cmd string are printed to stdout instead of being executed
 * See CommandRunnerOnProcBuilder with @Override and executeCommandBuffer
 */

@Slf4j
class UpdatesUpdateSpec extends Specification {

    @Rule final TemporaryFolder testDir = new TemporaryFolder(new File('src/test/temp'))
    Map<String,String> lprops = [:]
    BuildInfo bi
    SvnAccessInfo sai

    Updates upds
    def row_origin
    def row_dest
    def psi_origin, psi_dest
    def fc
    // Overridden CommandRunnerOnProcBuilder.execute will write to executeCommandBuffer
    ByteArrayOutputStream executeCommandBuffer = new ByteArrayOutputStream()

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
        SvnmuccCmdBuilder muccFctry = new SvnmuccCmdBuilder(user: 'user', password: 'password')
                .withRootUrl('rootUrl')
        // svn binaries path used to set svnExe
        SvnCmdBuilder svnFctry = new SvnCmdBuilder()
        // workingDir and env vars (Map<String, String>)
        CommandRunner cr = new CommandRunnerOnProcBuilder() {
            @Override
            Integer execute(List<String> command) {
                print "Command: " + command.join(' ')
                0
            }
        }

        upds = new Updates(cr: cr, muccBlder: muccFctry)
    }

    //@Ignore
    def "update empty list"() {
        when:
        def res = upds.update('destUrl', [], "commit")
        then:
        thrown(AssertionError)
    }

    //@Ignore
    // la put prende sia il nome url-encoded A%C2%A3BCDEF.jmodel sia quello CP1252 A£BCDEF.jmodel
    // considerandoli il medesimo file
    def "update with list"() {
        given:
        File working = testDir.newFolder('source')
        File t1 = new File(working, 'A£BCDEF.jmodel')
        t1.createNewFile()
        File t2 = new File(working, 'B£BCDEF.jmodel')
        t2.createNewFile()
        fc = new FileCollector(working)
        List<File> lf = fc.getFiles()
        List<SvnItem> lsi = []
        lf.each {
            lsi << new SvnItem(buildInfo: bi, assocFile: it)
        }

        when:
        ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        // do not execute, writes command to stdout redirected to buffer
        def res = upds.update('destUrl', lsi, "commit")
        then:
        buffer.toString() == 'Command: svnmucc -U destUrl put A£BCDEF.jmodel A%C2%A3BCDEF.jmodel put B£BCDEF.jmodel B%C2%A3BCDEF.jmodel -u user -p password --no-auth-cache -m "commit"'
    }
}
