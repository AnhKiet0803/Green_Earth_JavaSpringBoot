package com.example.demo.service;

import com.example.demo.dto.res.DonationRes;
import com.example.demo.repository.DonationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;

    public List<DonationRes> getAllDonations() {
        return donationRepository.findAll().stream().map(DonationRes::toJson).toList();
    }

    public DonationRes findById(Long id) {
        return DonationRes.toJson(donationRepository.findById(id).get());
    }
}