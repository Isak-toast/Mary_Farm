package com.ssafy.maryfarmboardservice.repository;

import com.ssafy.maryfarmboardservice.domain.board.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {
    Optional<Article> findById(String id);
}