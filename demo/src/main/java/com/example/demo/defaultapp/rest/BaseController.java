package com.example.demo.defaultapp.rest;

import com.example.demo.defaultapp.model.Animal;
import com.example.demo.defaultapp.model.Feline;
import com.example.demo.defaultapp.services.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {
    private final AnimalService animalService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BaseController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public String baseResponse(){return "Hello from base page! This page is available to anyone!";}

    @RequestMapping(value = "/read")
    @PreAuthorize("hasAuthority('READ_ANIMAL')")
    public String tryRead() {
        return "Hello authorized user! This page is available only for users with READ_ANIMAL access. For now it is user and any other oauth2 based user!";
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAuthority('DELETE_ANIMAL')")
    public String sayHelloAdmin() { return "Hello to admin user!"; }

    @GetMapping(value = "/animal/{id}")
    @PreAuthorize("hasAuthority('READ_ANIMAL')")
    public Animal getAnimalById(@PathVariable int id) {
        return animalService.findOne(id);
    }

    @SneakyThrows
    @PostMapping(value = "/createFeline")
    @PreAuthorize("hasAuthority('CREATE_ANIMAL')")
    public String createFeline(@RequestBody Feline animal) {
        animalService.persist(animal);
        return objectMapper.writeValueAsString(animal);
    }

    @DeleteMapping(value = "/deleteAnimal/{id}")
    public void deleteAnimal(@PathVariable int id){
        animalService.delete(id);
    }
}
