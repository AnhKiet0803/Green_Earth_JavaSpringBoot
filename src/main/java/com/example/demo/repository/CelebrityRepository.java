package com.example.demo.repository;

import com.example.demo.entity.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {
}