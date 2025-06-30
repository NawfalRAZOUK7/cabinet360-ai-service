package com.cabinet360.ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 🩺 Clinical Decision Entity
 * Stores clinical decision support analysis results and history
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@Entity
@Table(name = "clinical_decisions", indexes = {
        @Index(name = "idx_clinical_decision_user_id", columnList = "user_id"),
        @Index(name = "idx_clinical_decision_created_at", columnList = "created_at"),
        @Index(name = "idx_clinical_decision_type", columnList = "decision_type"),
        @Index(name = "idx_clinical_decision_urgency", columnList = "urgency_level"),
        @Index(name = "idx_clinical_decision_specialty", columnList = "specialty")
})
public class ClinicalDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 100)
    @Column(name = "user_email", length = 100)
    private String userEmail;

    // Decision Request Information
    @NotNull
    @Size(min = 20, max = 3000)
    @Column(name = "clinical_scenario", nullable = false, length = 3000)
    private String clinicalScenario;

    @Column(name = "patient_data", columnDefinition = "TEXT")
    private String patientData;

    @Size(max = 50)
    @Column(name = "decision_type", length = 50)
    private String decisionType; // DIAGNOSIS, TREATMENT, MONITORING, REFERRAL, PREVENTION, PROGNOSIS

    @Size(max = 30)
    @Column(name = "evidence_level", length = 30)
    private String evidenceLevel; // HIGH, MODERATE, LOW, EXPERT_OPINION

    @Size(max = 30)
    @Column(name = "urgency_level", length = 30)
    private String urgencyLevel; // EMERGENCY, HIGH, MODERATE, LOW, ROUTINE

    @Size(max = 50)
    @Column(name = "specialty", length = 50)
    private String specialty;

    @Size(max = 50)
    @Column(name = "clinical_setting", length = 50)
    private String clinicalSetting; // INPATIENT, OUTPATIENT, EMERGENCY, ICU, SURGERY, CLINIC

    // Patient Demographics
    @Size(max = 20)
    @Column(name = "patient_age", length = 20)
    private String patientAge;

    @Size(max = 20)
    @Column(name = "patient_gender", length = 20)
    private String patientGender;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications;

    @Size(max = 500)
    @Column(name = "allergies", length = 500)
    private String allergies;

    @Column(name = "comorbidities", columnDefinition = "TEXT")
    private String comorbidities; // JSON array

    @Column(name = "contraindications", columnDefinition = "TEXT")
    private String contraindications; // JSON array

    @Column(name = "treatment_options", columnDefinition = "TEXT")
    private String treatmentOptions; // JSON array

    @Column(name = "available_tests", columnDefinition = "TEXT")
    private String availableTests; // JSON array

    // Patient Preferences and Context
    @Column(name = "patient_preferences", columnDefinition = "TEXT")
    private String patientPreferences;

    @Column(name = "treatment_goals", columnDefinition = "TEXT")
    private String treatmentGoals;

    @Column(name = "social_factors", columnDefinition = "TEXT")
    private String socialFactors;

    @Column(name = "economic_factors", columnDefinition = "TEXT")
    private String economicFactors;

    @Column(name = "previous_treatments", columnDefinition = "TEXT")
    private String previousTreatments;

    // Analysis Configuration
    @Size(max = 30)
    @Column(name = "analysis_depth", length = 30)
    private String analysisDepth; // BASIC, STANDARD, COMPREHENSIVE, EXPERT

    @Column(name = "include_alternatives")
    private Boolean includeAlternatives = true;

    @Column(name = "include_cost_considerations")
    private Boolean includeCostConsiderations = false;

    @Column(name = "include_risk_assessment")
    private Boolean includeRiskAssessment = true;

    @Column(name = "include_monitoring_plan")
    private Boolean includeMonitoringPlan = true;

    @Column(name = "preferred_guidelines", columnDefinition = "TEXT")
    private String preferredGuidelines; // JSON array

    @Column(name = "specific_questions", columnDefinition = "TEXT")
    private String specificQuestions;

    @Column(name = "additional_context", columnDefinition = "TEXT")
    private String additionalContext;

    // Analysis Results
    @Column(name = "primary_recommendation", columnDefinition = "TEXT")
    private String primaryRecommendation;

    @Column(name = "recommendation_options", columnDefinition = "TEXT")
    private String recommendationOptions; // JSON array of recommendation objects

    @Column(name = "clinical_reasoning", columnDefinition = "TEXT")
    private String clinicalReasoning;

    @Column(name = "evidence_summary", columnDefinition = "TEXT")
    private String evidenceSummary;

    @Column(name = "evidence_sources", columnDefinition = "TEXT")
    private String evidenceSources; // JSON array of evidence source objects

    @Column(name = "risk_benefit_analysis", columnDefinition = "TEXT")
    private String riskBenefitAnalysis;

    @Column(name = "result_contraindications", columnDefinition = "TEXT")
    private String resultContraindications; // JSON array

    @Column(name = "precautions", columnDefinition = "TEXT")
    private String precautions; // JSON array

    @Column(name = "monitoring_parameters", columnDefinition = "TEXT")
    private String monitoringParameters; // JSON array

    @Column(name = "monitoring_plan", columnDefinition = "TEXT")
    private String monitoringPlan; // JSON object

    @Column(name = "follow_up_recommendations", columnDefinition = "TEXT")
    private String followUpRecommendations;

    @Column(name = "alternative_approaches", columnDefinition = "TEXT")
    private String alternativeApproaches; // JSON array

    @Column(name = "cost_analysis", columnDefinition = "TEXT")
    private String costAnalysis; // JSON object

    @Column(name = "patient_factors", columnDefinition = "TEXT")
    private String patientFactors; // JSON object

    @Column(name = "key_decision_factors", columnDefinition = "TEXT")
    private String keyDecisionFactors; // JSON array

    @Column(name = "patient_education", columnDefinition = "TEXT")
    private String patientEducation;

    @Column(name = "provider_guidance", columnDefinition = "TEXT")
    private String providerGuidance;

    @Column(name = "quality_measures", columnDefinition = "TEXT")
    private String qualityMeasures;

    @Column(name = "safety_considerations", columnDefinition = "TEXT")
    private String safetyConsiderations;

    // AI Analysis Metadata
    @Size(max = 30)
    @Column(name = "confidence_level", length = 30)
    private String confidenceLevel; // HIGH, MODERATE, LOW

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

    @Column(name = "complexity_score")
    private Integer complexityScore; // 0 to 10

    @Column(name = "decision_confidence_score")
    private Double decisionConfidenceScore; // 0.0 to 1.0

    // Decision Characteristics
    @Column(name = "is_emergency_decision")
    private Boolean isEmergencyDecision = false;

    @Column(name = "has_contraindications")
    private Boolean hasContraindications = false;

    @Column(name = "requires_monitoring")
    private Boolean requiresMonitoring = false;

    @Column(name = "requires_specialist_referral")
    private Boolean requiresSpecialistReferral = false;

    @Column(name = "is_high_complexity")
    private Boolean isHighComplexity = false;

    @Column(name = "has_multiple_options")
    private Boolean hasMultipleOptions = false;

    // Implementation Tracking
    @Column(name = "is_implemented")
    private Boolean isImplemented = false;

    @Column(name = "implementation_date")
    private LocalDateTime implementationDate;

    @Size(max = 100)
    @Column(name = "implemented_by", length = 100)
    private String implementedBy;

    @Column(name = "implementation_notes", columnDefinition = "TEXT")
    private String implementationNotes;

    @Column(name = "outcome_tracked")
    private Boolean outcomeTracked = false;

    @Column(name = "outcome_date")
    private LocalDateTime outcomeDate;

    @Size(max = 500)
    @Column(name = "outcome_description", length = 500)
    private String outcomeDescription;

    @Size(max = 30)
    @Column(name = "outcome_status", length = 30)
    private String outcomeStatus; // SUCCESS, PARTIAL_SUCCESS, FAILURE, PENDING

    // Quality and Feedback
    @Column(name = "user_feedback_rating")
    private Integer userFeedbackRating; // 1-5 stars

    @Column(name = "user_feedback_comments", columnDefinition = "TEXT")
    private String userFeedbackComments;

    @Column(name = "is_accurate")
    private Boolean isAccurate;

    @Column(name = "is_helpful")
    private Boolean isHelpful;

    @Column(name = "is_actionable")
    private Boolean isActionable;

    @Column(name = "is_evidence_based")
    private Boolean isEvidenceBased;

    @Column(name = "peer_review_requested")
    private Boolean peerReviewRequested = false;

    @Column(name = "peer_reviewed")
    private Boolean peerReviewed = false;

    @Column(name = "peer_review_date")
    private LocalDateTime peerReviewDate;

    @Size(max = 100)
    @Column(name = "peer_reviewer", length = 100)
    private String peerReviewer;

    @Column(name = "peer_review_notes", columnDefinition = "TEXT")
    private String peerReviewNotes;

    @Column(name = "peer_review_agreement")
    private Boolean peerReviewAgreement;

    // Metadata and Additional Information
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON object for additional data

    @Size(max = 1000)
    @Column(name = "disclaimer", length = 1000)
    private String disclaimer;

    @Column(name = "referenced_guidelines", columnDefinition = "TEXT")
    private String referencedGuidelines; // JSON array

    @Column(name = "literature_references", columnDefinition = "TEXT")
    private String literatureReferences; // JSON array

    // Audit and Tracking
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "decision_timestamp")
    private LocalDateTime decisionTimestamp;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @Size(max = 100)
    @Column(name = "archived_by", length = 100)
    private String archivedBy;

    @Size(max = 500)
    @Column(name = "archive_reason", length = 500)
    private String archiveReason;

    // Learning and Improvement
    @Column(name = "used_for_training")
    private Boolean usedForTraining = false;

    @Column(name = "anonymized_for_research")
    private Boolean anonymizedForResearch = false;

    @Column(name = "contributes_to_learning")
    private Boolean contributesToLearning = true;

    @Column(name = "flagged_for_review")
    private Boolean flaggedForReview = false;

    @Column(name = "flag_reason", length = 500)
    private String flagReason;

    @Column(name = "review_priority")
    private Integer reviewPriority; // 1-5, 5 being highest priority

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public ClinicalDecision() {
        this.decisionTimestamp = LocalDateTime.now();
        this.analysisVersion = "1.0";
        this.confidenceLevel = "MODERATE";
        this.isActive = true;
        this.isArchived = false;
        this.isEmergencyDecision = false;
        this.hasContraindications = false;
        this.requiresMonitoring = false;
        this.requiresSpecialistReferral = false;
        this.isHighComplexity = false;
        this.hasMultipleOptions = false;
        this.isImplemented = false;
        this.outcomeTracked = false;
        this.peerReviewRequested = false;
        this.peerReviewed = false;
        this.usedForTraining = false;
        this.anonymizedForResearch = false;
        this.contributesToLearning = true;
        this.flaggedForReview = false;
        this.includeAlternatives = true;
        this.includeCostConsiderations = false;
        this.includeRiskAssessment = true;
        this.includeMonitoringPlan = true;
    }

    /**
     * Constructor with basic information
     */
    public ClinicalDecision(Long userId, String userEmail, String clinicalScenario, String decisionType) {
        this();
        this.userId = userId;
        this.userEmail = userEmail;
        this.clinicalScenario = clinicalScenario;
        this.decisionType = decisionType;
        this.createdBy = userEmail;
    }

    /**
     * Constructor with patient data
     */
    public ClinicalDecision(Long userId, String userEmail, String clinicalScenario,
                            String decisionType, String patientData, String urgencyLevel) {
        this(userId, userEmail, clinicalScenario, decisionType);
        this.patientData = patientData;
        this.urgencyLevel = urgencyLevel;
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

    public String getClinicalScenario() {
        return clinicalScenario;
    }

    public void setClinicalScenario(String clinicalScenario) {
        this.clinicalScenario = clinicalScenario;
    }

    public String getPatientData() {
        return patientData;
    }

    public void setPatientData(String patientData) {
        this.patientData = patientData;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public String getEvidenceLevel() {
        return evidenceLevel;
    }

    public void setEvidenceLevel(String evidenceLevel) {
        this.evidenceLevel = evidenceLevel;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getClinicalSetting() {
        return clinicalSetting;
    }

    public void setClinicalSetting(String clinicalSetting) {
        this.clinicalSetting = clinicalSetting;
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

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getCurrentMedications() {
        return currentMedications;
    }

    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getComorbidities() {
        return comorbidities;
    }

    public void setComorbidities(String comorbidities) {
        this.comorbidities = comorbidities;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getTreatmentOptions() {
        return treatmentOptions;
    }

    public void setTreatmentOptions(String treatmentOptions) {
        this.treatmentOptions = treatmentOptions;
    }

    public String getAvailableTests() {
        return availableTests;
    }

    public void setAvailableTests(String availableTests) {
        this.availableTests = availableTests;
    }

    public String getPatientPreferences() {
        return patientPreferences;
    }

    public void setPatientPreferences(String patientPreferences) {
        this.patientPreferences = patientPreferences;
    }

    public String getTreatmentGoals() {
        return treatmentGoals;
    }

    public void setTreatmentGoals(String treatmentGoals) {
        this.treatmentGoals = treatmentGoals;
    }

    public String getSocialFactors() {
        return socialFactors;
    }

    public void setSocialFactors(String socialFactors) {
        this.socialFactors = socialFactors;
    }

    public String getEconomicFactors() {
        return economicFactors;
    }

    public void setEconomicFactors(String economicFactors) {
        this.economicFactors = economicFactors;
    }

    public String getPreviousTreatments() {
        return previousTreatments;
    }

    public void setPreviousTreatments(String previousTreatments) {
        this.previousTreatments = previousTreatments;
    }

    public String getAnalysisDepth() {
        return analysisDepth;
    }

    public void setAnalysisDepth(String analysisDepth) {
        this.analysisDepth = analysisDepth;
    }

    public Boolean getIncludeAlternatives() {
        return includeAlternatives;
    }

    public void setIncludeAlternatives(Boolean includeAlternatives) {
        this.includeAlternatives = includeAlternatives;
    }

    public Boolean getIncludeCostConsiderations() {
        return includeCostConsiderations;
    }

    public void setIncludeCostConsiderations(Boolean includeCostConsiderations) {
        this.includeCostConsiderations = includeCostConsiderations;
    }

    public Boolean getIncludeRiskAssessment() {
        return includeRiskAssessment;
    }

    public void setIncludeRiskAssessment(Boolean includeRiskAssessment) {
        this.includeRiskAssessment = includeRiskAssessment;
    }

    public Boolean getIncludeMonitoringPlan() {
        return includeMonitoringPlan;
    }

    public void setIncludeMonitoringPlan(Boolean includeMonitoringPlan) {
        this.includeMonitoringPlan = includeMonitoringPlan;
    }

    public String getPreferredGuidelines() {
        return preferredGuidelines;
    }

    public void setPreferredGuidelines(String preferredGuidelines) {
        this.preferredGuidelines = preferredGuidelines;
    }

    public String getSpecificQuestions() {
        return specificQuestions;
    }

    public void setSpecificQuestions(String specificQuestions) {
        this.specificQuestions = specificQuestions;
    }

    public String getAdditionalContext() {
        return additionalContext;
    }

    public void setAdditionalContext(String additionalContext) {
        this.additionalContext = additionalContext;
    }

    public String getPrimaryRecommendation() {
        return primaryRecommendation;
    }

    public void setPrimaryRecommendation(String primaryRecommendation) {
        this.primaryRecommendation = primaryRecommendation;
    }

    public String getRecommendationOptions() {
        return recommendationOptions;
    }

    public void setRecommendationOptions(String recommendationOptions) {
        this.recommendationOptions = recommendationOptions;
    }

    public String getClinicalReasoning() {
        return clinicalReasoning;
    }

    public void setClinicalReasoning(String clinicalReasoning) {
        this.clinicalReasoning = clinicalReasoning;
    }

    public String getEvidenceSummary() {
        return evidenceSummary;
    }

    public void setEvidenceSummary(String evidenceSummary) {
        this.evidenceSummary = evidenceSummary;
    }

    public String getEvidenceSources() {
        return evidenceSources;
    }

    public void setEvidenceSources(String evidenceSources) {
        this.evidenceSources = evidenceSources;
    }

    public String getRiskBenefitAnalysis() {
        return riskBenefitAnalysis;
    }

    public void setRiskBenefitAnalysis(String riskBenefitAnalysis) {
        this.riskBenefitAnalysis = riskBenefitAnalysis;
    }

    public String getResultContraindications() {
        return resultContraindications;
    }

    public void setResultContraindications(String resultContraindications) {
        this.resultContraindications = resultContraindications;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getMonitoringParameters() {
        return monitoringParameters;
    }

    public void setMonitoringParameters(String monitoringParameters) {
        this.monitoringParameters = monitoringParameters;
    }

    public String getMonitoringPlan() {
        return monitoringPlan;
    }

    public void setMonitoringPlan(String monitoringPlan) {
        this.monitoringPlan = monitoringPlan;
    }

    public String getFollowUpRecommendations() {
        return followUpRecommendations;
    }

    public void setFollowUpRecommendations(String followUpRecommendations) {
        this.followUpRecommendations = followUpRecommendations;
    }

    public String getAlternativeApproaches() {
        return alternativeApproaches;
    }

    public void setAlternativeApproaches(String alternativeApproaches) {
        this.alternativeApproaches = alternativeApproaches;
    }

    public String getCostAnalysis() {
        return costAnalysis;
    }

    public void setCostAnalysis(String costAnalysis) {
        this.costAnalysis = costAnalysis;
    }

    public String getPatientFactors() {
        return patientFactors;
    }

    public void setPatientFactors(String patientFactors) {
        this.patientFactors = patientFactors;
    }

    public String getKeyDecisionFactors() {
        return keyDecisionFactors;
    }

    public void setKeyDecisionFactors(String keyDecisionFactors) {
        this.keyDecisionFactors = keyDecisionFactors;
    }

    public String getPatientEducation() {
        return patientEducation;
    }

    public void setPatientEducation(String patientEducation) {
        this.patientEducation = patientEducation;
    }

    public String getProviderGuidance() {
        return providerGuidance;
    }

    public void setProviderGuidance(String providerGuidance) {
        this.providerGuidance = providerGuidance;
    }

    public String getQualityMeasures() {
        return qualityMeasures;
    }

    public void setQualityMeasures(String qualityMeasures) {
        this.qualityMeasures = qualityMeasures;
    }

    public String getSafetyConsiderations() {
        return safetyConsiderations;
    }

    public void setSafetyConsiderations(String safetyConsiderations) {
        this.safetyConsiderations = safetyConsiderations;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
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

    public Integer getComplexityScore() {
        return complexityScore;
    }

    public void setComplexityScore(Integer complexityScore) {
        this.complexityScore = complexityScore;
    }

    public Double getDecisionConfidenceScore() {
        return decisionConfidenceScore;
    }

    public void setDecisionConfidenceScore(Double decisionConfidenceScore) {
        this.decisionConfidenceScore = decisionConfidenceScore;
    }

    public Boolean getIsEmergencyDecision() {
        return isEmergencyDecision;
    }

    public void setIsEmergencyDecision(Boolean isEmergencyDecision) {
        this.isEmergencyDecision = isEmergencyDecision;
    }

    public Boolean getHasContraindications() {
        return hasContraindications;
    }

    public void setHasContraindications(Boolean hasContraindications) {
        this.hasContraindications = hasContraindications;
    }

    public Boolean getRequiresMonitoring() {
        return requiresMonitoring;
    }

    public void setRequiresMonitoring(Boolean requiresMonitoring) {
        this.requiresMonitoring = requiresMonitoring;
    }

    public Boolean getRequiresSpecialistReferral() {
        return requiresSpecialistReferral;
    }

    public void setRequiresSpecialistReferral(Boolean requiresSpecialistReferral) {
        this.requiresSpecialistReferral = requiresSpecialistReferral;
    }

    public Boolean getIsHighComplexity() {
        return isHighComplexity;
    }

    public void setIsHighComplexity(Boolean isHighComplexity) {
        this.isHighComplexity = isHighComplexity;
    }

    public Boolean getHasMultipleOptions() {
        return hasMultipleOptions;
    }

    public void setHasMultipleOptions(Boolean hasMultipleOptions) {
        this.hasMultipleOptions = hasMultipleOptions;
    }

    // Implementation tracking getters/setters
    public Boolean getIsImplemented() {
        return isImplemented;
    }

    public void setIsImplemented(Boolean isImplemented) {
        this.isImplemented = isImplemented;
    }

    public LocalDateTime getImplementationDate() {
        return implementationDate;
    }

    public void setImplementationDate(LocalDateTime implementationDate) {
        this.implementationDate = implementationDate;
    }

    public String getImplementedBy() {
        return implementedBy;
    }

    public void setImplementedBy(String implementedBy) {
        this.implementedBy = implementedBy;
    }

    public String getImplementationNotes() {
        return implementationNotes;
    }

    public void setImplementationNotes(String implementationNotes) {
        this.implementationNotes = implementationNotes;
    }

    public Boolean getOutcomeTracked() {
        return outcomeTracked;
    }

    public void setOutcomeTracked(Boolean outcomeTracked) {
        this.outcomeTracked = outcomeTracked;
    }

    public LocalDateTime getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(LocalDateTime outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getOutcomeDescription() {
        return outcomeDescription;
    }

    public void setOutcomeDescription(String outcomeDescription) {
        this.outcomeDescription = outcomeDescription;
    }

    public String getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(String outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    // Quality and feedback getters/setters
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

    public Boolean getIsActionable() {
        return isActionable;
    }

    public void setIsActionable(Boolean isActionable) {
        this.isActionable = isActionable;
    }

    public Boolean getIsEvidenceBased() {
        return isEvidenceBased;
    }

    public void setIsEvidenceBased(Boolean isEvidenceBased) {
        this.isEvidenceBased = isEvidenceBased;
    }

    public Boolean getPeerReviewRequested() {
        return peerReviewRequested;
    }

    public void setPeerReviewRequested(Boolean peerReviewRequested) {
        this.peerReviewRequested = peerReviewRequested;
    }

    public Boolean getPeerReviewed() {
        return peerReviewed;
    }

    public void setPeerReviewed(Boolean peerReviewed) {
        this.peerReviewed = peerReviewed;
    }

    public LocalDateTime getPeerReviewDate() {
        return peerReviewDate;
    }

    public void setPeerReviewDate(LocalDateTime peerReviewDate) {
        this.peerReviewDate = peerReviewDate;
    }

    public String getPeerReviewer() {
        return peerReviewer;
    }

    public void setPeerReviewer(String peerReviewer) {
        this.peerReviewer = peerReviewer;
    }

    public String getPeerReviewNotes() {
        return peerReviewNotes;
    }

    public void setPeerReviewNotes(String peerReviewNotes) {
        this.peerReviewNotes = peerReviewNotes;
    }

    public Boolean getPeerReviewAgreement() {
        return peerReviewAgreement;
    }

    public void setPeerReviewAgreement(Boolean peerReviewAgreement) {
        this.peerReviewAgreement = peerReviewAgreement;
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

    public String getReferencedGuidelines() {
        return referencedGuidelines;
    }

    public void setReferencedGuidelines(String referencedGuidelines) {
        this.referencedGuidelines = referencedGuidelines;
    }

    public String getLiteratureReferences() {
        return literatureReferences;
    }

    public void setLiteratureReferences(String literatureReferences) {
        this.literatureReferences = literatureReferences;
    }

    // Audit getters/setters
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

    public LocalDateTime getDecisionTimestamp() {
        return decisionTimestamp;
    }

    public void setDecisionTimestamp(LocalDateTime decisionTimestamp) {
        this.decisionTimestamp = decisionTimestamp;
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

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public String getArchivedBy() {
        return archivedBy;
    }

    public void setArchivedBy(String archivedBy) {
        this.archivedBy = archivedBy;
    }

    public String getArchiveReason() {
        return archiveReason;
    }

    public void setArchiveReason(String archiveReason) {
        this.archiveReason = archiveReason;
    }

    // Learning getters/setters
    public Boolean getUsedForTraining() {
        return usedForTraining;
    }

    public void setUsedForTraining(Boolean usedForTraining) {
        this.usedForTraining = usedForTraining;
    }

    public Boolean getAnonymizedForResearch() {
        return anonymizedForResearch;
    }

    public void setAnonymizedForResearch(Boolean anonymizedForResearch) {
        this.anonymizedForResearch = anonymizedForResearch;
    }

    public Boolean getContributesToLearning() {
        return contributesToLearning;
    }

    public void setContributesToLearning(Boolean contributesToLearning) {
        this.contributesToLearning = contributesToLearning;
    }

    public Boolean getFlaggedForReview() {
        return flaggedForReview;
    }

    public void setFlaggedForReview(Boolean flaggedForReview) {
        this.flaggedForReview = flaggedForReview;
    }

    public String getFlagReason() {
        return flagReason;
    }

    public void setFlagReason(String flagReason) {
        this.flagReason = flagReason;
    }

    public Integer getReviewPriority() {
        return reviewPriority;
    }

    public void setReviewPriority(Integer reviewPriority) {
        this.reviewPriority = reviewPriority;
    }

    // ================================
    // Utility Methods
    // ================================

    /**
     * Check if this is a high-priority decision
     */
    public boolean isHighPriority() {
        return Boolean.TRUE.equals(isEmergencyDecision) ||
                "EMERGENCY".equals(urgencyLevel) ||
                "HIGH".equals(urgencyLevel) ||
                Boolean.TRUE.equals(isHighComplexity);
    }

    /**
     * Check if peer review is needed
     */
    public boolean needsPeerReview() {
        return (isHighPriority() || Boolean.TRUE.equals(peerReviewRequested)) &&
                !Boolean.TRUE.equals(peerReviewed);
    }

    /**
     * Mark as implemented
     */
    public void markAsImplemented(String implementerEmail, String notes) {
        this.isImplemented = true;
        this.implementationDate = LocalDateTime.now();
        this.implementedBy = implementerEmail;
        this.implementationNotes = notes;
    }

    /**
     * Record outcome
     */
    public void recordOutcome(String status, String description) {
        this.outcomeTracked = true;
        this.outcomeDate = LocalDateTime.now();
        this.outcomeStatus = status;
        this.outcomeDescription = description;
    }

    /**
     * Request peer review
     */
    public void requestPeerReview(String reason, Integer priority) {
        this.peerReviewRequested = true;
        this.flaggedForReview = true;
        this.flagReason = reason;
        this.reviewPriority = priority;
    }

    /**
     * Complete peer review
     */
    public void completePeerReview(String reviewerEmail, String notes, Boolean agreement) {
        this.peerReviewed = true;
        this.peerReviewDate = LocalDateTime.now();
        this.peerReviewer = reviewerEmail;
        this.peerReviewNotes = notes;
        this.peerReviewAgreement = agreement;
        this.flaggedForReview = false;
    }

    /**
     * Set user feedback
     */
    public void setUserFeedback(Integer rating, String comments, Boolean accurate,
                                Boolean helpful, Boolean actionable, Boolean evidenceBased) {
        this.userFeedbackRating = rating;
        this.userFeedbackComments = comments;
        this.isAccurate = accurate;
        this.isHelpful = helpful;
        this.isActionable = actionable;
        this.isEvidenceBased = evidenceBased;
    }

    /**
     * Archive the decision
     */
    public void archive(String archiverEmail, String reason) {
        this.isArchived = true;
        this.archivedAt = LocalDateTime.now();
        this.archivedBy = archiverEmail;
        this.archiveReason = reason;
        this.isActive = false;
    }

    /**
     * Flag for review
     */
    public void flagForReview(String reason, Integer priority) {
        this.flaggedForReview = true;
        this.flagReason = reason;
        this.reviewPriority = priority;
    }

    /**
     * Check if decision is complex
     */
    public boolean isComplexDecision() {
        return Boolean.TRUE.equals(isHighComplexity) ||
                (complexityScore != null && complexityScore > 7) ||
                Boolean.TRUE.equals(hasMultipleOptions) ||
                Boolean.TRUE.equals(hasContraindications);
    }

    /**
     * Check if requires immediate attention
     */
    public boolean requiresImmediateAttention() {
        return Boolean.TRUE.equals(isEmergencyDecision) ||
                "EMERGENCY".equals(urgencyLevel) ||
                Boolean.TRUE.equals(requiresSpecialistReferral);
    }

    /**
     * Get decision quality score (0-100)
     */
    public Integer getQualityScore() {
        if (userFeedbackRating == null) {
            return null;
        }

        int score = userFeedbackRating * 20; // Convert 1-5 to 20-100

        // Adjust based on other quality indicators
        if (Boolean.TRUE.equals(isAccurate)) score += 5;
        if (Boolean.TRUE.equals(isHelpful)) score += 5;
        if (Boolean.TRUE.equals(isActionable)) score += 5;
        if (Boolean.TRUE.equals(isEvidenceBased)) score += 5;

        // Cap at 100
        return Math.min(score, 100);
    }

    /**
     * Check if eligible for training data
     */
    public boolean isEligibleForTraining() {
        return Boolean.TRUE.equals(contributesToLearning) &&
                !Boolean.TRUE.equals(flaggedForReview) &&
                Boolean.TRUE.equals(isActive) &&
                !Boolean.TRUE.equals(isArchived) &&
                (userFeedbackRating == null || userFeedbackRating >= 3);
    }

    /**
     * Get summary for logging
     */
    public String getSummary() {
        return String.format("ClinicalDecision[id=%d, userId=%d, type=%s, urgency=%s, emergency=%s, confidence=%s]",
                id, userId, decisionType, urgencyLevel, isEmergencyDecision, confidenceLevel);
    }

    /**
     * Get complexity description
     */
    public String getComplexityDescription() {
        if (complexityScore == null) {
            return "Unknown";
        } else if (complexityScore <= 3) {
            return "Low";
        } else if (complexityScore <= 6) {
            return "Moderate";
        } else if (complexityScore <= 8) {
            return "High";
        } else {
            return "Very High";
        }
    }

    /**
     * Get urgency description with emoji
     */
    public String getUrgencyDescription() {
        switch (urgencyLevel != null ? urgencyLevel : "MODERATE") {
            case "EMERGENCY":
                return "🚨 Emergency";
            case "HIGH":
                return "⚠️ High";
            case "MODERATE":
                return "📋 Moderate";
            case "LOW":
                return "📝 Low";
            case "ROUTINE":
                return "📅 Routine";
            default:
                return "📋 Moderate";
        }
    }

    /**
     * Get decision type description
     */
    public String getDecisionTypeDescription() {
        switch (decisionType != null ? decisionType : "TREATMENT") {
            case "DIAGNOSIS":
                return "🎯 Diagnosis";
            case "TREATMENT":
                return "💊 Treatment";
            case "MONITORING":
                return "📊 Monitoring";
            case "REFERRAL":
                return "👨‍⚕️ Referral";
            case "PREVENTION":
                return "🛡️ Prevention";
            case "PROGNOSIS":
                return "📈 Prognosis";
            case "INVESTIGATION":
                return "🔬 Investigation";
            default:
                return "🩺 Clinical Decision";
        }
    }

    /**
     * Check if decision has positive outcome
     */
    public boolean hasPositiveOutcome() {
        return "SUCCESS".equals(outcomeStatus) ||
                "PARTIAL_SUCCESS".equals(outcomeStatus);
    }

    /**
     * Get days since created
     */
    public long getDaysSinceCreated() {
        if (createdAt == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(createdAt.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    /**
     * Get days since last update
     */
    public long getDaysSinceUpdate() {
        if (updatedAt == null) {
            return getDaysSinceCreated();
        }
        return java.time.temporal.ChronoUnit.DAYS.between(updatedAt.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    /**
     * Check if decision is stale (no updates in 30 days)
     */
    public boolean isStale() {
        return getDaysSinceUpdate() > 30;
    }

    /**
     * Get implementation status description
     */
    public String getImplementationStatus() {
        if (Boolean.TRUE.equals(isImplemented)) {
            if (Boolean.TRUE.equals(outcomeTracked)) {
                return hasPositiveOutcome() ? "✅ Implemented Successfully" : "⚠️ Implemented with Issues";
            } else {
                return "🔄 Implemented - Outcome Pending";
            }
        } else {
            return "⏳ Not Yet Implemented";
        }
    }

    /**
     * Get confidence level with emoji
     */
    public String getConfidenceLevelDescription() {
        switch (confidenceLevel != null ? confidenceLevel : "MODERATE") {
            case "HIGH":
                return "🟢 High Confidence";
            case "MODERATE":
                return "🟡 Moderate Confidence";
            case "LOW":
                return "🔴 Low Confidence";
            default:
                return "🟡 Moderate Confidence";
        }
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "ClinicalDecision{" +
                "id=" + id +
                ", userId=" + userId +
                ", decisionType='" + decisionType + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", confidenceLevel='" + confidenceLevel + '\'' +
                ", isEmergencyDecision=" + isEmergencyDecision +
                ", complexityScore=" + complexityScore +
                ", isImplemented=" + isImplemented +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalDecision that = (ClinicalDecision) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // ================================
    // JPA Lifecycle Methods
    // ================================

    /**
     * Pre-persist lifecycle method
     */
    @PrePersist
    protected void onCreate() {
        if (decisionTimestamp == null) {
            decisionTimestamp = LocalDateTime.now();
        }
        if (analysisVersion == null) {
            analysisVersion = "1.0";
        }
        if (confidenceLevel == null) {
            confidenceLevel = "MODERATE";
        }

        // Set complexity flags based on data
        updateComplexityFlags();
    }

    /**
     * Pre-update lifecycle method
     */
    @PreUpdate
    protected void onUpdate() {
        updateComplexityFlags();
    }

    /**
     * Update complexity flags based on current data
     */
    private void updateComplexityFlags() {
        // Check if has contraindications
        this.hasContraindications = (contraindications != null && !contraindications.trim().isEmpty()) ||
                (resultContraindications != null && !resultContraindications.trim().isEmpty());

        // Check if requires monitoring
        this.requiresMonitoring = (monitoringParameters != null && !monitoringParameters.trim().isEmpty()) ||
                (monitoringPlan != null && !monitoringPlan.trim().isEmpty());

        // Check if has multiple options
        this.hasMultipleOptions = (recommendationOptions != null && !recommendationOptions.trim().isEmpty()) ||
                (alternativeApproaches != null && !alternativeApproaches.trim().isEmpty());

        // Determine if high complexity
        int complexityFactors = 0;
        if (Boolean.TRUE.equals(hasContraindications)) complexityFactors++;
        if (Boolean.TRUE.equals(requiresMonitoring)) complexityFactors++;
        if (Boolean.TRUE.equals(hasMultipleOptions)) complexityFactors++;
        if (Boolean.TRUE.equals(isEmergencyDecision)) complexityFactors++;
        if (complexityScore != null && complexityScore > 7) complexityFactors++;

        this.isHighComplexity = complexityFactors >= 3;

        // Check emergency status
        this.isEmergencyDecision = "EMERGENCY".equals(urgencyLevel) ||
                Boolean.TRUE.equals(isEmergencyDecision);
    }
}