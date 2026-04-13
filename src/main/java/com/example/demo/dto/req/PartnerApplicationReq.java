package com.example.demo.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerApplicationReq {
    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Contact name is required")
    private String contactName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Website is required")
    private String website;

    private String country;

    private String companySize;

    private String industry;

    private String programType;

    private String expectedContribution;

    private String timeline;

    private String message;
}
