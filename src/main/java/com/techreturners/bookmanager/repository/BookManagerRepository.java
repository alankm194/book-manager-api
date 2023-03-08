package com.techreturners.bookmanager.repository;

import com.techreturners.bookmanager.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookManagerRepository extends CrudRepository<Book, Long> {
    @Query("SELECT CASE WHEN COUNT(a) = 0 THEN true ELSE false END FROM Book a WHERE a.author = :author AND a.title = :title")
    boolean isUniqueBook(@Param("author") String author, @Param("title") String title);

}

