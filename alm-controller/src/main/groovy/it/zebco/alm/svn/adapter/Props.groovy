package it.zebco.alm.svn.adapter

import groovy.util.logging.Slf4j
import it.zebco.proc.CommandRunner
import it.zebco.proc.SvnCmdBuilder

@Slf4j
class Props {
    CommandRunner cr;
    SvnCmdBuilder svnBlder

    // mimic svn command
    String propGet(String prop, String uri) {
        // should test for commandPath also ?
        List<String> cmd = svnBlder.getPropGet(prop, uri)
        log.info 'Executing: ' + cmd.join(' ')
        cr.executeCaptureOutput(cmd)
    }

    Map<String, String> getOrigin(String uri) {
        def props = ['almProjectName', 'almProjectStreamBuildPrefix', 'almProjectStreamBuildSuffix']
        // should test for commandPath also ?
        props.inject([:]) {
            result, prop ->
                result[prop] = this.propGet(prop, uri)
                result
        }
    }

}
