package com.cabinet360.ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 💊 Drug Interaction Entity
 * Stores drug interaction analysis results and history
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@Entity
@Table(name = "drug_interactions", indexes = {
    @Index(name = "idx_drug_interaction_user_id", columnList = "user_id"),
    @Index(name = "idx_drug_interaction_created_at", columnList = "created_at"),
    @Index(name = "idx_drug_interaction_risk_level", columnList = "risk_level"),
    @Index(name = "idx_drug_interaction_medications", columnList = "medication_list")
})
public class DrugInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 100)
    @Column(name = "user_email", length = 100)
    private String userEmail;

    @NotNull
    @Size(min = 2, max = 2000)
    @Column(name = "medication_list", nullable = false, length = 2000)
    private String medicationList; // JSON array of medications

    @Column(name = "medication_count")
    private Integer medicationCount;

    @Size(max = 1000)
    @Column(name = "patient_conditions", length = 1000)
    private String patientConditions;

    @Size(max = 50)
    @Column(name = "patient_age", length = 50)
    private String patientAge;

    @Size(max = 20)
    @Column(name = "patient_gender", length = 20)
    private String patientGender;

    @Size(max = 100)
    @Column(name = "patient_weight", length = 100)
    private String patientWeight;

    @Size(max = 500)
    @Column(name = "allergies", length = 500)
    private String allergies;

    @Size(max = 500)
    @Column(name = "kidney_function", length = 500)
    private String kidneyFunction;

    @Size(max = 500)
    @Column(name = "liver_function", length = 500)
    private String liverFunction;

    @Size(max = 1000)
    @Column(name = "current_symptoms", length = 1000)
    private String currentSymptoms; // JSON array

    @Size(max = 20)
    @Column(name = "urgency", length = 20)
    private String urgency; // LOW, MODERATE, HIGH

    @Size(max = 500)
    @Column(name = "additional_notes", length = 500)
    private String additionalNotes;

    // Analysis Results
    @Column(name = "major_interactions", columnDefinition = "TEXT")
    private String majorInteractions; // JSON array of interaction objects

    @Column(name = "moderate_interactions", columnDefinition = "TEXT")
    private String moderateInteractions; // JSON array

    @Column(name = "minor_interactions", columnDefinition = "TEXT")
    private String minorInteractions; // JSON array

    @Column(name = "food_interactions", columnDefinition = "TEXT")
    private String foodInteractions; // JSON array

    @Size(max = 20)
    @Column(name = "risk_level", length = 20)
    private String riskLevel; // LOW, MODERATE, HIGH, CRITICAL

    @Column(name = "overall_risk_assessment", columnDefinition = "TEXT")
    private String overallRiskAssessment;

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations; // JSON array of strings

    @Column(name = "monitoring_recommendations", columnDefinition = "TEXT")
    private String monitoringRecommendations; // JSON array

    @Column(name = "alternative_options", columnDefinition = "TEXT")
    private String alternativeOptions; // JSON array

    @Column(name = "emergency_warnings", columnDefinition = "TEXT")
    private String emergencyWarnings; // JSON array

    @Size(max = 500)
    @Column(name = "pharmacist_consultation", length = 500)
    private String pharmacistConsultation;

    @Column(name = "ai_analysis", columnDefinition = "TEXT")
    private String aiAnalysis; // Raw AI response

    @Size(max = 50)
    @Column(name = "ai_provider", length = 50)
    private String aiProvider; // gemini, openai, etc.

    @Size(max = 20)
    @Column(name = "analysis_version", length = 20)
    private String analysisVersion;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(name = "analysis_duration_ms")
    private Long analysisDurationMs;

    @Column(name = "confidence_score")
    private Double confidenceScore; // 0.0 to 1.0

    @Column(name = "complexity_score")
    private Integer complexityScore; // 0 to 10

    @Column(name = "has_critical_interactions")
    private Boolean hasCriticalInteractions = false;

    @Column(name = "has_emergency_warnings")
    private Boolean hasEmergencyWarnings = false;

    @Column(name = "requires_immediate_attention")
    private Boolean requiresImmediateAttention = false;

    // Metadata
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON object for additional data

    @Size(max = 1000)
    @Column(name = "disclaimer", length = 1000)
    private String disclaimer;

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "analysis_timestamp")
    private LocalDateTime analysisTimestamp;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_reviewed")
    private Boolean isReviewed = false;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Size(max = 100)
    @Column(name = "reviewed_by", length = 100)
    private String reviewedBy;

    @Size(max = 500)
    @Column(name = "review_notes", length = 500)
    private String reviewNotes;

    // Quality tracking
    @Column(name = "user_feedback_rating")
    private Integer userFeedbackRating; // 1-5 stars

    @Size(max = 1000)
    @Column(name = "user_feedback_comments", length = 1000)
    private String userFeedbackComments;

    @Column(name = "is_accurate")
    private Boolean isAccurate;

    @Column(name = "is_helpful")
    private Boolean isHelpful;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public DrugInteraction() {
        this.analysisTimestamp = LocalDateTime.now();
        this.analysisVersion = "1.0";
        this.isActive = true;
        this.isReviewed = false;
        this.hasCriticalInteractions = false;
        this.hasEmergencyWarnings = false;
        this.requiresImmediateAttention = false;
    }

    /**
     * Constructor with basic information
     */
    public DrugInteraction(Long userId, String userEmail, String medicationList) {
        this();
        this.userId = userId;
        this.userEmail = userEmail;
        this.medicationList = medicationList;
        this.createdBy = userEmail;
    }

    /**
     * Constructor with patient data
     */
    public DrugInteraction(Long userId, String userEmail, String medicationList,
                          String patientConditions, String patientAge, String patientGender) {
        this(userId, userEmail, medicationList);
        this.patientConditions = patientConditions;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
    }

    // ================================
    // Getters and Setters
    // ================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMedicationList() {
        return medicationList;
    }

    public void setMedicationList(String medicationList) {
        this.medicationList = medicationList;
    }

    public Integer getMedicationCount() {
        return medicationCount;
    }

    public void setMedicationCount(Integer medicationCount) {
        this.medicationCount = medicationCount;
    }

    public String getPatientConditions() {
        return patientConditions;
    }

    public void setPatientConditions(String patientConditions) {
        this.patientConditions = patientConditions;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getKidneyFunction() {
        return kidneyFunction;
    }

    public void setKidneyFunction(String kidneyFunction) {
        this.kidneyFunction = kidneyFunction;
    }

    public String getLiverFunction() {
        return liverFunction;
    }

    public void setLiverFunction(String liverFunction) {
        this.liverFunction = liverFunction;
    }

    public String getCurrentSymptoms() {
        return currentSymptoms;
    }

    public void setCurrentSymptoms(String currentSymptoms) {
        this.currentSymptoms = currentSymptoms;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getMajorInteractions() {
        return majorInteractions;
    }

    public void setMajorInteractions(String majorInteractions) {
        this.majorInteractions = majorInteractions;
    }

    public String getModerateInteractions() {
        return moderateInteractions;
    }

    public void setModerateInteractions(String moderateInteractions) {
        this.moderateInteractions = moderateInteractions;
    }

    public String getMinorInteractions() {
        return minorInteractions;
    }

    public void setMinorInteractions(String minorInteractions) {
        this.minorInteractions = minorInteractions;
    }

    public String getFoodInteractions() {
        return foodInteractions;
    }

    public void setFoodInteractions(String foodInteractions) {
        this.foodInteractions = foodInteractions;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getOverallRiskAssessment() {
        return overallRiskAssessment;
    }

    public void setOverallRiskAssessment(String overallRiskAssessment) {
        this.overallRiskAssessment = overallRiskAssessment;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getMonitoringRecommendations() {
        return monitoringRecommendations;
    }

    public void setMonitoringRecommendations(String monitoringRecommendations) {
        this.monitoringRecommendations = monitoringRecommendations;
    }

    public String getAlternativeOptions() {
        return alternativeOptions;
    }

    public void setAlternativeOptions(String alternativeOptions) {
        this.alternativeOptions = alternativeOptions;
    }

    public String getEmergencyWarnings() {
        return emergencyWarnings;
    }

    public void setEmergencyWarnings(String emergencyWarnings) {
        this.emergencyWarnings = emergencyWarnings;
    }

    public String getPharmacistConsultation() {
        return pharmacistConsultation;
    }

    public void setPharmacistConsultation(String pharmacistConsultation) {
        this.pharmacistConsultation = pharmacistConsultation;
    }

    public String getAiAnalysis() {
        return aiAnalysis;
    }

    public void setAiAnalysis(String aiAnalysis) {
        this.aiAnalysis = aiAnalysis;
    }

    public String getAiProvider() {
        return aiProvider;
    }

    public void setAiProvider(String aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String getAnalysisVersion() {
        return analysisVersion;
    }

    public void setAnalysisVersion(String analysisVersion) {
        this.analysisVersion = analysisVersion;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    public Long getAnalysisDurationMs() {
        return analysisDurationMs;
    }

    public void setAnalysisDurationMs(Long analysisDurationMs) {
        this.analysisDurationMs = analysisDurationMs;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public Integer getComplexityScore() {
        return complexityScore;
    }

    public void setComplexityScore(Integer complexityScore) {
        this.complexityScore = complexityScore;
    }

    public Boolean getHasCriticalInteractions() {
        return hasCriticalInteractions;
    }

    public void setHasCriticalInteractions(Boolean hasCriticalInteractions) {
        this.hasCriticalInteractions = hasCriticalInteractions;
    }

    public Boolean getHasEmergencyWarnings() {
        return hasEmergencyWarnings;
    }

    public void setHasEmergencyWarnings(Boolean hasEmergencyWarnings) {
        this.hasEmergencyWarnings = hasEmergencyWarnings;
    }

    public Boolean getRequiresImmediateAttention() {
        return requiresImmediateAttention;
    }

    public void setRequiresImmediateAttention(Boolean requiresImmediateAttention) {
        this.requiresImmediateAttention = requiresImmediateAttention;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getAnalysisTimestamp() {
        return analysisTimestamp;
    }

    public void setAnalysisTimestamp(LocalDateTime analysisTimestamp) {
        this.analysisTimestamp = analysisTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(Boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public Integer getUserFeedbackRating() {
        return userFeedbackRating;
    }

    public void setUserFeedbackRating(Integer userFeedbackRating) {
        this.userFeedbackRating = userFeedbackRating;
    }

    public String getUserFeedbackComments() {
        return userFeedbackComments;
    }

    public void setUserFeedbackComments(String userFeedbackComments) {
        this.userFeedbackComments = userFeedbackComments;
    }

    public Boolean getIsAccurate() {
        return isAccurate;
    }

    public void setIsAccurate(Boolean isAccurate) {
        this.isAccurate = isAccurate;
    }

    public Boolean getIsHelpful() {
        return isHelpful;
    }

    public void setIsHelpful(Boolean isHelpful) {
        this.isHelpful = isHelpful;
    }

    // ================================
    // Utility Methods
    // ================================

    /**
     * Check if this is a high-risk analysis
     */
    public boolean isHighRisk() {
        return "HIGH".equals(riskLevel) || "CRITICAL".equals(riskLevel) ||
               Boolean.TRUE.equals(hasCriticalInteractions) ||
               Boolean.TRUE.equals(hasEmergencyWarnings);
    }

    /**
     * Check if review is needed
     */
    public boolean needsReview() {
        return isHighRisk() && !Boolean.TRUE.equals(isReviewed);
    }

    /**
     * Mark as reviewed
     */
    public void markAsReviewed(String reviewerEmail, String notes) {
        this.isReviewed = true;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = reviewerEmail;
        this.reviewNotes = notes;
    }

    /**
     * Set user feedback
     */
    public void setUserFeedback(Integer rating, String comments, Boolean accurate, Boolean helpful) {
        this.userFeedbackRating = rating;
        this.userFeedbackComments = comments;
        this.isAccurate = accurate;
        this.isHelpful = helpful;
    }

    /**
     * Get summary for logging
     */
    public String getSummary() {
        return String.format("DrugInteraction[id=%d, userId=%d, medications=%d, riskLevel=%s, critical=%s]",
                id, userId, medicationCount, riskLevel, hasCriticalInteractions);
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "DrugInteraction{" +
                "id=" + id +
                ", userId=" + userId +
                ", medicationCount=" + medicationCount +
                ", riskLevel='" + riskLevel + '\'' +
                ", hasCriticalInteractions=" + hasCriticalInteractions +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugInteraction that = (DrugInteraction) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}