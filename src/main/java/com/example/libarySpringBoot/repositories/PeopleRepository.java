package com.example.libarySpringBoot.repositories;

import com.example.libarySpringBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer>
{
	boolean existsByFirstNameAndLastNameAndMiddleName(String firstName, String lastName, String middleName);
}
