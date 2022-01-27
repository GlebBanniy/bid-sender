package com.example.bidsender.services;

import java.io.IOException;
import java.util.zip.ZipFile;

public class ZipFileFactory {

    public static ZipFile makeZipFile(String name) throws IOException {
        return new ZipFile(name);
    }
}
