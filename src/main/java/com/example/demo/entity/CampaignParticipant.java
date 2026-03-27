package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "campaign_participants")
@Getter
@Setter
public class CampaignParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "register_date", nullable = false, updatable = false)
    private Timestamp registerDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        success, failed
    }
}