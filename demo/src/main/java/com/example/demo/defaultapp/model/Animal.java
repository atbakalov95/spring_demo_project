package com.example.demo.defaultapp.model;

import com.example.demo.defaultapp.utils.NamedParamStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String owner;

    @ManyToOne
    @JsonIgnore
    private Zoo zoo;

    @Transient
    @JsonIgnore
    public List<String> getQueryInsertParameters() {
        return new ArrayList<>(Arrays.asList("name", "zoo_id", "dtype"));
    }

    public abstract NamedParamStatement addBatch(NamedParamStatement namedParamStatement);
}
