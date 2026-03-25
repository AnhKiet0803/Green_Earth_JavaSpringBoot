package com.example.demo.dto.res;

import com.example.demo.entity.Organization_info;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrganizationInfoRes {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String description;
    private String logo;
    private String facebook;
    private String twitter;
    private String instagram;
    private String youtube;

    public static OrganizationInfoRes toJson(Organization_info organizationInfo) {
        return new OrganizationInfoRes(
                organizationInfo.getId(),
                organizationInfo.getName(),
                organizationInfo.getEmail(),
                organizationInfo.getPhone(),
                organizationInfo.getAddress(),
                organizationInfo.getDescription(),
                organizationInfo.getLogo(),
                organizationInfo.getFacebook(),
                organizationInfo.getTwitter(),
                organizationInfo.getInstagram(),
                organizationInfo.getYoutube()
        );
    }
}