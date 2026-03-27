package com.example.demo.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class CampaignReq {
    private Long id;

    @NotBlank(message = "Input campaign title")
    @Min(value = 5, message = "Campaign title must be at least 5 characters")
    private String title;

    private String description;

    private String location;

    private Date startDate;

    private Date endDate;


    @NotNull(message = "Input target amount")
    @DecimalMin(value = "0.00", message = "Target amount cannot be less than 0")
    private BigDecimal targetAmount;

    private String image;

    private Long createdBy;
}