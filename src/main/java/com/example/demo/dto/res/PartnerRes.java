package com.example.demo.dto.res;

import com.example.demo.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PartnerRes {
    private Long id;
    private String name;
    private String description;
    private String logo;
    private String website;

    public static PartnerRes toJson(Partner partner) {
        return new PartnerRes(
                partner.getId(),
                partner.getName(),
                partner.getDescription(),
                partner.getLogo(),
                partner.getWebsite()
        );
    }
}