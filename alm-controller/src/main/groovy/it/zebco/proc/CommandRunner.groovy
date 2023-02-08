package it.zebco.proc

import java.nio.file.Path

interface CommandRunner {
    Integer execute(List<String> cmd)
    String executeCaptureOutput(List<String> cmd)
    CommandRunner withWorkingDir(Path workingDir)
    CommandRunner withEnv(Map<String, String> env)
}
