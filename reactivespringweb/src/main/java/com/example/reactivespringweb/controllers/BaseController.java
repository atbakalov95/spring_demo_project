package com.example.reactivespringweb.controllers;

import com.example.reactivespringweb.models.Author;
import com.example.reactivespringweb.services.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/base")
public class BaseController {
    private final AuthorService authorService;

    public BaseController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/greetFromAuthors")
    public Flux<Author> greetFromAuthors() {
        List<Author> authors = authorService.readAuthors();
        return Flux.fromArray(authors.toArray(Author[]::new));
    }
}
