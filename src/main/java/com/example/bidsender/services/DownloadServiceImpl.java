package com.example.bidsender.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DownloadServiceImpl{

    public boolean readFile(MultipartFile file) {
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            String name;
            long size;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                size = entry.getSize();
                System.out.println("File name: " + name + ", " + size);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
