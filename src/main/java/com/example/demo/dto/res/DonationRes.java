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
    private String donorName;
    private String campaignName;
    private BigDecimal amount;
    private String message;
    private String paymentMethod;
    private String donationDate;

    public static DonationRes toJson(Donation donation) {
        String name = "Anonymous";

        //Nếu có User (đã đăng nhập)
        if (donation.getUser() != null) {
            name = donation.getUser().getName();
        }
        //Nếu không có User, lấy từ cột donorName (khách vãng lai)
        else if (donation.getDonorName() != null && !donation.getDonorName().isEmpty()) {
            name = donation.getDonorName();
        }

        return new DonationRes(
                donation.getId(),
                name,
                donation.getCampaign() != null ? donation.getCampaign().getTitle() : "General Donation",
                donation.getAmount(),
                donation.getMessage(),
                donation.getPaymentMethod(),
                donation.getDonationDate() != null ? donation.getDonationDate().toString() : ""
        );
    }
}