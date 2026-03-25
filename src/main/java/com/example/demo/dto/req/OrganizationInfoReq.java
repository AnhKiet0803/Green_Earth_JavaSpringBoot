package com.example.demo.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationInfoReq {
    private Long id;

    @NotBlank(message = "Organization name is required")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    private String address;
    private String description;
    private String logo;
    private String facebook;
    private String twitter;
    private String instagram;
    private String youtube;
}