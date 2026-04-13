package com.example.demo.repository;

import com.example.demo.entity.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign.id = :campaignId")
    BigDecimal getTotalDonatedByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT d FROM Donation d LEFT JOIN d.user u LEFT JOIN d.campaign c WHERE " +
            "(LOWER(COALESCE(d.donorName, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.name, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(c.title, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(d.message, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(d.paymentMethod, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Donation> searchByKeyword(@Param("q") String q, Pageable pageable);
}