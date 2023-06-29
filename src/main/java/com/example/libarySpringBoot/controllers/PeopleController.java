package com.example.libarySpringBoot.controllers;

import com.example.libarySpringBoot.models.Person;
import com.example.libarySpringBoot.services.PeopleService;
import com.example.libarySpringBoot.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/people")
public class PeopleController
{

	private final PeopleService peopleService;

	private final PersonValidator personValidator;

	@Autowired
	public PeopleController(PeopleService peopleService, PersonValidator personValidator)
	{
		this.peopleService = peopleService;
		this.personValidator = personValidator;
	}

	// Return NULL value from input in HTML-form instead of empty string ("") by default
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		StringTrimmerEditor editor = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, editor);
	}

	@GetMapping()
	public String people(@RequestParam(name = "page", defaultValue = "1") int page,
						 @RequestParam(name = "people_per_page", defaultValue = "10") int peoplePerPage,
						 Model model)
	{
		Page<Person> personPage = peopleService.findAll(page - 1, peoplePerPage);
		int totalPages = personPage.getTotalPages();
		if (totalPages > 1)
		{
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("people", personPage);
		return "/people/people";
	}

	@GetMapping("/person/{id}")
	public String personInfo(@PathVariable("id") int personId, Model model)
	{
		Person person = peopleService.findOneByIdWithBooks(personId);
		if (person == null)
		{
			model.addAttribute("personId", personId);
			return "/people/noPerson";
		}

		model.addAttribute("person", person);
		return "/people/person";
	}

	@GetMapping("/new")
	public String newPerson(@ModelAttribute("person") Person person)
	{
		return "/people/new";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
	{
		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/people/new";

		peopleService.save(person);
		return "redirect:/people";
	}

	// TODO probably need to change to GetMapping
	//  Now PostMapping used to trim question mark in url
	@PostMapping("/person/{id}/edit")
	public String editPersonPage(@PathVariable("id") int personId, Model model)
	{
		model.addAttribute("person", peopleService.findOneById(personId));
		return "/people/edit";
	}

	@PatchMapping("/person/{id}/edit")
	public String editPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
	{
		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/people/edit";

		peopleService.update(person);
		return "redirect:/people/person/{id}";
	}

	@DeleteMapping("/person/{id}/delete")
	public String deletePerson(@PathVariable("id") int personId)
	{
		peopleService.delete(personId);
		return "redirect:/people";
	}
}
