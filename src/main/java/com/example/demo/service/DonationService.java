package com.example.demo.service;

import com.example.demo.dto.common.PageResult;
import com.example.demo.dto.req.DonationReq;
import com.example.demo.dto.res.DonationRes;
import com.example.demo.entity.Campaign;
import com.example.demo.entity.Donation;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.DonationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;

    public List<DonationRes> getAllDonations() {
        return donationRepository.findAll().stream().map(DonationRes::toJson).toList();
    }

    public PageResult<DonationRes> searchDonations(String q, int page, int size) {
        Pageable pg = PageRequest.of(page, Math.min(Math.max(size, 1), 100));
        Page<Donation> p = (q == null || q.isBlank())
                ? donationRepository.findAll(pg)
                : donationRepository.searchByKeyword(q.trim(), pg);
        List<DonationRes> content = p.getContent().stream().map(DonationRes::toJson).toList();
        return new PageResult<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
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
