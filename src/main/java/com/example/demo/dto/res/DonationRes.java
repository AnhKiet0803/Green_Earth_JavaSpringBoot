package com.example.demo.dto.res;

import com.example.demo.entity.Donation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class DonationRes {
    private Long id;
    private Long userId;
    private Long campaignId;
    private BigDecimal amount;
    private String message;
    private String paymentMethod;
    private String donationDate;

    public static DonationRes toJson(Donation donation) {
        return new DonationRes(
                donation.getId(),
                donation.getUser().getId(),
                donation.getCampaign().getId(),
                donation.getAmount(),
                donation.getMessage(),
                donation.getPaymentMethod(),
                donation.getDonationDate().toString()
        );
    }
}