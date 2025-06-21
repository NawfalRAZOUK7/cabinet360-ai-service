package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.PubMedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PubMedArticleRepository extends JpaRepository<PubMedArticle, Long> {

    Optional<PubMedArticle> findByPmid(String pmid);

    boolean existsByPmid(String pmid);

    @Query("SELECT p FROM PubMedArticle p WHERE p.keywords LIKE %:keyword% OR p.title LIKE %:keyword% ORDER BY p.relevanceScore DESC")
    List<PubMedArticle> findByKeywordOrTitle(String keyword);

    @Query("SELECT p FROM PubMedArticle p WHERE p.relevanceScore >= :minScore ORDER BY p.relevanceScore DESC")
    List<PubMedArticle> findHighRelevanceArticles(Double minScore);

    @Query("SELECT p FROM PubMedArticle p WHERE p.aiSummary IS NOT NULL ORDER BY p.indexedAt DESC")
    List<PubMedArticle> findArticlesWithAiSummary();
}