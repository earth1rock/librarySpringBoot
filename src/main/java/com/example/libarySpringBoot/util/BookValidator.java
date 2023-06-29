package com.example.libarySpringBoot.util;

import com.example.libarySpringBoot.models.Book;
import com.example.libarySpringBoot.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator
{
	private final BooksService booksService;

	@Autowired
	public BookValidator(BooksService booksService)
	{
		this.booksService = booksService;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return Book.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		Book book = (Book) target;

		if (booksService.exists(book))
			errors.reject("book.exists", "This book is already exists");
	}
}
