package com.techreturners.bookmanager.service;

import com.techreturners.bookmanager.exceptions.BookAlreadyExistsException;
import com.techreturners.bookmanager.exceptions.BookNotFoundException;
import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.model.Genre;

import com.techreturners.bookmanager.repository.BookManagerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
public class BookManagerServiceTests {

    @Mock
    private BookManagerRepository mockBookManagerRepository;

    @InjectMocks
    private BookManagerServiceImpl bookManagerServiceImpl;

    @Test
    public void testGetAllBooksReturnsListOfBooks() {

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book One", "This is the description for Book One", "Person One", Genre.Education));
        books.add(new Book(2L, "Book Two", "This is the description for Book Two", "Person Two", Genre.Education));
        books.add(new Book(3L, "Book Three", "This is the description for Book Three", "Person Three", Genre.Education));

        when(mockBookManagerRepository.findAll()).thenReturn(books);

        List<Book> actualResult = bookManagerServiceImpl.getAllBooks();

        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(books);
    }

    @Test
    public void testAddABook() {

        var book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerRepository.save(book)).thenReturn(book);
        when(mockBookManagerRepository.isUniqueBook(book.getAuthor(), book.getTitle())).thenReturn(true);

        Book actualResult = bookManagerServiceImpl.insertBook(book);

        assertThat(actualResult).isEqualTo(book);
    }

    @Test
    public void testGetBookById() {

        Long bookId = 5L;
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);

        when(mockBookManagerRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book actualResult = bookManagerServiceImpl.getBookById(bookId);

        assertThat(actualResult).isEqualTo(book);
    }

    //User Story 4 - Update Book By Id Solution
    @Test
    public void testUpdateBookById() {

        Long bookId = 5L;
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);

        when(mockBookManagerRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(mockBookManagerRepository.save(book)).thenReturn(book);

        bookManagerServiceImpl.updateBookById(bookId, book);

        verify(mockBookManagerRepository, times(1)).save(book);
    }

    @Test
    void testDeleteABook() {
        Long bookId = 5L;
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);
        when(mockBookManagerRepository.existsById(book.getId())).thenReturn(true);
        bookManagerServiceImpl.deleteBookById(bookId);
        verify(mockBookManagerRepository, times(1)).deleteById(book.getId());
    }

    @Test
    void testBookThatAlreadyExists_ThrowsException() {
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);
        when(mockBookManagerRepository.isUniqueBook(book.getAuthor(), book.getTitle())).thenReturn(false);
        BookAlreadyExistsException thrown = Assertions.assertThrows(BookAlreadyExistsException.class, () ->
            bookManagerServiceImpl.insertBook(book));

        Assertions.assertEquals(String.format("title %s with author %s already exists", book.getTitle(), book.getAuthor()), thrown.getMessage());
        verify(mockBookManagerRepository, times(0)).save(book);
    }

    @Test
    void testFindingBookThatDoesntExist_throwsException() {
        BookNotFoundException thrown = Assertions.assertThrows(BookNotFoundException.class, () ->
                bookManagerServiceImpl.getBookById(34343L));

        Assertions.assertEquals("Book ID not found", thrown.getMessage());

    }

    @Test
    void testUpdatingBookThatDoesntExist_throwsException() {
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);
        BookNotFoundException thrown = Assertions.assertThrows(BookNotFoundException.class, () ->
                bookManagerServiceImpl.updateBookById(book.getId(), book));

        Assertions.assertEquals("Book ID not found", thrown.getMessage());
    }

    @Test
    void testDeletingBookThatDoesntExist_throwsException() {
        BookNotFoundException thrown = Assertions.assertThrows(BookNotFoundException.class, () ->
                bookManagerServiceImpl.deleteBookById(34343434L));

        Assertions.assertEquals("Book with ID 34343434 is not found", thrown.getMessage());
    }
}
