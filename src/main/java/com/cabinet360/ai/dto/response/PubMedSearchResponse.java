package com.cabinet360.ai.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PubMedSearchResponse {

    private List<PubMedArticleDto> articles;
    private int totalResults;
    private String searchQuery;
    private String searchId;
    private LocalDateTime searchDate;

    public PubMedSearchResponse() {}

    public PubMedSearchResponse(List<PubMedArticleDto> articles, int totalResults, String searchQuery) {
        this.articles = articles;
        this.totalResults = totalResults;
        this.searchQuery = searchQuery;
        this.searchDate = LocalDateTime.now();
    }

    // Getters and Setters
    public List<PubMedArticleDto> getArticles() { return articles; }
    public void setArticles(List<PubMedArticleDto> articles) { this.articles = articles; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getSearchId() { return searchId; }
    public void setSearchId(String searchId) { this.searchId = searchId; }

    public LocalDateTime getSearchDate() { return searchDate; }
    public void setSearchDate(LocalDateTime searchDate) { this.searchDate = searchDate; }
}