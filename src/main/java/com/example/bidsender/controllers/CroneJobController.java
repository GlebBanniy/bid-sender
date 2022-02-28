package com.example.bidsender.controllers;

import com.example.bidsender.exceptions.JobProcessingException;
import com.example.bidsender.services.CroneJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Api(value = "Controller for crone job service", description = "For manual starting crone job")
public class CroneJobController {

    private final CroneJobService service;

    @Autowired
    public CroneJobController(CroneJobService service) {
        this.service = service;
    }

    @ApiOperation(value = "Forced manual start crone job")
    @PostMapping("/crone-job-starter")
    public ResponseEntity<String> doCroneJob(@RequestParam("name") String fileName)
            throws JobProcessingException {
        service.startJob(fileName);
        return new ResponseEntity<>("Process file completed", HttpStatus.OK);
    }
}
