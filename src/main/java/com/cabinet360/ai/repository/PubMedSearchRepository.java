package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.PubMedSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PubMedSearchRepository extends JpaRepository<PubMedSearch, Long> {

    List<PubMedSearch> findByUserIdOrderBySearchDateDesc(Long userId);

    @Query("SELECT p FROM PubMedSearch p WHERE p.userId = :userId AND p.searchDate >= :fromDate ORDER BY p.searchDate DESC")
    List<PubMedSearch> findRecentSearchesByUser(Long userId, LocalDateTime fromDate);

    @Query("SELECT p.searchTerms, COUNT(p) as count FROM PubMedSearch p WHERE p.userId = :userId GROUP BY p.searchTerms ORDER BY count DESC")
    List<Object[]> findPopularSearchTermsByUser(Long userId);

    long countByUserIdAndSearchDateAfter(Long userId, LocalDateTime date);
}