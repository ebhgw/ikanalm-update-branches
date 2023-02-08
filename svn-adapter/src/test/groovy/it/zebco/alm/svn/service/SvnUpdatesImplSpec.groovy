package it.zebco.alm.svn.service

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Paths

class SvnUpdatesImplSpec extends Specification {
    @TempDir File tempDir
    FileTreeBuilder treeBuilder

    def setup () {
        treeBuilder = new FileTreeBuilder(tempDir)
    }

    def "test file collect one file" () {
        given:
        //final FileTreeBuilder treeBuilder = new FileTreeBuilder(tempDir)
        treeBuilder.dir('src') {
            file('note.txt') {
                write '''\
                    This is a text note
               '''.stripIndent()
            }
        }
        SvnUpdatesImpl upd = new SvnUpdatesImpl()
        String tempDirOnSlash = tempDir.toString().replaceAll('\\\\', '/')
        when:
        upd.collectUpdates(tempDir)
        def relDirs = upd.getDirectories().sort()
        def relFiles = upd.getFiles().collect{it.get(1).toString()}
        def fulls = upd.getFiles().collect{it.get(0).toString().replaceAll('\\\\', '/')}
        then:
        fulls == [tempDirOnSlash + '/src/note.txt']
        relDirs == ['src']
        relFiles == ['src/note.txt']
    }

    def "test file collect two files" () {
        given:
        //final FileTreeBuilder treeBuilder = new FileTreeBuilder(tempDir)
        treeBuilder.dir('src') {
            file('note.txt') {
                write '''\
                    This is a text note
               '''.stripIndent()
            }
            dir('main') {
                dir('java') {
                    file('zulu.java') {
                        write '''\
                              public class zulu {}
                              '''.stripIndent()
                    }
                }
            }
        }
        SvnUpdatesImpl upd = new SvnUpdatesImpl()
        when:
        upd.collectUpdates(tempDir)
        def res = upd.getFiles().collect{it.get(1).toString()}.sort()
        then:
        res == ['src/main/java/zulu.java', 'src/note.txt'].sort()
    }

    def "test file collect zero files" () {
        given:
        //final FileTreeBuilder treeBuilder = new FileTreeBuilder(tempDir)
        treeBuilder.dir('src') {
            dir('main') {
                dir('java')
            }
        }
        SvnUpdatesImpl upd = new SvnUpdatesImpl()
        when:
        upd.collectUpdates(tempDir)
        def res = upd.upds
        then:
        res == []
    }
}
