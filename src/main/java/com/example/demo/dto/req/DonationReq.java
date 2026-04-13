package com.example.demo.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DonationReq {
    private Long campaignId;
    private BigDecimal amount;
    private String message;
    private String paymentMethod;
    private String donorName;
}
