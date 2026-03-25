package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "celebrities")
@Getter
@Setter
public class Celebrity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String name;

    @Lob
    private String description;

    @Column(length = 255)
    private String image;

    @Column(length = 255)
    private String socialLink;
}
