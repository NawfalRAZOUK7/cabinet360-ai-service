package com.cabinet360.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PubMedSearchRequest {

    @NotBlank(message = "Search query cannot be empty")
    private String query;

    private Integer maxResults = 10;
    private String patientContext;
    private String specialty;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Integer getMaxResults() { return maxResults; }
    public void setMaxResults(Integer maxResults) { this.maxResults = maxResults; }

    public String getPatientContext() { return patientContext; }
    public void setPatientContext(String patientContext) { this.patientContext = patientContext; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
