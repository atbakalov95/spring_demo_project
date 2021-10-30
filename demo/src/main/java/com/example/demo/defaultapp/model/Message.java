package com.example.demo.defaultapp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@Builder
public class Message {
    private String message;
}
