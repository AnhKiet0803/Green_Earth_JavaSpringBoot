package com.example.demo.repository;

import com.example.demo.entity.Organization_info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationInfoRepository extends JpaRepository<Organization_info, Long> {
}