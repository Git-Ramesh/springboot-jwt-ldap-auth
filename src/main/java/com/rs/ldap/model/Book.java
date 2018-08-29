package com.rs.ldap.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOK")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable{
	private static final long serialVersionUID = -4611082701434463450L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int id;
	private String isbn;
	private String title;
	private String author;
	private float price;
	private String publisher;
	
	public Book(String isbn, String title, String author, float price, String publisher) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.price = price;
		this.publisher = publisher;
	}
}
