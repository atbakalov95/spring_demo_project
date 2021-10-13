package com.example.demo.defaultapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Zoo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "zoo", fetch = FetchType.EAGER)
    private List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
        animal.setZoo(this);
    }
}
