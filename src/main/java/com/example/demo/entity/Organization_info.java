package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization_info")
@Getter
@Setter
public class Organization_info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Lob
    private String description;

    @Column(length = 255)
    private String logo;

    @Column(length = 255)
    private String facebook;

    @Column(length = 255)
    private String twitter;

    @Column(length = 255)
    private String instagram;

    @Column(length = 255)
    private String youtube;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.sql.Timestamp updatedAt;
}
