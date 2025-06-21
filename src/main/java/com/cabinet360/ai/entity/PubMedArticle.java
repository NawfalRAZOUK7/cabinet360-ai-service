package com.cabinet360.ai.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pubmed_articles")
public class PubMedArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pmid; // PubMed ID

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @Column
    private String authors;

    @Column
    private String journal;

    @Column
    private String publicationDate;

    @Column(columnDefinition = "TEXT")
    private String keywords;

    @Column
    private String doi;

    @Column(nullable = false)
    private LocalDateTime indexedAt = LocalDateTime.now();

    // For AI-generated summaries
    @Column(columnDefinition = "TEXT")
    private String aiSummary;

    @Column
    private Double relevanceScore;

    // Constructors
    public PubMedArticle() {}

    public PubMedArticle(String pmid, String title, String abstractText) {
        this.pmid = pmid;
        this.title = title;
        this.abstractText = abstractText;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPmid() { return pmid; }
    public void setPmid(String pmid) { this.pmid = pmid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public String getJournal() { return journal; }
    public void setJournal(String journal) { this.journal = journal; }

    public String getPublicationDate() { return publicationDate; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }

    public LocalDateTime getIndexedAt() { return indexedAt; }
    public void setIndexedAt(LocalDateTime indexedAt) { this.indexedAt = indexedAt; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
}