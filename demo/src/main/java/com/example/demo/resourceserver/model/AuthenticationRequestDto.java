package com.example.demo.resourceserver.model;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
