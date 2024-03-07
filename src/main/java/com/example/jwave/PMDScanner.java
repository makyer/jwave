package com.example.jwave;

import java.io.IOException;

public class PMDScanner extends ClassloaderScanner {

    public PMDScanner(FileManager fileManager) throws IOException {
        super("pmd", fileManager);
        runFromClassLoader(getProgramName());
    }
}
