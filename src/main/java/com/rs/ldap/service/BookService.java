package com.rs.ldap.service;

import java.util.Collection;

import com.rs.ldap.model.Book;

public interface BookService {
	Book saveBook(Book book);
	Book getBookById(int id);
	Book getBookByIsbn(String isbn);
	Collection<Book> getAllBooks();
}
