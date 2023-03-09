package com.techreturners.bookmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(BookNotFoundException e ) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "404");
        response.put("message", e.getLocalizedMessage());
        return response;
    }


    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExistsException(BookAlreadyExistsException e) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "409");
        response.put("message", e.getLocalizedMessage());
        return response;
    }

}

