package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(length = 255)
    private String location;

    private Date startDate;

    private Date endDate;

    @Column(name = "target_volunteers")
    private Integer targetVolunteers;

    @Column(name = "target_amount", precision = 12, scale = 2)
    private BigDecimal targetAmount;

    @Column(length = 255)
    private String image;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.sql.Timestamp createdAt;

    public enum Status {
        upcoming, ongoing, completed
    }
}