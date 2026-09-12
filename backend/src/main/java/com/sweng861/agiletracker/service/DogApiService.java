package com.sweng861.agiletracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweng861.agiletracker.model.Breed;
import com.sweng861.agiletracker.repository.BreedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DogApiService {

    private static final String DOG_API_URL = "https://dogapi.dog/api/v2/breeds";
    private static final int MAX_DOG_PAGE_SIZE = 5;

    private final RestTemplate restTemplate;
    private final BreedRepository breedRepository;

    @Autowired
    public DogApiService(RestTemplate restTemplate, BreedRepository breedRepository) {
        this.restTemplate = restTemplate;
        this.breedRepository = breedRepository;
    }

    public void fetchAndSaveBreeds() {
        try {
            String url = DOG_API_URL + "?page[size]=" + MAX_DOG_PAGE_SIZE;
            String response = restTemplate.getForObject(url, String.class);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode breedArray = mapper.readTree(response).get("data");

            for (JsonNode breedNode : breedArray) {
                Breed breed = parseBreedData(breedNode);
                breedRepository.save(breed);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching Dog API data", e);
        }
    }

    private Breed parseBreedData(JsonNode node) {
        Breed breed = new Breed();
        breed.setId(node.get("id").asText());
        
        JsonNode attributes = node.get("attributes");
        breed.setName(attributes.get("name").asText());
        breed.setDescription(attributes.get("description").asText());
        breed.setEnergy(attributes.get("energy").asInt());

        return breed;
    }
}
