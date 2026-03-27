package com.example.demo.repository;

import com.example.demo.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign.id = :campaignId")
    BigDecimal getTotalDonatedByCampaignId(@Param("campaignId") Long campaignId);
}