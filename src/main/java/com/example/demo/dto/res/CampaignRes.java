package com.example.demo.dto.res;

import com.example.demo.entity.Campaign;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@AllArgsConstructor
@Getter
@Setter
public class CampaignRes {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Date startDate;
    private Date endDate;
    private Integer targetVolunteers;
    private BigDecimal targetAmount;
    private String image;

    public static CampaignRes toJson(Campaign campaign){
        return new CampaignRes(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getLocation(),
                campaign.getStartDate(),
                campaign.getEndDate(),
                campaign.getTargetVolunteers(),
                campaign.getTargetAmount(),
                campaign.getImage()
        );
    }
}
