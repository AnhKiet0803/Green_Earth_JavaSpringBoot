package com.example.demo.service;

import com.example.demo.dto.req.OrganizationInfoReq;
import com.example.demo.dto.res.OrganizationInfoRes;
import com.example.demo.entity.Organization_info;
import com.example.demo.repository.OrganizationInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationInfoService {
    private final OrganizationInfoRepository organizationInfoRepository;

    public OrganizationInfoRes getOrganizationInfo() {
        Organization_info organizationInfo = organizationInfoRepository.findAll().stream().findFirst().orElse(null);
        return organizationInfo != null ? OrganizationInfoRes.toJson(organizationInfo) : null;
    }

    public OrganizationInfoRes update(Long id, OrganizationInfoReq req) {
        try {
            Organization_info organizationInfo = organizationInfoRepository.findById(id).orElseThrow();
            organizationInfo.setName(req.getName());
            organizationInfo.setEmail(req.getEmail());
            organizationInfo.setPhone(req.getPhone());
            organizationInfo.setAddress(req.getAddress());
            organizationInfo.setDescription(req.getDescription());
            organizationInfo.setLogo(req.getLogo());
            organizationInfo.setFacebook(req.getFacebook());
            organizationInfo.setTwitter(req.getTwitter());
            organizationInfo.setInstagram(req.getInstagram());
            organizationInfo.setYoutube(req.getYoutube());
            return OrganizationInfoRes.toJson(organizationInfoRepository.save(organizationInfo));
        }catch (Exception e){
            return null;
        }
    }
}