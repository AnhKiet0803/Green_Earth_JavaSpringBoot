package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gallery")
@Getter
@Setter
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(length = 255)
    private String image;

    @Column(length = 255)
    private String caption;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.sql.Timestamp createdAt;
}
