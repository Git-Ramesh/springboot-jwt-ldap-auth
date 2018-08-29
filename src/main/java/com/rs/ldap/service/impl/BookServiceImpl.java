package com.rs.ldap.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.rs.ldap.model.Book;
import com.rs.ldap.repository.BookRepo;
import com.rs.ldap.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	private static final Logger LOGGER =LoggerFactory.getLogger(BookServiceImpl.class);
	
	@Autowired
	private BookRepo bookRepo;
	
	@Override
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Book saveBook(Book book) {
		LOGGER.debug("Saving the book..");
		return bookRepo.save(book);
	}

	@Override
	@PreAuthorize("hasAnyAuthority(['ROLE_ADMIN', 'ROLE_USER'])")
	public Book getBookById(int id) {
		LOGGER.debug("Retreving the book has id {} ", id);
		Optional<Book> optional = bookRepo.findById(id);
		if(optional.isPresent()) {
			LOGGER.debug("Book found!");
			return optional.get();
		}
		else {
			LOGGER.debug("No book found!");
			return null;
		}
	}

	@Override
	public Book getBookByIsbn(String isbn) {
		LOGGER.debug("Retreving the book has isbn: {}", isbn);
		return bookRepo.findByIsbn(isbn).get(0);
	}

	@Override
	public Collection<Book> getAllBooks() {
		LOGGER.debug("Retreving all books");
		return bookRepo.findAll();
	}

}
