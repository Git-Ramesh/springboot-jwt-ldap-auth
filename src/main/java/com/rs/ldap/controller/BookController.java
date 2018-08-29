package com.rs.ldap.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rs.ldap.model.Book;
import com.rs.ldap.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookService bookService;

	@PostMapping("/add")
	public Book addBook(@RequestBody Book book) {
		return bookService.saveBook(book);
	}

	@GetMapping("/{id}")
	public Book getBook(@PathVariable("id") int id) {
		return bookService.getBookById(id);
	}

	@GetMapping("/{isbn}")
	public Book getBook(@PathVariable("isbn") String isbn) {
		return bookService.getBookByIsbn(isbn);
	}

	@GetMapping("/all")
	public Collection<Book> getAllBooks() {
		return bookService.getAllBooks();
	}
}
