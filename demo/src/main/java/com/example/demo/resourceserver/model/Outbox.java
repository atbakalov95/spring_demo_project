package com.example.demo.resourceserver.model;

import com.example.demo.resourceserver.enums.MessageStatusEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Outbox {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageStatusEnum status;
}
