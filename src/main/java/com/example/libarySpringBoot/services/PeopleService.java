package com.example.libarySpringBoot.services;

import com.example.libarySpringBoot.models.Person;
import com.example.libarySpringBoot.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PeopleService
{
	private final PeopleRepository peopleRepository;

	@Autowired
	public PeopleService(PeopleRepository peopleRepository)
	{
		this.peopleRepository = peopleRepository;
	}


	//TODO fix it via use search (SORT)
	public List<Person> findAll()
	{
		return peopleRepository.findAll();
	}

	public Page<Person> findAll(int page, int personPerPage)
	{
		return peopleRepository.findAll(PageRequest.of(page, personPerPage));
	}

	public Person findOneById(int id)
	{
		return peopleRepository.findById(id).orElse(null);
	}

	public Person findOneByIdWithBooks(int id)
	{
		Optional<Person> personWithBooks = peopleRepository.findById(id);
		if (personWithBooks.isEmpty())
			return null;

		Hibernate.initialize(personWithBooks.get().getBooks());
		return personWithBooks.get();
	}

	public boolean exists(Person person)
	{
		return peopleRepository.existsByFirstNameAndLastNameAndMiddleName(
				person.getFirstName(),
				person.getLastName(),
				person.getMiddleName()
		);
	}

	@Transactional
	public void save(Person person)
	{
		peopleRepository.save(person);
	}

	@Transactional
	public void update(Person updatedPerson)
	{
		peopleRepository.save(updatedPerson);
	}

	@Transactional
	public void delete(int id)
	{
		peopleRepository.deleteById(id);
	}
}
