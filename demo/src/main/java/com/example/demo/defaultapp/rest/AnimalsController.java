package com.example.demo.defaultapp.rest;

import com.example.demo.defaultapp.model.Animal;
import com.example.demo.defaultapp.model.Feline;
import com.example.demo.defaultapp.model.Message;
import com.example.demo.defaultapp.services.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/animals")
public class AnimalsController {
    private final AnimalService animalService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnimalsController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public EntityModel<Message> baseResponse(){
        String responseString =  "Hello from base page! This page is available to anyone!";

        return EntityModel.of(Message.builder().message(responseString).build(),
                linkTo(methodOn(AnimalsController.class).getAnimalById(0)).withSelfRel(),
                linkTo(methodOn(AnimalsController.class).createFeline(null)).withSelfRel(),
                linkTo(methodOn(AnimalsController.class).deleteAnimal(0)).withSelfRel()
        );
    }

    @GetMapping(value = "/animal/{id}")
    @PreAuthorize("hasAuthority('READ_ANIMAL')")
    public ResponseEntity<Animal> getAnimalById(@PathVariable int id) {
        return ResponseEntity.ok(animalService.findOne(id));
    }

    @SneakyThrows
    @PostMapping(value = "/createFeline")
    @PreAuthorize("hasAuthority('CREATE_ANIMAL')")
    public ResponseEntity<Message> createFeline(@RequestBody Feline animal) {
        animalService.persist(animal);
        return ResponseEntity.ok(Message.builder().message(objectMapper.writeValueAsString(animal)).build());
    }

    @DeleteMapping(value = "/deleteAnimal/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable int id){
        animalService.delete(id);
        return ResponseEntity.ok().build();
    }
}
