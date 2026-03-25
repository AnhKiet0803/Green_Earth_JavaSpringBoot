package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "petitions")
@Getter
@Setter
public class Petition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String title;

    @Lob
    private String description;

    @Column(name = "target_signatures")
    private Integer targetSignatures;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
