package com.example.demo.dto.res;

import com.example.demo.entity.Campaign;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
@NoArgsConstructor
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
    private BigDecimal targetAmount;
    private String image;
    private String status;
    private BigDecimal raisedAmount;
    private Double progressPercentage;

    public static CampaignRes toJson(Campaign campaign, BigDecimal raisedAmount) {
        CampaignRes res = new CampaignRes();

        res.setId(campaign.getId());
        res.setTitle(campaign.getTitle());
        res.setDescription(campaign.getDescription());
        res.setLocation(campaign.getLocation());
        res.setStartDate(campaign.getStartDate());
        res.setEndDate(campaign.getEndDate());
        res.setTargetAmount(campaign.getTargetAmount());
        res.setImage(campaign.getImage());

        if (campaign.getStatus() != null) {
            res.setStatus(campaign.getStatus().name().toUpperCase());
        }

        BigDecimal raised = (raisedAmount != null) ? raisedAmount : BigDecimal.ZERO;
        res.setRaisedAmount(raised);

        if (campaign.getTargetAmount() != null && campaign.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = raised.multiply(new BigDecimal(100))
                    .divide(campaign.getTargetAmount(), 2, RoundingMode.HALF_UP);
            res.setProgressPercentage(progress.doubleValue());
        } else {
            res.setProgressPercentage(0.0);
        }
        return res;
    }

}
