package it.zebco.alm.svn

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

class SvnPutWrapperSpec extends Specification {
    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder()
    @Shared File myProps

    def setup() {
        String svnmuccCmdPath = '"C:/Program Files/SlikSvn/bin/svnmucc.exe"'
        def wrapper = new SvnPutWrapper(svnmuccCmdPath)
        SvnPutWrapper.metaclass.run = {
            new Tuple2(0, '')
        }
    }

    def "Test svn put without actually putting" () {
        given:
        Map<String, String> info = new HashMap<String, String>()
        info.put('baseUrl', 'http://localhost:11180/svn/PASS01')
        info.put('user', 'ey00018')
        info.put('password', 'IknAlm18')
        File ff = testProjectDir.newFile('test.txt')
        ff << "Added on " + new Date().toString()

//wrapper.mkDirs(info, dirs)
        when:
        Tuple2<Integer, String> res = wrapper.put(info, ff, 'branches/correttiva/pkg-corr-00.properties')
        then:
        res.first == 0
        res.second == ''
    }
}
