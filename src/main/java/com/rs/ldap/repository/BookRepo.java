package com.rs.ldap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rs.ldap.model.Book;

public interface BookRepo extends JpaRepository<Book, Integer>{
	List<Book> findByIsbn(String isbn);
}
