package com.example.openidauthorizationserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
    @GetMapping("userInfo")
    public Principal user(Principal principal){
        return principal;
    }
}
