package com.example.bidsender.controllers;

import com.example.bidsender.exceptions.JobProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class MyExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({JobProcessingException.class})
    public ResponseEntity<String> handleError(JobProcessingException ex) {
        log.error("An error occurred while executing the program");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
