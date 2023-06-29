package com.example.libarySpringBoot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "book")
public class Book
{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "{person.book.title.notEmpty}")
	@Length(max = 100)
	@Column(name = "title")
	private String title;

	@NotEmpty(message = "{person.book.author.notEmpty}")
	@Column(name = "author")
	private String author;

	@Range(max = 2023, message = "{person.book.year.range}")
	@Column(name = "year")
	private int year;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(
			name = "person_book",
			joinColumns = @JoinColumn(name = "id_book"),
			inverseJoinColumns = @JoinColumn(name = "id_person")
	)
	private Person owner;

	public Book()
	{
	}

	public Book(String title, String author, int year)
	{
		this.title = title;
		this.author = author;
		this.year = year;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public Person getOwner()
	{
		return owner;
	}

	public void setOwner(Person owner)
	{
		this.owner = owner;
	}
}
