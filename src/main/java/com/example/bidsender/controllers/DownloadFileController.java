package com.example.bidsender.controllers;

import com.example.bidsender.services.DownloadServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@Api(value = "Upload controller", description = "Controller for upload zip files")
public class DownloadFileController {

    private final DownloadServiceImpl service;

    @Autowired
    public DownloadFileController(DownloadServiceImpl service) {
        this.service = service;
    }

    @ApiOperation(value = "Greeting page")
    @GetMapping("/uploader")
    public @ResponseBody String provideUploadInfo(){
        return "Вы можете загружать файл с использованием этого URL.";
    }

    @ApiOperation(value = "Uploading zip file", position = 1)
    @PostMapping("/uploader")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".zip")) {
            if (service.readFile(file)) { return new ResponseEntity<>("File is upload", HttpStatus.OK); }
            else { return new ResponseEntity<>("File is not upload", HttpStatus.INTERNAL_SERVER_ERROR); }
        }
        else { return new ResponseEntity<>("File is not ZIP", HttpStatus.FORBIDDEN); }
    }
}   
