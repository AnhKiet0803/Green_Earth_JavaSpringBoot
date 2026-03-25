package com.example.demo.service;

import com.example.demo.dto.req.CampaignReq;
import com.example.demo.dto.res.CampaignRes;
import com.example.demo.entity.Campaign;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public List<CampaignRes> getAllCampaigns() {
        return campaignRepository.findAll().stream().map(CampaignRes::toJson).toList();
    }

    public CampaignRes findById(Long id) {
        return CampaignRes.toJson(campaignRepository.findById(id).get());
    }

    public CampaignRes create(CampaignReq req) {
        try {
            Campaign campaign = new Campaign();
            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setLocation(req.getLocation());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());
            campaign.setTargetVolunteers(req.getTargetVolunteers());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setImage(req.getImage());
            campaign.setCreatedBy(userRepository.findById(req.getCreatedBy()).get());
            return CampaignRes.toJson(campaignRepository.save(campaign));
        }catch (Exception e){
            return null;
        }
    }

    public CampaignRes update(Long id, CampaignReq req) {
        try {
            Campaign campaign = campaignRepository.findById(id).get();
            campaign.setTitle(req.getTitle());
            campaign.setDescription(req.getDescription());
            campaign.setLocation(req.getLocation());
            campaign.setStartDate(req.getStartDate());
            campaign.setEndDate(req.getEndDate());
            campaign.setTargetVolunteers(req.getTargetVolunteers());
            campaign.setTargetAmount(req.getTargetAmount());
            campaign.setImage(req.getImage());
            campaign.setCreatedBy(userRepository.findById(req.getCreatedBy()).get());
            return CampaignRes.toJson(campaignRepository.save(campaign));
        }catch (Exception e){
            return null;
        }
    }

    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }
}