package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "partner_applications")
@Getter
@Setter
public class PartnerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name", length = 150, nullable = false)
    private String organizationName;

    @Column(name = "contact_name", length = 100, nullable = false)
    private String contactName;

    @Column(length = 150, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @Column(length = 255)
    private String website;

    @Column(length = 100)
    private String country;

    @Column(name = "company_size", length = 40)
    private String companySize;

    @Column(length = 80)
    private String industry;

    @Column(name = "program_type", length = 30)
    private String programType;

    @Lob
    private String expectedContribution;

    @Column(length = 120)
    private String timeline;

    @Lob
    private String message;

    @Column(length = 30, nullable = false)
    private String status;

    @Lob
    private String adminNote;

    @Column(name = "approved_user_id")
    private Long approvedUserId;

    @Column(name = "submitted_at", nullable = false)
    private Timestamp submittedAt;

    @Column(name = "approved_at")
    private Timestamp approvedAt;
}
