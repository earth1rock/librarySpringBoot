package com.example.libarySpringBoot.controllers;

import com.example.libarySpringBoot.models.Book;
import com.example.libarySpringBoot.models.Person;
import com.example.libarySpringBoot.services.BooksService;
import com.example.libarySpringBoot.services.PeopleService;
import com.example.libarySpringBoot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/books")
public class BookController
{

	private final BooksService booksService;
	private final PeopleService peopleService;

	private final BookValidator bookValidator;

	@Autowired
	public BookController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator)
	{
		this.booksService = booksService;
		this.peopleService = peopleService;
		this.bookValidator = bookValidator;
	}

	@GetMapping()
	public String books(@RequestParam(name = "page", defaultValue = "1") int page,
						@RequestParam(name = "books_per_page", defaultValue = "10") int booksPerPage,
						@RequestParam(name = "sort_by_year", defaultValue = "false") boolean sortByYear,
						Model model)
	{
		Page<Book> bookPage = booksService.findAll(page - 1, booksPerPage, sortByYear);

		int totalPages = bookPage.getTotalPages();
		if (totalPages > 1)
		{
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("books", bookPage);
		return "/books/books";
	}

	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book)
	{
		return "/books/new";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult)
	{
		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors())
			return "/books/new";

		booksService.save(book);
		return "redirect:/books";
	}

	 /*
	 	Need to have different names to escape overlapping 'id' field in PathVariable and ModelAttribute
	 Otherwise, the order of owner selection will be caused by Book.id
	 So @PathVariable("id") → @PathVariable("bookId") and @GetMapping("/book/{id}") → @GetMapping("/book/{bookId}")
	 https://github.com/spring-projects/spring-framework/issues/13987
	 	The second way to fix this is to add 'model.addAttribute("person", new Person())" directly, but that looks like a bad way
	 	Probably, the best way to fix it is a change '@ModelAttribute("person")' to '@ModelAttribute(name = "person", binding = false)'
	 to disable data binding
	 */
	@GetMapping("/book/{id}")
	public String bookInfo(@PathVariable("id") int bookId, @ModelAttribute(name = "person", binding = false) Person person, Model model)
	{
		Book book = booksService.findOneByIdWithOwner(bookId);
		if (book == null)
		{
			model.addAttribute("bookId", bookId);
			return "/books/noBook";
		}

		model.addAttribute("book", book);
		if (book.getOwner() == null)
			model.addAttribute("people", peopleService.findAll());

		return "/books/book";
	}

	@GetMapping("/search")
	public String bookSearch(@RequestParam(name = "title", required = false) String title, Model model)
	{
		if (title == null)
			return "/books/search";

		if (title.isBlank())
			model.addAttribute("isTitleBlank", true);
		else
		{
			model.addAttribute("title", title);
			model.addAttribute("books", booksService.findByTitleStartsWithIgnoreCase(title));
		}
		return "/books/search";
	}

	@PostMapping("/book/{id}/edit")
	public String editBookPage(@PathVariable("id") int bookId, Model model)
	{
		model.addAttribute("book", booksService.findOneById(bookId));
		return "/books/edit";
	}

	@PatchMapping("/book/{id}/edit")
	public String editBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult)
	{
		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors())
			return "/books/edit";

		booksService.update(book);
		return "redirect:/books/book/{id}";
	}

	@PostMapping("/book/{id}/setOwner")
	public String setOwner(@PathVariable("id") int bookId, @ModelAttribute("person") Person owner)
	{
		booksService.setOwner(bookId, owner);
		return "redirect:/books/book/{id}";
	}

	@PostMapping("/book/{id}/releaseOwner")
	public String releaseOwner(@PathVariable("id") int bookId)
	{
		booksService.releaseOwner(bookId);
		return "redirect:/books/book/{id}";
	}

	@DeleteMapping("/book/{id}/delete")
	public String deleteBook(@PathVariable("id") int bookId)
	{
		booksService.delete(bookId);
		return "redirect:/books";
	}
}
