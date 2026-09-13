package com.sweng861.agiletracker.controller;

import com.sweng861.agiletracker.exception.ResourceNotFoundException;
import com.sweng861.agiletracker.model.Breed;
import com.sweng861.agiletracker.repository.BreedRepository;
import com.sweng861.agiletracker.service.DogApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DogController {

    private final BreedRepository breedRepository;
    private final DogApiService dogApiService;

    @Autowired
    public DogController(BreedRepository breedRepository, DogApiService dogApiService) {
        this.breedRepository = breedRepository;
        this.dogApiService = dogApiService;
    }

    // Fetch from Dog API
    @PostMapping("/sync")
    public ResponseEntity<String> syncBreeds() {
        dogApiService.fetchAndSaveBreeds();
        return ResponseEntity.ok("Breeds synced successfully");
    }

    // READ all
    @GetMapping("/breeds")
    public List<Breed> getAllBreeds() {
        return breedRepository.findAll();
    }

    // READ one
    @GetMapping("/breeds/{id}")
    public Breed getBreedById(@PathVariable String id) {
        return breedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Breed not found with id " + id));
    }

    // CREATE
    @PostMapping("/breeds")
    public Breed createBreed(@Valid @RequestBody Breed breed) {
        return breedRepository.save(breed);
    }

    // UPDATE
    @PutMapping("/breeds/{id}")
    public Breed updateBreed(@PathVariable String id, @Valid @RequestBody Breed breedDetails) {
        Breed existingBreed = breedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Breed not found with id " + id));

        existingBreed.setName(breedDetails.getName());
        existingBreed.setDescription(breedDetails.getDescription());
        existingBreed.setEnergy(breedDetails.getEnergy());

        return breedRepository.save(existingBreed);
    }

    // DELETE
    @DeleteMapping("/breeds/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable String id) {
        if (!breedRepository.existsById(id)) {
            throw new ResourceNotFoundException("Breed not found with id " + id);
        }
        breedRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}