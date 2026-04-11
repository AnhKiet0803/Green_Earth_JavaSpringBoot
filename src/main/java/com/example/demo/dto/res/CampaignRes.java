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
        res.setTargetAmount(campaign.getTargetAmount() != null ? campaign.getTargetAmount() : BigDecimal.ZERO);
        res.setImage(campaign.getImage());
        BigDecimal raised = (raisedAmount != null) ? raisedAmount : BigDecimal.ZERO;
        res.setRaisedAmount(raised);

        double percentage = 0.0;
        if (res.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = raised.multiply(new BigDecimal(100))
                    .divide(res.getTargetAmount(), 2, RoundingMode.HALF_UP);
            percentage = progress.doubleValue();
        }
        res.setProgressPercentage(percentage);

        long now = System.currentTimeMillis();
        Long startTime = (campaign.getStartDate() != null) ? campaign.getStartDate().getTime() : null;
        Long endTime = (campaign.getEndDate() != null) ? campaign.getEndDate().getTime() : null;

        if (percentage >= 100 || (endTime != null && now > endTime)) {
            res.setStatus("COMPLETED");
        }
        else if (startTime != null && now < startTime) {
            res.setStatus("UPCOMING");
        }
        else {
            res.setStatus("ONGOING");
        }
        return res;
    }
}