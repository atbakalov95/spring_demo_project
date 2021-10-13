package com.example.demo.defaultapp.model;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
