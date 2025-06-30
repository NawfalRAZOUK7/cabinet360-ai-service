package com.cabinet360.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * 🩺 Clinical Decision Support Response DTO
 * Contains AI-generated clinical decision support with evidence-based recommendations
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinicalDecisionResponse {

    private String decisionType; // DIAGNOSIS, TREATMENT, MONITORING, REFERRAL, PREVENTION
    private String primaryRecommendation;
    private List<RecommendationOption> recommendationOptions;
    private String clinicalReasoning;
    private String evidenceSummary;
    private List<EvidenceSource> evidenceSources;
    private String riskBenefitAnalysis;
    private List<String> contraindications;
    private List<String> precautions;
    private List<String> monitoringParameters;
    private MonitoringPlan monitoringPlan;
    private String followUpRecommendations;
    private List<String> alternativeApproaches;
    private CostEffectivenessAnalysis costAnalysis;
    private PatientFactors patientConsiderations;
    private String urgencyLevel; // ROUTINE, MODERATE, HIGH, EMERGENCY
    private String confidenceLevel; // HIGH, MODERATE, LOW
    private List<String> keyDecisionFactors;
    private String patientEducation;
    private String providerGuidance;
    private String qualityMeasures;
    private String safetyConsiderations;
    private String disclaimer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String analysisVersion;
    private DecisionSummary summary;
    private Map<String, Object> metadata;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public ClinicalDecisionResponse() {
        this.recommendationOptions = new ArrayList<>();
        this.evidenceSources = new ArrayList<>();
        this.contraindications = new ArrayList<>();
        this.precautions = new ArrayList<>();
        this.monitoringParameters = new ArrayList<>();
        this.alternativeApproaches = new ArrayList<>();
        this.keyDecisionFactors = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.timestamp = LocalDateTime.now();
        this.analysisVersion = "1.0";
        this.confidenceLevel = "MODERATE";
    }

    /**
     * Constructor with decision type and primary recommendation
     * @param decisionType Type of clinical decision
     * @param primaryRecommendation Main recommendation
     */
    public ClinicalDecisionResponse(@NotNull String decisionType, String primaryRecommendation) {
        this();
        this.decisionType = decisionType;
        this.primaryRecommendation = primaryRecommendation;
        this.generateSummary();
    }

    /**
     * Constructor with recommendations and reasoning
     * @param decisionType Type of decision
     * @param primaryRecommendation Main recommendation
     * @param clinicalReasoning Clinical reasoning
     * @param confidenceLevel AI confidence level
     */
    public ClinicalDecisionResponse(@NotNull String decisionType,
                                    String primaryRecommendation,
                                    String clinicalReasoning,
                                    String confidenceLevel) {
        this();
        this.decisionType = decisionType;
        this.primaryRecommendation = primaryRecommendation;
        this.clinicalReasoning = clinicalReasoning;
        this.confidenceLevel = confidenceLevel;
        this.generateSummary();
    }

    // ================================
    // Static Factory Methods
    // ================================

    /**
     * Create error response
     * @param message Error message
     * @return Error response
     */
    public static ClinicalDecisionResponse error(String message) {
        ClinicalDecisionResponse response = new ClinicalDecisionResponse();
        response.disclaimer = "Error: " + message;
        response.confidenceLevel = "LOW";
        response.primaryRecommendation = "Unable to provide clinical decision support due to: " + message;
        response.providerGuidance = "Please consult with clinical colleagues or specialists for guidance.";
        return response;
    }

    /**
     * Create emergency response
     * @param scenario Emergency scenario
     * @return Emergency clinical decision response
     */
    public static ClinicalDecisionResponse emergency(String scenario) {
        ClinicalDecisionResponse response = new ClinicalDecisionResponse();
        response.decisionType = "EMERGENCY";
        response.urgencyLevel = "EMERGENCY";
        response.confidenceLevel = "HIGH";
        response.primaryRecommendation = "🚨 IMMEDIATE EMERGENCY INTERVENTION REQUIRED 🚨";
        response.clinicalReasoning = "Emergency situation detected requiring immediate medical intervention.";
        response.providerGuidance = "Activate emergency protocols immediately. Do not delay care.";
        response.disclaimer = "This is a medical emergency requiring immediate intervention.";
        return response;
    }

    // ================================
    // Getters and Setters
    // ================================

    /**
     * Get decision type
     * @return Type of clinical decision being made
     */
    public String getDecisionType() {
        return decisionType;
    }

    /**
     * Set decision type
     * @param decisionType Type of clinical decision
     */
    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    /**
     * Get primary recommendation
     * @return Main clinical recommendation
     */
    public String getPrimaryRecommendation() {
        return primaryRecommendation;
    }

    /**
     * Set primary recommendation
     * @param primaryRecommendation Main clinical recommendation
     */
    public void setPrimaryRecommendation(String primaryRecommendation) {
        this.primaryRecommendation = primaryRecommendation;
    }

    /**
     * Get recommendation options
     * @return List of ranked recommendation options
     */
    public List<RecommendationOption> getRecommendationOptions() {
        return recommendationOptions;
    }

    /**
     * Set recommendation options
     * @param recommendationOptions Alternative recommendation options
     */
    public void setRecommendationOptions(List<RecommendationOption> recommendationOptions) {
        this.recommendationOptions = recommendationOptions != null ?
                new ArrayList<>(recommendationOptions) : new ArrayList<>();
    }

    /**
     * Get clinical reasoning
     * @return AI-generated clinical reasoning
     */
    public String getClinicalReasoning() {
        return clinicalReasoning;
    }

    /**
     * Set clinical reasoning
     * @param clinicalReasoning Clinical thought process
     */
    public void setClinicalReasoning(String clinicalReasoning) {
        this.clinicalReasoning = clinicalReasoning;
    }

    /**
     * Get evidence summary
     * @return Summary of supporting evidence
     */
    public String getEvidenceSummary() {
        return evidenceSummary;
    }

    /**
     * Set evidence summary
     * @param evidenceSummary Summary of evidence base
     */
    public void setEvidenceSummary(String evidenceSummary) {
        this.evidenceSummary = evidenceSummary;
    }

    /**
     * Get evidence sources
     * @return List of evidence sources
     */
    public List<EvidenceSource> getEvidenceSources() {
        return evidenceSources;
    }

    /**
     * Set evidence sources
     * @param evidenceSources Supporting evidence sources
     */
    public void setEvidenceSources(List<EvidenceSource> evidenceSources) {
        this.evidenceSources = evidenceSources != null ?
                new ArrayList<>(evidenceSources) : new ArrayList<>();
    }

    /**
     * Get risk-benefit analysis
     * @return Risk-benefit assessment
     */
    public String getRiskBenefitAnalysis() {
        return riskBenefitAnalysis;
    }

    /**
     * Set risk-benefit analysis
     * @param riskBenefitAnalysis Analysis of risks vs benefits
     */
    public void setRiskBenefitAnalysis(String riskBenefitAnalysis) {
        this.riskBenefitAnalysis = riskBenefitAnalysis;
    }

    /**
     * Get contraindications
     * @return List of contraindications
     */
    public List<String> getContraindications() {
        return contraindications;
    }

    /**
     * Set contraindications
     * @param contraindications Known contraindications
     */
    public void setContraindications(List<String> contraindications) {
        this.contraindications = contraindications != null ?
                new ArrayList<>(contraindications) : new ArrayList<>();
    }

    /**
     * Get precautions
     * @return List of clinical precautions
     */
    public List<String> getPrecautions() {
        return precautions;
    }

    /**
     * Set precautions
     * @param precautions Clinical precautions to observe
     */
    public void setPrecautions(List<String> precautions) {
        this.precautions = precautions != null ?
                new ArrayList<>(precautions) : new ArrayList<>();
    }

    /**
     * Get monitoring parameters
     * @return List of parameters to monitor
     */
    public List<String> getMonitoringParameters() {
        return monitoringParameters;
    }

    /**
     * Set monitoring parameters
     * @param monitoringParameters What to monitor
     */
    public void setMonitoringParameters(List<String> monitoringParameters) {
        this.monitoringParameters = monitoringParameters != null ?
                new ArrayList<>(monitoringParameters) : new ArrayList<>();
    }

    /**
     * Get monitoring plan
     * @return Detailed monitoring plan
     */
    public MonitoringPlan getMonitoringPlan() {
        return monitoringPlan;
    }

    /**
     * Set monitoring plan
     * @param monitoringPlan Structured monitoring plan
     */
    public void setMonitoringPlan(MonitoringPlan monitoringPlan) {
        this.monitoringPlan = monitoringPlan;
    }

    /**
     * Get follow-up recommendations
     * @return Follow-up care plan
     */
    public String getFollowUpRecommendations() {
        return followUpRecommendations;
    }

    /**
     * Set follow-up recommendations
     * @param followUpRecommendations Ongoing care recommendations
     */
    public void setFollowUpRecommendations(String followUpRecommendations) {
        this.followUpRecommendations = followUpRecommendations;
    }

    /**
     * Get alternative approaches
     * @return List of alternative treatment approaches
     */
    public List<String> getAlternativeApproaches() {
        return alternativeApproaches;
    }

    /**
     * Set alternative approaches
     * @param alternativeApproaches Alternative management options
     */
    public void setAlternativeApproaches(List<String> alternativeApproaches) {
        this.alternativeApproaches = alternativeApproaches != null ?
                new ArrayList<>(alternativeApproaches) : new ArrayList<>();
    }

    /**
     * Get cost analysis
     * @return Cost-effectiveness analysis
     */
    public CostEffectivenessAnalysis getCostAnalysis() {
        return costAnalysis;
    }

    /**
     * Set cost analysis
     * @param costAnalysis Cost-effectiveness information
     */
    public void setCostAnalysis(CostEffectivenessAnalysis costAnalysis) {
        this.costAnalysis = costAnalysis;
    }

    /**
     * Get patient considerations
     * @return Patient-specific factors
     */
    public PatientFactors getPatientConsiderations() {
        return patientConsiderations;
    }

    /**
     * Set patient considerations
     * @param patientConsiderations Patient-specific factors
     */
    public void setPatientConsiderations(PatientFactors patientConsiderations) {
        this.patientConsiderations = patientConsiderations;
    }

    /**
     * Get urgency level
     * @return Clinical urgency (ROUTINE, MODERATE, HIGH, EMERGENCY)
     */
    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    /**
     * Set urgency level
     * @param urgencyLevel How urgently decision should be implemented
     */
    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    /**
     * Get confidence level
     * @return AI confidence in recommendations (HIGH, MODERATE, LOW)
     */
    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    /**
     * Set confidence level
     * @param confidenceLevel AI confidence in analysis
     */
    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    /**
     * Get key decision factors
     * @return List of factors influencing the decision
     */
    public List<String> getKeyDecisionFactors() {
        return keyDecisionFactors;
    }

    /**
     * Set key decision factors
     * @param keyDecisionFactors Factors that influenced the decision
     */
    public void setKeyDecisionFactors(List<String> keyDecisionFactors) {
        this.keyDecisionFactors = keyDecisionFactors != null ?
                new ArrayList<>(keyDecisionFactors) : new ArrayList<>();
    }

    /**
     * Get patient education
     * @return Patient education recommendations
     */
    public String getPatientEducation() {
        return patientEducation;
    }

    /**
     * Set patient education
     * @param patientEducation Information to share with patient
     */
    public void setPatientEducation(String patientEducation) {
        this.patientEducation = patientEducation;
    }

    /**
     * Get provider guidance
     * @return Guidance for healthcare providers
     */
    public String getProviderGuidance() {
        return providerGuidance;
    }

    /**
     * Set provider guidance
     * @param providerGuidance Implementation guidance for providers
     */
    public void setProviderGuidance(String providerGuidance) {
        this.providerGuidance = providerGuidance;
    }

    /**
     * Get quality measures
     * @return Quality indicators to track
     */
    public String getQualityMeasures() {
        return qualityMeasures;
    }

    /**
     * Set quality measures
     * @param qualityMeasures Quality metrics for decision
     */
    public void setQualityMeasures(String qualityMeasures) {
        this.qualityMeasures = qualityMeasures;
    }

    /**
     * Get safety considerations
     * @return Patient safety considerations
     */
    public String getSafetyConsiderations() {
        return safetyConsiderations;
    }

    /**
     * Set safety considerations
     * @param safetyConsiderations Safety factors to consider
     */
    public void setSafetyConsiderations(String safetyConsiderations) {
        this.safetyConsiderations = safetyConsiderations;
    }

    /**
     * Get disclaimer
     * @return Medical/legal disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * Set disclaimer
     * @param disclaimer Clinical disclaimer text
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     * Get timestamp
     * @return When analysis was performed
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp
     * @param timestamp Analysis timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get analysis version
     * @return Version of analysis algorithm
     */
    public String getAnalysisVersion() {
        return analysisVersion;
    }

    /**
     * Set analysis version
     * @param analysisVersion Algorithm version used
     */
    public void setAnalysisVersion(String analysisVersion) {
        this.analysisVersion = analysisVersion;
    }

    /**
     * Get decision summary
     * @return Summary of the clinical decision
     */
    public DecisionSummary getSummary() {
        return summary;
    }

    /**
     * Set decision summary
     * @param summary Summary information
     */
    public void setSummary(DecisionSummary summary) {
        this.summary = summary;
    }

    /**
     * Get metadata
     * @return Additional analysis metadata
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Set metadata
     * @param metadata Analysis metadata
     */
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
    }

    // ================================
    // Utility Methods
    // ================================

    /**
     * Add a recommendation option
     * @param option Recommendation option to add
     */
    public void addRecommendationOption(RecommendationOption option) {
        if (option != null) {
            this.recommendationOptions.add(option);
        }
    }

    /**
     * Add a contraindication
     * @param contraindication Contraindication to add
     */
    public void addContraindication(String contraindication) {
        if (contraindication != null && !contraindication.trim().isEmpty()) {
            this.contraindications.add(contraindication.trim());
        }
    }

    /**
     * Add a monitoring parameter
     * @param parameter Monitoring parameter to add
     */
    public void addMonitoringParameter(String parameter) {
        if (parameter != null && !parameter.trim().isEmpty()) {
            this.monitoringParameters.add(parameter.trim());
        }
    }

    /**
     * Add an evidence source
     * @param source Evidence source to add
     */
    public void addEvidenceSource(EvidenceSource source) {
        if (source != null) {
            this.evidenceSources.add(source);
        }
    }

    /**
     * Add a key decision factor
     * @param factor Decision factor to add
     */
    public void addKeyDecisionFactor(String factor) {
        if (factor != null && !factor.trim().isEmpty()) {
            this.keyDecisionFactors.add(factor.trim());
        }
    }

    /**
     * Check if this is an emergency decision
     * @return true if emergency urgency level
     */
    public boolean isEmergencyDecision() {
        return "EMERGENCY".equals(urgencyLevel);
    }

    /**
     * Check if high confidence recommendation
     * @return true if high confidence level
     */
    public boolean isHighConfidence() {
        return "HIGH".equals(confidenceLevel);
    }

    /**
     * Check if contraindications exist
     * @return true if contraindications are present
     */
    public boolean hasContraindications() {
        return contraindications != null && !contraindications.isEmpty();
    }

    /**
     * Get the best recommendation option
     * @return Top-ranked recommendation option
     */
    public RecommendationOption getBestRecommendation() {
        if (recommendationOptions != null && !recommendationOptions.isEmpty()) {
            return recommendationOptions.get(0);
        }
        return null;
    }

    /**
     * Generate summary automatically
     */
    private void generateSummary() {
        this.summary = new DecisionSummary();
        this.summary.setDecisionType(this.decisionType);
        this.summary.setUrgencyLevel(this.urgencyLevel);
        this.summary.setConfidenceLevel(this.confidenceLevel);
        this.summary.setHasContraindications(hasContraindications());
        this.summary.setOptionsCount(recommendationOptions != null ? recommendationOptions.size() : 0);
    }

    // ================================
    // Inner Classes
    // ================================

    /**
     * Individual recommendation option with details
     */
    public static class RecommendationOption {
        private String recommendation;
        private String rationale;
        private String evidenceLevel; // HIGH, MODERATE, LOW
        private Double strengthRating; // 0.0 to 1.0
        private List<String> benefits;
        private List<String> risks;
        private String implementation;
        private String monitoring;
        private String duration;
        private String cost;
        private String patientAcceptability;

        // Constructors
        public RecommendationOption() {
            this.benefits = new ArrayList<>();
            this.risks = new ArrayList<>();
        }

        public RecommendationOption(String recommendation, String rationale) {
            this();
            this.recommendation = recommendation;
            this.rationale = rationale;
        }

        // Getters and Setters
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

        public String getRationale() { return rationale; }
        public void setRationale(String rationale) { this.rationale = rationale; }

        public String getEvidenceLevel() { return evidenceLevel; }
        public void setEvidenceLevel(String evidenceLevel) { this.evidenceLevel = evidenceLevel; }

        public Double getStrengthRating() { return strengthRating; }
        public void setStrengthRating(Double strengthRating) { this.strengthRating = strengthRating; }

        public List<String> getBenefits() { return benefits; }
        public void setBenefits(List<String> benefits) {
            this.benefits = benefits != null ? new ArrayList<>(benefits) : new ArrayList<>();
        }

        public List<String> getRisks() { return risks; }
        public void setRisks(List<String> risks) {
            this.risks = risks != null ? new ArrayList<>(risks) : new ArrayList<>();
        }

        public String getImplementation() { return implementation; }
        public void setImplementation(String implementation) { this.implementation = implementation; }

        public String getMonitoring() { return monitoring; }
        public void setMonitoring(String monitoring) { this.monitoring = monitoring; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public String getCost() { return cost; }
        public void setCost(String cost) { this.cost = cost; }

        public String getPatientAcceptability() { return patientAcceptability; }
        public void setPatientAcceptability(String patientAcceptability) { this.patientAcceptability = patientAcceptability; }

        @Override
        public String toString() {
            return String.format("%s (Evidence: %s)", recommendation, evidenceLevel);
        }
    }

    /**
     * Evidence source information
     */
    public static class EvidenceSource {
        private String type; // GUIDELINE, RCT, META_ANALYSIS, SYSTEMATIC_REVIEW, EXPERT_OPINION
        private String title;
        private String organization;
        private String year;
        private String quality; // HIGH, MODERATE, LOW
        private String relevance; // HIGH, MODERATE, LOW
        private String summary;
        private String url;

        // Constructors
        public EvidenceSource() {}

        public EvidenceSource(String type, String title, String organization) {
            this.type = type;
            this.title = title;
            this.organization = organization;
        }

        // Getters and Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getOrganization() { return organization; }
        public void setOrganization(String organization) { this.organization = organization; }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }

        public String getRelevance() { return relevance; }
        public void setRelevance(String relevance) { this.relevance = relevance; }

        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    /**
     * Monitoring plan details
     */
    public static class MonitoringPlan {
        private String frequency;
        private List<String> parameters;
        private List<String> labTests;
        private List<String> clinicalAssessments;
        private String duration;
        private List<String> stopCriteria;
        private String escalationPlan;

        // Constructors
        public MonitoringPlan() {
            this.parameters = new ArrayList<>();
            this.labTests = new ArrayList<>();
            this.clinicalAssessments = new ArrayList<>();
            this.stopCriteria = new ArrayList<>();
        }

        // Getters and Setters
        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }

        public List<String> getParameters() { return parameters; }
        public void setParameters(List<String> parameters) {
            this.parameters = parameters != null ? new ArrayList<>(parameters) : new ArrayList<>();
        }

        public List<String> getLabTests() { return labTests; }
        public void setLabTests(List<String> labTests) {
            this.labTests = labTests != null ? new ArrayList<>(labTests) : new ArrayList<>();
        }

        public List<String> getClinicalAssessments() { return clinicalAssessments; }
        public void setClinicalAssessments(List<String> clinicalAssessments) {
            this.clinicalAssessments = clinicalAssessments != null ? new ArrayList<>(clinicalAssessments) : new ArrayList<>();
        }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public List<String> getStopCriteria() { return stopCriteria; }
        public void setStopCriteria(List<String> stopCriteria) {
            this.stopCriteria = stopCriteria != null ? new ArrayList<>(stopCriteria) : new ArrayList<>();
        }

        public String getEscalationPlan() { return escalationPlan; }
        public void setEscalationPlan(String escalationPlan) { this.escalationPlan = escalationPlan; }
    }

    /**
     * Cost-effectiveness analysis
     */
    public static class CostEffectivenessAnalysis {
        private String costCategory; // LOW, MODERATE, HIGH
        private String costComparison;
        private String valueAssessment;
        private String insuranceCoverage;
        private List<String> costFactors;
        private String economicImpact;

        // Constructors
        public CostEffectivenessAnalysis() {
            this.costFactors = new ArrayList<>();
        }

        // Getters and Setters
        public String getCostCategory() { return costCategory; }
        public void setCostCategory(String costCategory) { this.costCategory = costCategory; }

        public String getCostComparison() { return costComparison; }
        public void setCostComparison(String costComparison) { this.costComparison = costComparison; }

        public String getValueAssessment() { return valueAssessment; }
        public void setValueAssessment(String valueAssessment) { this.valueAssessment = valueAssessment; }

        public String getInsuranceCoverage() { return insuranceCoverage; }
        public void setInsuranceCoverage(String insuranceCoverage) { this.insuranceCoverage = insuranceCoverage; }

        public List<String> getCostFactors() { return costFactors; }
        public void setCostFactors(List<String> costFactors) {
            this.costFactors = costFactors != null ? new ArrayList<>(costFactors) : new ArrayList<>();
        }

        public String getEconomicImpact() { return economicImpact; }
        public void setEconomicImpact(String economicImpact) { this.economicImpact = economicImpact; }
    }

    /**
     * Patient-specific factors
     */
    public static class PatientFactors {
        private String ageConsiderations;
        private String genderConsiderations;
        private String comorbidityImpact;
        private String functionalStatus;
        private String cognitiveStatus;
        private String socialSupport;
        private String culturalFactors;
        private String preferences;
        private String adherenceFactors;

        // Constructors
        public PatientFactors() {}

        // Getters and Setters
        public String getAgeConsiderations() { return ageConsiderations; }
        public void setAgeConsiderations(String ageConsiderations) { this.ageConsiderations = ageConsiderations; }

        public String getGenderConsiderations() { return genderConsiderations; }
        public void setGenderConsiderations(String genderConsiderations) { this.genderConsiderations = genderConsiderations; }

        public String getComorbidityImpact() { return comorbidityImpact; }
        public void setComorbidityImpact(String comorbidityImpact) { this.comorbidityImpact = comorbidityImpact; }

        public String getFunctionalStatus() { return functionalStatus; }
        public void setFunctionalStatus(String functionalStatus) { this.functionalStatus = functionalStatus; }

        public String getCognitiveStatus() { return cognitiveStatus; }
        public void setCognitiveStatus(String cognitiveStatus) { this.cognitiveStatus = cognitiveStatus; }

        public String getSocialSupport() { return socialSupport; }
        public void setSocialSupport(String socialSupport) { this.socialSupport = socialSupport; }

        public String getCulturalFactors() { return culturalFactors; }
        public void setCulturalFactors(String culturalFactors) { this.culturalFactors = culturalFactors; }

        public String getPreferences() { return preferences; }
        public void setPreferences(String preferences) { this.preferences = preferences; }

        public String getAdherenceFactors() { return adherenceFactors; }
        public void setAdherenceFactors(String adherenceFactors) { this.adherenceFactors = adherenceFactors; }
    }

    /**
     * Summary of the clinical decision
     */
    public static class DecisionSummary {
        private String decisionType;
        private String urgencyLevel;
        private String confidenceLevel;
        private int optionsCount;
        private boolean hasContraindications;
        private boolean requiresMonitoring;
        private boolean highComplexity;
        private String primaryFocus;

        // Constructors
        public DecisionSummary() {}

        // Getters and Setters
        public String getDecisionType() { return decisionType; }
        public void setDecisionType(String decisionType) { this.decisionType = decisionType; }

        public String getUrgencyLevel() { return urgencyLevel; }
        public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

        public String getConfidenceLevel() { return confidenceLevel; }
        public void setConfidenceLevel(String confidenceLevel) { this.confidenceLevel = confidenceLevel; }

        public int getOptionsCount() { return optionsCount; }
        public void setOptionsCount(int optionsCount) { this.optionsCount = optionsCount; }

        public boolean isHasContraindications() { return hasContraindications; }
        public void setHasContraindications(boolean hasContraindications) { this.hasContraindications = hasContraindications; }

        public boolean isRequiresMonitoring() { return requiresMonitoring; }
        public void setRequiresMonitoring(boolean requiresMonitoring) { this.requiresMonitoring = requiresMonitoring; }

        public boolean isHighComplexity() { return highComplexity; }
        public void setHighComplexity(boolean highComplexity) { this.highComplexity = highComplexity; }

        public String getPrimaryFocus() { return primaryFocus; }
        public void setPrimaryFocus(String primaryFocus) { this.primaryFocus = primaryFocus; }
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "ClinicalDecisionResponse{" +
                "decisionType='" + decisionType + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", confidenceLevel='" + confidenceLevel + '\'' +
                ", optionsCount=" + (recommendationOptions != null ? recommendationOptions.size() : 0) +
                ", hasContraindications=" + hasContraindications() +
                ", timestamp=" + timestamp +
                '}';
    }
}