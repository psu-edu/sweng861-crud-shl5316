package com.sweng861.agiletracker.controller;

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

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private DogApiService dogApiService;

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
    public ResponseEntity<Breed> getBreedById(@PathVariable String id) {
        return breedRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping("/breeds")
    public Breed createBreed(@Valid @RequestBody Breed breed) {
        return breedRepository.save(breed);
    }

    // UPDATE
    @PutMapping("/breeds/{id}")
    public ResponseEntity<Breed> updateBreed(@PathVariable String id, @Valid @RequestBody Breed breedDetails) {
        return breedRepository.findById(id)
            .map(breed -> {
                breed.setName(breedDetails.getName());
                breed.setDescription(breedDetails.getDescription());
                breed.setEnergy(breedDetails.getEnergy());
                return breedRepository.save(breed);
            })
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/breeds/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable String id) {
        breedRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
