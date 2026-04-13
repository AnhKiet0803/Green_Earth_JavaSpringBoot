package com.example.demo.repository;

import com.example.demo.entity.PartnerAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerAssetRepository extends JpaRepository<PartnerAsset, Long> {
    List<PartnerAsset> findByApplicationIdOrderByUploadedAtDesc(Long applicationId);
}

