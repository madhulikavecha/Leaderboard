package com.gloot.springbootcodetest.leaderboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handle(Exception e) {

        return new ResponseEntity<>("Invalid HTTP request !! Please provide valid data!! ", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> noHandlerFoundException(
            NoHandlerFoundException ex) {

        return new ResponseEntity<>("The URL you have reached is not in service at this time (404). ", HttpStatus.NOT_FOUND);

    }


}
