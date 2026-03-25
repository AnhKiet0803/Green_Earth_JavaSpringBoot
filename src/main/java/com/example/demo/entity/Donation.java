package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "donations")
@Getter
@Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String message;

    @Column(length = 50)
    private String paymentMethod;

    @Column(name = "donation_date", nullable = false, updatable = false)
    private Timestamp donationDate;
}