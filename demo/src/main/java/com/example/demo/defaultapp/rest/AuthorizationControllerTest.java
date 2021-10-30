package com.example.demo.defaultapp.rest;

import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class AuthorizationControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public EntityModel<Message> baseResponse(){
        String responseString =  "Hello from base page! This page is available to anyone!";

        return EntityModel.of(Message.builder().message(responseString).build(),
                linkTo(methodOn(AuthorizationControllerTest.class).triggerError()).withSelfRel(),
                linkTo(methodOn(AuthorizationControllerTest.class).tryRead()).withSelfRel(),
                linkTo(methodOn(AuthorizationControllerTest.class).sayHelloAdmin()).withSelfRel()
        );
    }

    @SneakyThrows
    @GetMapping(value = "/manual_error")
    public ResponseEntity<Message> triggerError() {
        throw new ServiceException("Manually triggered exception");
    }

    @RequestMapping(value = "/read")
    @PreAuthorize("hasAuthority('READ_ANIMAL')")
    public ResponseEntity<Message> tryRead() {
        return ResponseEntity.ok(Message.builder()
                .message("Hello authorized user! This page is available only for users with READ_ANIMAL access. " +
                "For now it is user and any other oauth2 based user!").build());
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAuthority('DELETE_ANIMAL')")
    public ResponseEntity<Message> sayHelloAdmin() {
        return ResponseEntity.ok(Message.builder().message("Hello to admin user!").build());
    }
}
