package com.example.demo.service;

import com.example.demo.dto.req.PartnerReq;
import com.example.demo.dto.res.PartnerRes;
import com.example.demo.entity.Partner;
import com.example.demo.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;

    public List<PartnerRes> getAllPartners() {
        return partnerRepository.findAll().stream().map(PartnerRes::toJson).toList();
    }

    public PartnerRes findById(Long id) {
        return PartnerRes.toJson(partnerRepository.findById(id).get());
    }

    public PartnerRes create(PartnerReq req) {
        try {
            Partner partner = new Partner();
            partner.setName(req.getName());
            partner.setDescription(req.getDescription());
            partner.setLogo(req.getLogo());
            partner.setWebsite(req.getWebsite());
            return PartnerRes.toJson(partnerRepository.save(partner));
        }catch (Exception e){
            return null;
        }
    }

    public PartnerRes update(Long id, PartnerReq req) {
        try {
            Partner partner = partnerRepository.findById(id).get();
            partner.setName(req.getName());
            partner.setDescription(req.getDescription());
            partner.setLogo(req.getLogo());
            partner.setWebsite(req.getWebsite());
            return PartnerRes.toJson(partnerRepository.save(partner));
        }catch (Exception e){
            return null;
        }
    }

    public void delete(Long id) {
        partnerRepository.deleteById(id);
    }
}