package com.example.demo.resourceserver.rest;

import com.example.demo.resourceserver.model.Animal;
import com.example.demo.resourceserver.model.Feline;
import com.example.demo.resourceserver.services.AnimalService;
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

    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "Hello authorized user!";
    }

    @GetMapping(value = "/admin")
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
