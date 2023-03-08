package com.techreturners.bookmanager.service;

import com.techreturners.bookmanager.exceptions.AlreadyExistsException;
import com.techreturners.bookmanager.exceptions.NotFoundException;
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
        when(mockBookManagerRepository.save(book)).thenReturn(book);
        bookManagerServiceImpl.deleteBookById(bookId);
        verify(mockBookManagerRepository, times(1)).deleteById(book.getId());
    }

    @Test
    void testBookThatAlreadyExists_ThrowsException() {
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);
        when(mockBookManagerRepository.isUniqueBook(book.getAuthor(), book.getTitle())).thenReturn(false);
        AlreadyExistsException thrown = Assertions.assertThrows(AlreadyExistsException.class, () ->
            bookManagerServiceImpl.insertBook(book));

        Assertions.assertEquals(String.format("title %s with author %s already exists", book.getTitle(), book.getAuthor()), thrown.getMessage());
        verify(mockBookManagerRepository, times(0)).save(book);
    }

    @Test
    void testFindingBookThatDoesntExist_throwsException() {
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () ->
                bookManagerServiceImpl.getBookById(34343L));

        Assertions.assertEquals("Book ID not found", thrown.getMessage());

    }
}
