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
 * 🎯 Differential Diagnosis Response DTO
 * Contains AI-generated differential diagnosis with clinical reasoning
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DifferentialDiagnosisResponse {

    private List<DiagnosisOption> diagnoses;
    private String clinicalReasoning;
    private String chiefComplaint;
    private List<String> recommendedTests;
    private List<String> recommendedImaging;
    private List<String> recommendedLabWork;
    private List<String> redFlags;
    private List<String> safetyConcerns;
    private String urgencyLevel; // LOW, MODERATE, HIGH, EMERGENCY
    private String nextSteps;
    private String followUpRecommendations;
    private List<String> specialistReferrals;
    private String patientEducation;
    private String disclaimer;
    private DiagnosisSummary summary;
    private ClinicalContext context;
    private List<String> keyFindings;
    private String prognosticFactors;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String analysisVersion;
    private String confidenceLevel; // HIGH, MODERATE, LOW
    private Map<String, Object> metadata;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public DifferentialDiagnosisResponse() {
        this.diagnoses = new ArrayList<>();
        this.recommendedTests = new ArrayList<>();
        this.recommendedImaging = new ArrayList<>();
        this.recommendedLabWork = new ArrayList<>();
        this.redFlags = new ArrayList<>();
        this.safetyConcerns = new ArrayList<>();
        this.specialistReferrals = new ArrayList<>();
        this.keyFindings = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.timestamp = LocalDateTime.now();
        this.analysisVersion = "1.0";
        this.confidenceLevel = "MODERATE";
    }

    /**
     * Constructor with diagnosis list and disclaimer
     * @param diagnoses List of differential diagnoses
     * @param disclaimer Medical disclaimer
     */
    public DifferentialDiagnosisResponse(@NotNull List<DiagnosisOption> diagnoses, String disclaimer) {
        this();
        this.diagnoses = new ArrayList<>(diagnoses);
        this.disclaimer = disclaimer;
        this.generateSummary();
        this.assessUrgency();
    }

    /**
     * Constructor with clinical reasoning
     * @param diagnoses List of diagnoses
     * @param clinicalReasoning Clinical reasoning narrative
     * @param urgencyLevel Clinical urgency assessment
     */
    public DifferentialDiagnosisResponse(@NotNull List<DiagnosisOption> diagnoses,
                                         String clinicalReasoning,
                                         String urgencyLevel) {
        this();
        this.diagnoses = new ArrayList<>(diagnoses);
        this.clinicalReasoning = clinicalReasoning;
        this.urgencyLevel = urgencyLevel;
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
    public static DifferentialDiagnosisResponse error(String message) {
        DifferentialDiagnosisResponse response = new DifferentialDiagnosisResponse();
        response.disclaimer = "Error: " + message;
        response.urgencyLevel = "UNKNOWN";
        response.confidenceLevel = "LOW";
        response.clinicalReasoning = "Unable to complete differential diagnosis analysis due to: " + message;
        return response;
    }

    /**
     * Create emergency response
     * @param symptoms Emergency symptoms
     * @return Emergency differential response
     */
    public static DifferentialDiagnosisResponse emergency(String symptoms) {
        DifferentialDiagnosisResponse response = new DifferentialDiagnosisResponse();
        response.urgencyLevel = "EMERGENCY";
        response.confidenceLevel = "HIGH";
        response.clinicalReasoning = "Emergency symptoms detected requiring immediate medical evaluation.";
        response.redFlags.add("Emergency symptoms present");
        response.nextSteps = "🚨 SEEK IMMEDIATE MEDICAL ATTENTION 🚨";
        response.disclaimer = "This appears to be a medical emergency. Call 911 or go to the nearest emergency room immediately.";
        return response;
    }

    // ================================
    // Getters and Setters
    // ================================

    /**
     * Get differential diagnoses list
     * @return List of diagnosis options ranked by likelihood
     */
    public List<DiagnosisOption> getDiagnoses() {
        return diagnoses;
    }

    /**
     * Set differential diagnoses list
     * @param diagnoses Ranked list of possible diagnoses
     */
    public void setDiagnoses(List<DiagnosisOption> diagnoses) {
        this.diagnoses = diagnoses != null ? new ArrayList<>(diagnoses) : new ArrayList<>();
    }

    /**
     * Get clinical reasoning
     * @return AI-generated clinical reasoning narrative
     */
    public String getClinicalReasoning() {
        return clinicalReasoning;
    }

    /**
     * Set clinical reasoning
     * @param clinicalReasoning Clinical thought process explanation
     */
    public void setClinicalReasoning(String clinicalReasoning) {
        this.clinicalReasoning = clinicalReasoning;
    }

    /**
     * Get chief complaint
     * @return Primary presenting complaint
     */
    public String getChiefComplaint() {
        return chiefComplaint;
    }

    /**
     * Set chief complaint
     * @param chiefComplaint Main reason for seeking care
     */
    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    /**
     * Get recommended tests
     * @return List of recommended diagnostic tests
     */
    public List<String> getRecommendedTests() {
        return recommendedTests;
    }

    /**
     * Set recommended tests
     * @param recommendedTests Diagnostic tests to consider
     */
    public void setRecommendedTests(List<String> recommendedTests) {
        this.recommendedTests = recommendedTests != null ?
                new ArrayList<>(recommendedTests) : new ArrayList<>();
    }

    /**
     * Get recommended imaging
     * @return List of recommended imaging studies
     */
    public List<String> getRecommendedImaging() {
        return recommendedImaging;
    }

    /**
     * Set recommended imaging
     * @param recommendedImaging Imaging studies to consider
     */
    public void setRecommendedImaging(List<String> recommendedImaging) {
        this.recommendedImaging = recommendedImaging != null ?
                new ArrayList<>(recommendedImaging) : new ArrayList<>();
    }

    /**
     * Get recommended lab work
     * @return List of recommended laboratory tests
     */
    public List<String> getRecommendedLabWork() {
        return recommendedLabWork;
    }

    /**
     * Set recommended lab work
     * @param recommendedLabWork Laboratory tests to order
     */
    public void setRecommendedLabWork(List<String> recommendedLabWork) {
        this.recommendedLabWork = recommendedLabWork != null ?
                new ArrayList<>(recommendedLabWork) : new ArrayList<>();
    }

    /**
     * Get red flags
     * @return List of concerning symptoms or findings
     */
    public List<String> getRedFlags() {
        return redFlags;
    }

    /**
     * Set red flags
     * @param redFlags Warning signs requiring immediate attention
     */
    public void setRedFlags(List<String> redFlags) {
        this.redFlags = redFlags != null ? new ArrayList<>(redFlags) : new ArrayList<>();
    }

    /**
     * Get safety concerns
     * @return List of patient safety considerations
     */
    public List<String> getSafetyConcerns() {
        return safetyConcerns;
    }

    /**
     * Set safety concerns
     * @param safetyConcerns Patient safety issues to monitor
     */
    public void setSafetyConcerns(List<String> safetyConcerns) {
        this.safetyConcerns = safetyConcerns != null ?
                new ArrayList<>(safetyConcerns) : new ArrayList<>();
    }

    /**
     * Get urgency level
     * @return Clinical urgency assessment (LOW, MODERATE, HIGH, EMERGENCY)
     */
    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    /**
     * Set urgency level
     * @param urgencyLevel How urgently evaluation is needed
     */
    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    /**
     * Get next steps
     * @return Immediate next steps in patient care
     */
    public String getNextSteps() {
        return nextSteps;
    }

    /**
     * Set next steps
     * @param nextSteps Immediate actions to take
     */
    public void setNextSteps(String nextSteps) {
        this.nextSteps = nextSteps;
    }

    /**
     * Get follow-up recommendations
     * @return Long-term follow-up plan
     */
    public String getFollowUpRecommendations() {
        return followUpRecommendations;
    }

    /**
     * Set follow-up recommendations
     * @param followUpRecommendations Ongoing care plan
     */
    public void setFollowUpRecommendations(String followUpRecommendations) {
        this.followUpRecommendations = followUpRecommendations;
    }

    /**
     * Get specialist referrals
     * @return List of specialist referrals to consider
     */
    public List<String> getSpecialistReferrals() {
        return specialistReferrals;
    }

    /**
     * Set specialist referrals
     * @param specialistReferrals Specialists to consult
     */
    public void setSpecialistReferrals(List<String> specialistReferrals) {
        this.specialistReferrals = specialistReferrals != null ?
                new ArrayList<>(specialistReferrals) : new ArrayList<>();
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
     * Get diagnosis summary
     * @return Summary statistics and overview
     */
    public DiagnosisSummary getSummary() {
        return summary;
    }

    /**
     * Set diagnosis summary
     * @param summary Summary of differential analysis
     */
    public void setSummary(DiagnosisSummary summary) {
        this.summary = summary;
    }

    /**
     * Get clinical context
     * @return Context information for the analysis
     */
    public ClinicalContext getContext() {
        return context;
    }

    /**
     * Set clinical context
     * @param context Clinical context information
     */
    public void setContext(ClinicalContext context) {
        this.context = context;
    }

    /**
     * Get key findings
     * @return List of key clinical findings
     */
    public List<String> getKeyFindings() {
        return keyFindings;
    }

    /**
     * Set key findings
     * @param keyFindings Important clinical findings
     */
    public void setKeyFindings(List<String> keyFindings) {
        this.keyFindings = keyFindings != null ?
                new ArrayList<>(keyFindings) : new ArrayList<>();
    }

    /**
     * Get prognostic factors
     * @return Factors affecting prognosis
     */
    public String getPrognosticFactors() {
        return prognosticFactors;
    }

    /**
     * Set prognostic factors
     * @param prognosticFactors Elements affecting patient outcome
     */
    public void setPrognosticFactors(String prognosticFactors) {
        this.prognosticFactors = prognosticFactors;
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
     * Get confidence level
     * @return AI confidence in analysis (HIGH, MODERATE, LOW)
     */
    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    /**
     * Set confidence level
     * @param confidenceLevel How confident the AI is in results
     */
    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
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
     * Add a diagnosis option
     * @param diagnosis Diagnosis to add to differential
     */
    public void addDiagnosis(DiagnosisOption diagnosis) {
        if (diagnosis != null) {
            this.diagnoses.add(diagnosis);
        }
    }

    /**
     * Add a red flag
     * @param redFlag Warning sign to add
     */
    public void addRedFlag(String redFlag) {
        if (redFlag != null && !redFlag.trim().isEmpty()) {
            this.redFlags.add(redFlag.trim());
        }
    }

    /**
     * Add a recommended test
     * @param test Diagnostic test to add
     */
    public void addRecommendedTest(String test) {
        if (test != null && !test.trim().isEmpty()) {
            this.recommendedTests.add(test.trim());
        }
    }

    /**
     * Add a specialist referral
     * @param specialist Specialist to add
     */
    public void addSpecialistReferral(String specialist) {
        if (specialist != null && !specialist.trim().isEmpty()) {
            this.specialistReferrals.add(specialist.trim());
        }
    }

    /**
     * Check if this is an emergency case
     * @return true if emergency urgency level
     */
    public boolean isEmergencyCase() {
        return "EMERGENCY".equals(urgencyLevel);
    }

    /**
     * Check if immediate attention is needed
     * @return true if high urgency or red flags present
     */
    public boolean requiresImmediateAttention() {
        return isEmergencyCase() ||
                "HIGH".equals(urgencyLevel) ||
                (redFlags != null && !redFlags.isEmpty());
    }

    /**
     * Get the most likely diagnosis
     * @return Top diagnosis option or null if none
     */
    public DiagnosisOption getMostLikelyDiagnosis() {
        if (diagnoses != null && !diagnoses.isEmpty()) {
            return diagnoses.get(0);
        }
        return null;
    }

    /**
     * Get number of high likelihood diagnoses
     * @return Count of diagnoses marked as HIGH likelihood
     */
    public int getHighLikelihoodCount() {
        if (diagnoses == null) return 0;
        return (int) diagnoses.stream()
                .filter(d -> "HIGH".equals(d.getLikelihood()))
                .count();
    }

    /**
     * Generate summary automatically
     */
    private void generateSummary() {
        this.summary = new DiagnosisSummary();
        this.summary.setTotalDiagnoses(diagnoses != null ? diagnoses.size() : 0);
        this.summary.setHighLikelihood(getHighLikelihoodCount());
        this.summary.setRequiresUrgentCare(requiresImmediateAttention());
        this.summary.setComplexCase(diagnoses != null && diagnoses.size() > 5);
    }

    /**
     * Assess urgency automatically
     */
    private void assessUrgency() {
        if (urgencyLevel == null) {
            if (redFlags != null && !redFlags.isEmpty()) {
                this.urgencyLevel = "HIGH";
            } else if (getHighLikelihoodCount() > 0) {
                this.urgencyLevel = "MODERATE";
            } else {
                this.urgencyLevel = "LOW";
            }
        }
    }

    // ================================
    // Inner Classes
    // ================================

    /**
     * Individual diagnosis option with supporting details
     */
    public static class DiagnosisOption {
        private String diagnosis;
        private String likelihood; // HIGH, MEDIUM, LOW
        private Double probabilityScore; // 0.0 to 1.0
        private List<String> supportingFeatures;
        private List<String> distinguishingFeatures;
        private List<String> recommendedTests;
        private String reasoning;
        private String icd10Code;
        private Integer prevalence; // Prevalence percentage
        private String prognosis;
        private String urgency;
        private List<String> complications;
        private String treatment;

        // Constructors
        public DiagnosisOption() {
            this.supportingFeatures = new ArrayList<>();
            this.distinguishingFeatures = new ArrayList<>();
            this.recommendedTests = new ArrayList<>();
            this.complications = new ArrayList<>();
        }

        public DiagnosisOption(String diagnosis, String likelihood) {
            this();
            this.diagnosis = diagnosis;
            this.likelihood = likelihood;
        }

        // Getters and Setters
        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

        public String getLikelihood() { return likelihood; }
        public void setLikelihood(String likelihood) { this.likelihood = likelihood; }

        public Double getProbabilityScore() { return probabilityScore; }
        public void setProbabilityScore(Double probabilityScore) { this.probabilityScore = probabilityScore; }

        public List<String> getSupportingFeatures() { return supportingFeatures; }
        public void setSupportingFeatures(List<String> supportingFeatures) {
            this.supportingFeatures = supportingFeatures != null ?
                    new ArrayList<>(supportingFeatures) : new ArrayList<>();
        }

        public List<String> getDistinguishingFeatures() { return distinguishingFeatures; }
        public void setDistinguishingFeatures(List<String> distinguishingFeatures) {
            this.distinguishingFeatures = distinguishingFeatures != null ?
                    new ArrayList<>(distinguishingFeatures) : new ArrayList<>();
        }

        public List<String> getRecommendedTests() { return recommendedTests; }
        public void setRecommendedTests(List<String> recommendedTests) {
            this.recommendedTests = recommendedTests != null ?
                    new ArrayList<>(recommendedTests) : new ArrayList<>();
        }

        public String getReasoning() { return reasoning; }
        public void setReasoning(String reasoning) { this.reasoning = reasoning; }

        public String getIcd10Code() { return icd10Code; }
        public void setIcd10Code(String icd10Code) { this.icd10Code = icd10Code; }

        public Integer getPrevalence() { return prevalence; }
        public void setPrevalence(Integer prevalence) { this.prevalence = prevalence; }

        public String getPrognosis() { return prognosis; }
        public void setPrognosis(String prognosis) { this.prognosis = prognosis; }

        public String getUrgency() { return urgency; }
        public void setUrgency(String urgency) { this.urgency = urgency; }

        public List<String> getComplications() { return complications; }
        public void setComplications(List<String> complications) {
            this.complications = complications != null ?
                    new ArrayList<>(complications) : new ArrayList<>();
        }

        public String getTreatment() { return treatment; }
        public void setTreatment(String treatment) { this.treatment = treatment; }

        @Override
        public String toString() {
            return String.format("%s (%s likelihood)", diagnosis, likelihood);
        }
    }

    /**
     * Summary statistics for the differential diagnosis
     */
    public static class DiagnosisSummary {
        private int totalDiagnoses;
        private int highLikelihood;
        private int moderateLikelihood;
        private int lowLikelihood;
        private boolean requiresUrgentCare;
        private boolean complexCase;
        private String primarySystem; // Most likely affected body system

        // Constructors
        public DiagnosisSummary() {}

        // Getters and Setters
        public int getTotalDiagnoses() { return totalDiagnoses; }
        public void setTotalDiagnoses(int totalDiagnoses) { this.totalDiagnoses = totalDiagnoses; }

        public int getHighLikelihood() { return highLikelihood; }
        public void setHighLikelihood(int highLikelihood) { this.highLikelihood = highLikelihood; }

        public int getModerateLikelihood() { return moderateLikelihood; }
        public void setModerateLikelihood(int moderateLikelihood) { this.moderateLikelihood = moderateLikelihood; }

        public int getLowLikelihood() { return lowLikelihood; }
        public void setLowLikelihood(int lowLikelihood) { this.lowLikelihood = lowLikelihood; }

        public boolean isRequiresUrgentCare() { return requiresUrgentCare; }
        public void setRequiresUrgentCare(boolean requiresUrgentCare) { this.requiresUrgentCare = requiresUrgentCare; }

        public boolean isComplexCase() { return complexCase; }
        public void setComplexCase(boolean complexCase) { this.complexCase = complexCase; }

        public String getPrimarySystem() { return primarySystem; }
        public void setPrimarySystem(String primarySystem) { this.primarySystem = primarySystem; }
    }

    /**
     * Clinical context information
     */
    public static class ClinicalContext {
        private String specialty;
        private String setting; // EMERGENCY, OUTPATIENT, INPATIENT
        private String patientAge;
        private String patientGender;
        private String chiefComplaint;
        private String symptomDuration;
        private String onsetType;

        // Constructors
        public ClinicalContext() {}

        // Getters and Setters
        public String getSpecialty() { return specialty; }
        public void setSpecialty(String specialty) { this.specialty = specialty; }

        public String getSetting() { return setting; }
        public void setSetting(String setting) { this.setting = setting; }

        public String getPatientAge() { return patientAge; }
        public void setPatientAge(String patientAge) { this.patientAge = patientAge; }

        public String getPatientGender() { return patientGender; }
        public void setPatientGender(String patientGender) { this.patientGender = patientGender; }

        public String getChiefComplaint() { return chiefComplaint; }
        public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }

        public String getSymptomDuration() { return symptomDuration; }
        public void setSymptomDuration(String symptomDuration) { this.symptomDuration = symptomDuration; }

        public String getOnsetType() { return onsetType; }
        public void setOnsetType(String onsetType) { this.onsetType = onsetType; }
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "DifferentialDiagnosisResponse{" +
                "totalDiagnoses=" + (diagnoses != null ? diagnoses.size() : 0) +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", confidenceLevel='" + confidenceLevel + '\'' +
                ", redFlags=" + (redFlags != null ? redFlags.size() : 0) +
                ", requiresUrgentCare=" + requiresImmediateAttention() +
                ", timestamp=" + timestamp +
                '}';
    }
}