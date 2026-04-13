package com.example.demo.repository;

import com.example.demo.entity.PartnerApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PartnerApplicationRepository extends JpaRepository<PartnerApplication, Long> {
    Optional<PartnerApplication> findByEmailAndStatus(String email, String status);
    Optional<PartnerApplication> findTopByEmailAndStatusOrderBySubmittedAtDesc(String email, String status);

    @Query("SELECT p FROM PartnerApplication p WHERE " +
            "(LOWER(p.organizationName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(p.contactName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(p.phoneNumber, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(p.industry, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(p.status) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<PartnerApplication> searchByKeyword(@Param("q") String q, Pageable pageable);
}
