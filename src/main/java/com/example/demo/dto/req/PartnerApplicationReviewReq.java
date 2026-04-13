package com.example.demo.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerApplicationReviewReq {
    @NotBlank(message = "Status is required")
    private String status;

    private String adminNote;
}
