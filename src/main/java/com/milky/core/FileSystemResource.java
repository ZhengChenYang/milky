package com.milky.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 */
public class FileSystemResource implements Resource{

    private File file;

    public FileSystemResource(File file) throws Exception {
        if(!file.isFile()){
            throw new Exception("object file is not a file!");
        }
        if(!file.exists()){
            throw new Exception("file is not exist!");
        }
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }
}
