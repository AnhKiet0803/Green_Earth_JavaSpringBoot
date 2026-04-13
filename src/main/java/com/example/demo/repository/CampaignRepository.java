package com.example.demo.repository;

import com.example.demo.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE " +
            "(LOWER(c.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(CAST(c.description AS string), '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(c.location, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(c.searchKeywords, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Campaign> searchByKeyword(@Param("q") String q, Pageable pageable);
}
