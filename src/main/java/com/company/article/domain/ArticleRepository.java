package com.company.article.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

        Page<Article> findAll(Pageable pageable);

        @Query("SELECT COUNT(a) FROM Article a WHERE a.created >= :startDate")
        Long countArticlesFromStartDate(LocalDate startDate);
}
