package com.example.demo.service;

import com.example.demo.dto.req.DonationReq;
import com.example.demo.dto.res.DonationRes;
import com.example.demo.entity.Campaign;
import com.example.demo.entity.Donation;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public List<DonationRes> getAllDonations() {
        return donationRepository.findAll().stream().map(DonationRes::toJson).toList();
    }

    public DonationRes findById(Long id) {
        return DonationRes.toJson(donationRepository.findById(id).get());
    }

    public DonationRes createDonation(DonationReq req) {
        Campaign campaign = campaignRepository.findById(req.getCampaignId())
                .orElseThrow(() -> new RuntimeException("No campaign found."));

        Donation donation = new Donation();
        donation.setCampaign(campaign);
        donation.setAmount(req.getAmount());
        donation.setMessage(req.getMessage());
        donation.setPaymentMethod(req.getPaymentMethod());
        donation.setDonorName(req.getDonorName());
        donation.setDonationDate(new java.sql.Timestamp(System.currentTimeMillis()));

        donation.setUser(null);

        Donation saved = donationRepository.save(donation);
        return DonationRes.toJson(saved);
    }

}