package com.example.libarySpringBoot.repositories;

import com.example.libarySpringBoot.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer>
{
	boolean existsByTitleAndAuthor(String title, String author);

	Page<Book> findAllByTitleStartsWithIgnoreCase(String title, Pageable pageable);
}
