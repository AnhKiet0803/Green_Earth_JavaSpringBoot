package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "petition_signatures")
@Getter
@Setter
public class PetitionSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "petition_id")
    private Petition petition;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "signed_at", nullable = false, updatable = false)
    private Timestamp signedAt;
}
