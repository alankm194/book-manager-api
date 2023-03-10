package com.techreturners.bookmanager.exceptions;

public class BookNotFoundException extends RuntimeException{

    private static final String ERROR_MESSAGE = "Book with ID %s is not found";

    public BookNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
