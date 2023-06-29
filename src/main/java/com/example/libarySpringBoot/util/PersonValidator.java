package com.example.libarySpringBoot.util;

import com.example.libarySpringBoot.models.Person;
import com.example.libarySpringBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator
{
	private final PeopleService peopleService;

	@Autowired
	public PersonValidator(PeopleService peopleService)
	{
		this.peopleService = peopleService;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return Person.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		Person person = (Person) target;

		if (peopleService.exists(person))
			errors.reject("person.exists", "This person is already exists");

	}
}
