package com.techreturners.bookmanager.service;

import com.techreturners.bookmanager.exceptions.BookAlreadyExistsException;
import com.techreturners.bookmanager.exceptions.BookNotFoundException;
import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.repository.BookManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookManagerServiceImpl implements BookManagerService {

    @Autowired
    BookManagerRepository bookManagerRepository;

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookManagerRepository.findAll().forEach(books::add);
        return books;
    }

    @Override
    public Book insertBook(Book book) {
        if (!bookManagerRepository.isUniqueBook(book.getAuthor(), book.getTitle())) {
            throw new BookAlreadyExistsException(String.format("title %s with author %s already exists", book.getTitle(), book.getAuthor()));
        }
        return bookManagerRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookManagerRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    //User Story 4 - Update Book By Id Solution
    @Override
    public void updateBookById(Long id, Book book) {
        Book retrievedBook = bookManagerRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        retrievedBook.setTitle(book.getTitle());
        retrievedBook.setDescription(book.getDescription());
        retrievedBook.setAuthor(book.getAuthor());
        retrievedBook.setGenre(book.getGenre());

        bookManagerRepository.save(retrievedBook);
    }


    @Override
    public void deleteBookById(Long id) {
        if (!bookManagerRepository.existsById(id))  {
            throw new BookNotFoundException(id);
        }
        bookManagerRepository.deleteById(id);
    }


}
