package it.zebco.alm.proc

trait ExecuteWrapper {

    Tuple2<Integer, String> execute(String cmdString, File workingDir = null) {
        Process proc
        StringBuffer output = new StringBuffer('')
        // execute(String[] envp, File dir)
        String[] envp = new ArrayList().toList()
        if (workingDir)
            proc = cmdString.execute(null, workingDir)
        else
            proc = cmdString.execute()
        //Blocking
        proc.in.eachLine {
            line -> output.append(line)
                           println line }
        proc.out.close()
        proc.waitFor()
        int exitValue = proc.exitValue()
        if (exitValue) {
            println "Complete with errors, exit value is ${exitValue}"
            println "ErrorStream ${proc.getErrorStream()}"
        }
        new Tuple2(exitValue, output)
    }
}