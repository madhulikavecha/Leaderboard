package com.gloot.springbootcodetest.leaderboard.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception e) {

       // logger.error("error occurred {}", e);
        return new ResponseEntity<>("Something happened: " , HttpStatus.INTERNAL_SERVER_ERROR);

   }


}
