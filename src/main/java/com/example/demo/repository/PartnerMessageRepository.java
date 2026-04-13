package com.example.demo.repository;

import com.example.demo.entity.PartnerMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerMessageRepository extends JpaRepository<PartnerMessage, Long> {
    List<PartnerMessage> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);
}

