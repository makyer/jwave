package com.example.jwave;

import java.io.File;
import java.io.IOException;

public class SemgrepScanner extends AbstractScanner {

    public SemgrepScanner(FileManager fileManager) throws IOException {
        super("semgrep", fileManager);
        String[] semgrepCommand = {
                "semgrep",
                "--config=auto",
                "--junit-xml",
                "--include=*.java",
                "--dryrun",
                getFileManager().getServletPath()
        };
        runProcessScan(semgrepCommand);
    }

    public void runProcessScan(String[] processCommand) throws IOException {
        logStart();
        File outputFile = File.createTempFile(String.format("%sOutput", getProgramName()), ".xml");
        try {
            synchronized (MainServlet.processLock) {
                Process process = new ProcessBuilder(processCommand).redirectOutput(outputFile).start();
                process.waitFor();
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        logConclude();
        getFileManager().addFileToOutput(outputFile);
    }
}
