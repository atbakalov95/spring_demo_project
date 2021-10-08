package com.example.demo.authserver.model;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
