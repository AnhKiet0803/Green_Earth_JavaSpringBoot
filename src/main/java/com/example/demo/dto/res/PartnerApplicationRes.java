package com.example.demo.dto.res;

import com.example.demo.entity.PartnerApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PartnerApplicationRes {
    private Long id;
    private String organizationName;
    private String contactName;
    private String email;
    private String phoneNumber;
    private String website;
    private String country;
    private String companySize;
    private String industry;
    private String programType;
    private String expectedContribution;
    private String timeline;
    private String message;
    private String status;
    private String adminNote;
    private Long approvedUserId;
    private Timestamp submittedAt;
    private Timestamp approvedAt;
    private String partnerLoginLink;

    public static PartnerApplicationRes toJson(PartnerApplication entity, String partnerLoginLink) {
        return new PartnerApplicationRes(
                entity.getId(),
                entity.getOrganizationName(),
                entity.getContactName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getWebsite(),
                entity.getCountry(),
                entity.getCompanySize(),
                entity.getIndustry(),
                entity.getProgramType(),
                entity.getExpectedContribution(),
                entity.getTimeline(),
                entity.getMessage(),
                entity.getStatus(),
                entity.getAdminNote(),
                entity.getApprovedUserId(),
                entity.getSubmittedAt(),
                entity.getApprovedAt(),
                partnerLoginLink
        );
    }

    public static PartnerApplicationRes toJson(PartnerApplication entity) {
        return toJson(entity, null);
    }
}
