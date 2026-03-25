package com.example.demo.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerReq {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String logo;

    private String website;
}