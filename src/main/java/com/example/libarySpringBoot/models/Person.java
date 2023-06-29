package com.example.libarySpringBoot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "person")
public class Person
{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull(message = "{person.firstName.notEmpty}")
	@Length(min = 2, max = 40, message = "{person.firstName.range}")
	@Column(name = "first_name")
	private String firstName;

	@NotNull(message = "{person.lastName.notEmpty}")
	@Length(min = 2, max = 40, message = "{person.lastName.range}")
	@Column(name = "last_name")
	private String lastName;

	@Nullable
	@Length(min = 2, max = 40, message = "{person.middleName.range}")
	@Column(name = "middle_name")
	private String middleName;

	@NotNull(message = "{person.yearOfBirth.notEmpty}")
	@Range(min = 1900, max = 2023, message = "{person.yearOfBirth.range}")
	@Column(name = "year_of_birth")
	private Integer yearOfBirth;

	@OneToMany(mappedBy = "owner")
	private final List<Book> books = new ArrayList<>();

	public Person()
	{
	}

	public Person(String firstName, String lastName, @Nullable String middleName, Integer yearOfBirth)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.yearOfBirth = yearOfBirth;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@Nullable
	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(@Nullable String middleName)
	{
		this.middleName = middleName;
	}

	public Integer getYearOfBirth()
	{
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth)
	{
		this.yearOfBirth = yearOfBirth;
	}

	public String getFullName()
	{
		return String.format("%s %s %s", firstName, middleName, lastName);
	}

	public boolean hasBooks()
	{
		return !books.isEmpty();
	}

	public void addBook(Book book)
	{
		books.add(book);
	}

	public List<Book> getBooks()
	{
		return books;
	}

	public void addBooks(List<Book> bookList)
	{
		books.addAll(bookList);
	}
}
