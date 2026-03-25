package com.example.demo.repository;

import com.example.demo.entity.CampaignParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignParticipantRepository extends JpaRepository<CampaignParticipant, Long> {
}
