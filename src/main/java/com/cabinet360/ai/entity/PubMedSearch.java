package com.cabinet360.ai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pubmed_searches")
public class PubMedSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String searchQuery;

    @Column(nullable = false)
    private String searchTerms;

    @Column(nullable = false)
    private LocalDateTime searchDate = LocalDateTime.now();

    @Column
    private Integer resultsCount;

    @Column(columnDefinition = "TEXT")
    private String searchContext; // Patient context, symptoms, etc.

    // Constructors
    public PubMedSearch() {}

    public PubMedSearch(Long userId, String searchQuery, String searchTerms) {
        this.userId = userId;
        this.searchQuery = searchQuery;
        this.searchTerms = searchTerms;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getSearchTerms() { return searchTerms; }
    public void setSearchTerms(String searchTerms) { this.searchTerms = searchTerms; }

    public LocalDateTime getSearchDate() { return searchDate; }
    public void setSearchDate(LocalDateTime searchDate) { this.searchDate = searchDate; }

    public Integer getResultsCount() { return resultsCount; }
    public void setResultsCount(Integer resultsCount) { this.resultsCount = resultsCount; }

    public String getSearchContext() { return searchContext; }
    public void setSearchContext(String searchContext) { this.searchContext = searchContext; }
}
