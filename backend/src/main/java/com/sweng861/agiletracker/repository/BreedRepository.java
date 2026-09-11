package com.sweng861.agiletracker.repository;

import com.sweng861.agiletracker.model.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, String> {
}
