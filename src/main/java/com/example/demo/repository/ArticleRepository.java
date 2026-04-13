package com.example.demo.repository;

import com.example.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // CAST Lob to string before LOWER — Hibernate 7 does not allow LOWER() directly on CLOB/@Lob
    @Query("SELECT a FROM Article a LEFT JOIN a.category cat WHERE " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(CAST(a.content AS string), '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(cat.name, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(COALESCE(a.searchKeywords, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Article> searchByKeyword(@Param("q") String q, Pageable pageable);
}
