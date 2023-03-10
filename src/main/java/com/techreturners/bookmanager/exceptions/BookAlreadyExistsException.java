package com.techreturners.bookmanager.exceptions;

public class BookAlreadyExistsException extends RuntimeException{

    private static final String ERROR_MESSAGE = "Book with title %s and author %s already exists";
    public BookAlreadyExistsException(String bookTitle, String bookAuthor) {
        super (String.format(ERROR_MESSAGE, bookTitle, bookAuthor)); }


}
