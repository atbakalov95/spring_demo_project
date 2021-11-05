package com.example.reactivespringweb.models.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String occupancy;
    private Double age;
}
