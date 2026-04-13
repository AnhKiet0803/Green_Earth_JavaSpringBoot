package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(u.phone, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<User> searchByKeyword(@Param("q") String q, Pageable pageable);
}
