package com.cabinet360.ai.dto.response;

public class PubMedArticleDto {

    private String pmid;
    private String title;
    private String abstractText;
    private String authors;
    private String journal;
    private String publicationDate;
    private String doi;
    private String aiSummary;
    private Double relevanceScore;

    public PubMedArticleDto() {}

    public PubMedArticleDto(String pmid, String title, String abstractText) {
        this.pmid = pmid;
        this.title = title;
        this.abstractText = abstractText;
    }

    // Getters and Setters
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

    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
}
