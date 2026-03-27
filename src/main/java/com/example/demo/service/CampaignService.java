package com.example.demo.service;

import com.example.demo.dto.req.CampaignReq;
import com.example.demo.dto.res.CampaignRes;
import com.example.demo.entity.Campaign;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final DonationRepository donationRepository;

    public List<CampaignRes> getAllCampaigns() {
        return campaignRepository.findAll().stream().map(campaign -> {
            BigDecimal raised = donationRepository.getTotalDonatedByCampaignId(campaign.getId());
            return CampaignRes.toJson(campaign, raised);
        }).toList();
    }

    public CampaignRes findById(Long id) {
        Campaign campaign = campaignRepository.findById(id).orElseThrow(()->new RuntimeException("Campaign not found"));
        BigDecimal raised = donationRepository.getTotalDonatedByCampaignId(id);
        return CampaignRes.toJson(campaign, raised);
    }

    public CampaignRes create(CampaignReq req) {
        try {
            Campaign campaign = new Campaign();
            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setLocation(req.getLocation());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setImage(req.getImage());
            if (req.getCreatedBy() == null) {
                throw new RuntimeException("createdBy must not be null");
            }
            campaign.setCreatedBy(userRepository.findById(req.getCreatedBy()).orElseThrow(() -> new RuntimeException("User not found")));
            campaign.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            Campaign saved = campaignRepository.save(campaign);
            return CampaignRes.toJson(saved, BigDecimal.ZERO);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public CampaignRes update(Long id, CampaignReq req) {
        try {
            Campaign campaign = campaignRepository.findById(id).orElseThrow(()
                    -> new RuntimeException("Campaign not found"));
            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setLocation(req.getLocation());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setImage(req.getImage());
            Campaign updated = campaignRepository.save(campaign);
            BigDecimal raised = donationRepository.getTotalDonatedByCampaignId(id);
            return CampaignRes.toJson(updated, raised);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }
}