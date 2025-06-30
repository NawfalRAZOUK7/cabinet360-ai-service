package com.cabinet360.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * 🩺 Clinical Decision Support Request DTO
 * Used for AI-powered clinical decision support and treatment recommendations
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
public class ClinicalDecisionRequest {

    @NotBlank(message = "Clinical scenario cannot be empty")
    @Size(min = 20, max = 3000, message = "Clinical scenario must be between 20 and 3000 characters")
    private String clinicalScenario;

    @Size(max = 1500, message = "Patient data description too long")
    private String patientData;

    @Pattern(regexp = "^(DIAGNOSIS|TREATMENT|MONITORING|REFERRAL|PREVENTION|PROGNOSIS|INVESTIGATION)?$",
            message = "Decision type must be DIAGNOSIS, TREATMENT, MONITORING, REFERRAL, PREVENTION, PROGNOSIS, or INVESTIGATION")
    private String decisionType;

    @Pattern(regexp = "^(HIGH|MODERATE|LOW|EXPERT_OPINION)?$",
            message = "Evidence level must be HIGH, MODERATE, LOW, or EXPERT_OPINION")
    private String evidenceLevel;

    @Pattern(regexp = "^(EMERGENCY|HIGH|MODERATE|LOW|ROUTINE)?$",
            message = "Urgency must be EMERGENCY, HIGH, MODERATE, LOW, or ROUTINE")
    private String urgency;

    @Pattern(regexp = "^(CARDIOLOGY|NEUROLOGY|PSYCHIATRY|PEDIATRICS|GERIATRICS|EMERGENCY|FAMILY|INTERNAL|SURGERY|GYNECOLOGY|DERMATOLOGY|ONCOLOGY|ENDOCRINOLOGY|RHEUMATOLOGY|PULMONOLOGY|GASTROENTEROLOGY|NEPHROLOGY|INFECTIOUS|HEMATOLOGY|RADIOLOGY|PATHOLOGY|ANESTHESIOLOGY|OPHTHALMOLOGY|OTOLARYNGOLOGY|UROLOGY|ORTHOPEDICS|PLASTIC_SURGERY)?$",
            message = "Invalid specialty. Must be a valid medical specialty.")
    private String specialty;

    @Pattern(regexp = "^\\d{1,3}$|^$",
            message = "Patient age must be a valid number (0-150)")
    private String patientAge;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)?$",
            message = "Gender must be MALE, FEMALE, or OTHER")
    private String patientGender;

    @Size(max = 1000, message = "Medical history too long")
    private String medicalHistory;

    @Size(max = 500, message = "Current medications list too long")
    private String currentMedications;

    @Size(max = 500, message = "Allergies description too long")
    private String allergies;

    @Size(max = 15, message = "Too many diagnostic tests listed")
    private List<String> availableDiagnosticTests;

    @Size(max = 20, message = "Too many treatment options listed")
    private List<String> treatmentOptions;

    @Size(max = 10, message = "Too many contraindications listed")
    private List<String> contraindications;

    @Size(max = 10, message = "Too many comorbidities listed")
    private List<String> comorbidities;

    @Size(max = 500, message = "Previous treatments description too long")
    private String previousTreatments;

    @Size(max = 500, message = "Treatment goals description too long")
    private String treatmentGoals;

    @Size(max = 200, message = "Patient preferences description too long")
    private String patientPreferences;

    @Size(max = 300, message = "Social factors description too long")
    private String socialFactors;

    @Size(max = 200, message = "Economic factors description too long")
    private String economicFactors;

    @Pattern(regexp = "^(INPATIENT|OUTPATIENT|EMERGENCY|ICU|SURGERY|CLINIC)?$",
            message = "Setting must be INPATIENT, OUTPATIENT, EMERGENCY, ICU, SURGERY, or CLINIC")
    private String clinicalSetting;

    @Size(max = 10, message = "Too many guidelines specified")
    private List<String> preferredGuidelines;

    @Pattern(regexp = "^(BASIC|STANDARD|COMPREHENSIVE|EXPERT)?$",
            message = "Analysis depth must be BASIC, STANDARD, COMPREHENSIVE, or EXPERT")
    private String analysisDepth;

    private Boolean includeAlternatives;
    private Boolean includeCostConsiderations;
    private Boolean includeRiskAssessment;
    private Boolean includeMonitoringPlan;

    @Size(max = 500, message = "Additional context too long")
    private String additionalContext;

    @Size(max = 300, message = "Specific questions too long")
    private String specificQuestions;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public ClinicalDecisionRequest() {
        this.availableDiagnosticTests = new ArrayList<>();
        this.treatmentOptions = new ArrayList<>();
        this.contraindications = new ArrayList<>();
        this.comorbidities = new ArrayList<>();
        this.preferredGuidelines = new ArrayList<>();
        this.includeAlternatives = true;
        this.includeCostConsiderations = false;
        this.includeRiskAssessment = true;
        this.includeMonitoringPlan = true;
    }

    /**
     * Constructor with clinical scenario only
     * @param clinicalScenario Description of the clinical situation
     */
    public ClinicalDecisionRequest(@NotNull String clinicalScenario) {
        this();
        this.clinicalScenario = clinicalScenario;
    }

    /**
     * Constructor with scenario and decision type
     * @param clinicalScenario Clinical situation description
     * @param decisionType Type of decision needed
     */
    public ClinicalDecisionRequest(@NotNull String clinicalScenario, String decisionType) {
        this();
        this.clinicalScenario = clinicalScenario;
        this.decisionType = decisionType;
    }

    /**
     * Constructor with core decision parameters
     * @param clinicalScenario Clinical situation
     * @param decisionType Type of decision
     * @param urgency Clinical urgency
     * @param specialty Medical specialty
     */
    public ClinicalDecisionRequest(@NotNull String clinicalScenario, String decisionType,
                                   String urgency, String specialty) {
        this();
        this.clinicalScenario = clinicalScenario;
        this.decisionType = decisionType;
        this.urgency = urgency;
        this.specialty = specialty;
    }

    // ================================
    // Getters and Setters
    // ================================

    /**
     * Get clinical scenario description
     * @return Clinical scenario
     */
    public String getClinicalScenario() {
        return clinicalScenario;
    }

    /**
     * Set clinical scenario description
     * @param clinicalScenario Description of clinical situation requiring decision
     */
    public void setClinicalScenario(String clinicalScenario) {
        this.clinicalScenario = clinicalScenario;
    }

    /**
     * Get patient data
     * @return Patient information
     */
    public String getPatientData() {
        return patientData;
    }

    /**
     * Set patient data
     * @param patientData Patient demographic and clinical information
     */
    public void setPatientData(String patientData) {
        this.patientData = patientData;
    }

    /**
     * Get decision type
     * @return Type of clinical decision needed
     */
    public String getDecisionType() {
        return decisionType;
    }

    /**
     * Set decision type
     * @param decisionType Type of decision (DIAGNOSIS, TREATMENT, etc.)
     */
    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    /**
     * Get evidence level requirement
     * @return Required evidence level
     */
    public String getEvidenceLevel() {
        return evidenceLevel;
    }

    /**
     * Set evidence level requirement
     * @param evidenceLevel Minimum evidence level for recommendations
     */
    public void setEvidenceLevel(String evidenceLevel) {
        this.evidenceLevel = evidenceLevel;
    }

    /**
     * Get urgency level
     * @return Clinical urgency
     */
    public String getUrgency() {
        return urgency;
    }

    /**
     * Set urgency level
     * @param urgency Clinical urgency assessment
     */
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    /**
     * Get medical specialty
     * @return Specialty context
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Set medical specialty
     * @param specialty Medical specialty for context
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * Get patient age
     * @return Patient age
     */
    public String getPatientAge() {
        return patientAge;
    }

    /**
     * Set patient age
     * @param patientAge Age in years
     */
    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    /**
     * Get patient gender
     * @return Patient gender
     */
    public String getPatientGender() {
        return patientGender;
    }

    /**
     * Set patient gender
     * @param patientGender Gender (MALE, FEMALE, OTHER)
     */
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    /**
     * Get medical history
     * @return Past medical history
     */
    public String getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * Set medical history
     * @param medicalHistory Past medical conditions and procedures
     */
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    /**
     * Get current medications
     * @return Current medication list
     */
    public String getCurrentMedications() {
        return currentMedications;
    }

    /**
     * Set current medications
     * @param currentMedications List of current medications
     */
    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }

    /**
     * Get allergies
     * @return Known allergies
     */
    public String getAllergies() {
        return allergies;
    }

    /**
     * Set allergies
     * @param allergies Known allergies and reactions
     */
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    /**
     * Get available diagnostic tests
     * @return List of available tests
     */
    public List<String> getAvailableDiagnosticTests() {
        return availableDiagnosticTests;
    }

    /**
     * Set available diagnostic tests
     * @param availableDiagnosticTests Tests available for diagnosis
     */
    public void setAvailableDiagnosticTests(List<String> availableDiagnosticTests) {
        this.availableDiagnosticTests = availableDiagnosticTests != null ?
                new ArrayList<>(availableDiagnosticTests) : new ArrayList<>();
    }

    /**
     * Get treatment options
     * @return List of treatment options
     */
    public List<String> getTreatmentOptions() {
        return treatmentOptions;
    }

    /**
     * Set treatment options
     * @param treatmentOptions Available treatment choices
     */
    public void setTreatmentOptions(List<String> treatmentOptions) {
        this.treatmentOptions = treatmentOptions != null ?
                new ArrayList<>(treatmentOptions) : new ArrayList<>();
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
     * Get comorbidities
     * @return List of comorbid conditions
     */
    public List<String> getComorbidities() {
        return comorbidities;
    }

    /**
     * Set comorbidities
     * @param comorbidities Comorbid medical conditions
     */
    public void setComorbidities(List<String> comorbidities) {
        this.comorbidities = comorbidities != null ?
                new ArrayList<>(comorbidities) : new ArrayList<>();
    }

    /**
     * Get previous treatments
     * @return Previous treatment history
     */
    public String getPreviousTreatments() {
        return previousTreatments;
    }

    /**
     * Set previous treatments
     * @param previousTreatments History of previous interventions
     */
    public void setPreviousTreatments(String previousTreatments) {
        this.previousTreatments = previousTreatments;
    }

    /**
     * Get treatment goals
     * @return Treatment objectives
     */
    public String getTreatmentGoals() {
        return treatmentGoals;
    }

    /**
     * Set treatment goals
     * @param treatmentGoals Therapeutic objectives
     */
    public void setTreatmentGoals(String treatmentGoals) {
        this.treatmentGoals = treatmentGoals;
    }

    /**
     * Get patient preferences
     * @return Patient's treatment preferences
     */
    public String getPatientPreferences() {
        return patientPreferences;
    }

    /**
     * Set patient preferences
     * @param patientPreferences Patient's wishes and preferences
     */
    public void setPatientPreferences(String patientPreferences) {
        this.patientPreferences = patientPreferences;
    }

    /**
     * Get social factors
     * @return Social determinants affecting care
     */
    public String getSocialFactors() {
        return socialFactors;
    }

    /**
     * Set social factors
     * @param socialFactors Social determinants and support systems
     */
    public void setSocialFactors(String socialFactors) {
        this.socialFactors = socialFactors;
    }

    /**
     * Get economic factors
     * @return Economic considerations
     */
    public String getEconomicFactors() {
        return economicFactors;
    }

    /**
     * Set economic factors
     * @param economicFactors Cost and insurance considerations
     */
    public void setEconomicFactors(String economicFactors) {
        this.economicFactors = economicFactors;
    }

    /**
     * Get clinical setting
     * @return Care setting context
     */
    public String getClinicalSetting() {
        return clinicalSetting;
    }

    /**
     * Set clinical setting
     * @param clinicalSetting Context of care delivery
     */
    public void setClinicalSetting(String clinicalSetting) {
        this.clinicalSetting = clinicalSetting;
    }

    /**
     * Get preferred guidelines
     * @return List of preferred clinical guidelines
     */
    public List<String> getPreferredGuidelines() {
        return preferredGuidelines;
    }

    /**
     * Set preferred guidelines
     * @param preferredGuidelines Clinical guidelines to prioritize
     */
    public void setPreferredGuidelines(List<String> preferredGuidelines) {
        this.preferredGuidelines = preferredGuidelines != null ?
                new ArrayList<>(preferredGuidelines) : new ArrayList<>();
    }

    /**
     * Get analysis depth
     * @return Depth of analysis requested
     */
    public String getAnalysisDepth() {
        return analysisDepth;
    }

    /**
     * Set analysis depth
     * @param analysisDepth Level of detail for analysis
     */
    public void setAnalysisDepth(String analysisDepth) {
        this.analysisDepth = analysisDepth;
    }

    /**
     * Check if alternatives should be included
     * @return true if alternatives requested
     */
    public Boolean getIncludeAlternatives() {
        return includeAlternatives;
    }

    /**
     * Set whether to include alternatives
     * @param includeAlternatives Include alternative treatment options
     */
    public void setIncludeAlternatives(Boolean includeAlternatives) {
        this.includeAlternatives = includeAlternatives;
    }

    /**
     * Check if cost considerations should be included
     * @return true if cost analysis requested
     */
    public Boolean getIncludeCostConsiderations() {
        return includeCostConsiderations;
    }

    /**
     * Set whether to include cost considerations
     * @param includeCostConsiderations Include cost-effectiveness analysis
     */
    public void setIncludeCostConsiderations(Boolean includeCostConsiderations) {
        this.includeCostConsiderations = includeCostConsiderations;
    }

    /**
     * Check if risk assessment should be included
     * @return true if risk analysis requested
     */
    public Boolean getIncludeRiskAssessment() {
        return includeRiskAssessment;
    }

    /**
     * Set whether to include risk assessment
     * @param includeRiskAssessment Include risk-benefit analysis
     */
    public void setIncludeRiskAssessment(Boolean includeRiskAssessment) {
        this.includeRiskAssessment = includeRiskAssessment;
    }

    /**
     * Check if monitoring plan should be included
     * @return true if monitoring plan requested
     */
    public Boolean getIncludeMonitoringPlan() {
        return includeMonitoringPlan;
    }

    /**
     * Set whether to include monitoring plan
     * @param includeMonitoringPlan Include follow-up monitoring recommendations
     */
    public void setIncludeMonitoringPlan(Boolean includeMonitoringPlan) {
        this.includeMonitoringPlan = includeMonitoringPlan;
    }

    /**
     * Get additional context
     * @return Extra contextual information
     */
    public String getAdditionalContext() {
        return additionalContext;
    }

    /**
     * Set additional context
     * @param additionalContext Extra information relevant to decision
     */
    public void setAdditionalContext(String additionalContext) {
        this.additionalContext = additionalContext;
    }

    /**
     * Get specific questions
     * @return Specific questions to address
     */
    public String getSpecificQuestions() {
        return specificQuestions;
    }

    /**
     * Set specific questions
     * @param specificQuestions Particular questions to answer
     */
    public void setSpecificQuestions(String specificQuestions) {
        this.specificQuestions = specificQuestions;
    }

    // ================================
    // Utility Methods
    // ================================

    /**
     * Add a diagnostic test to available options
     * @param test Diagnostic test to add
     */
    public void addAvailableDiagnosticTest(String test) {
        if (test != null && !test.trim().isEmpty()) {
            if (this.availableDiagnosticTests == null) {
                this.availableDiagnosticTests = new ArrayList<>();
            }
            this.availableDiagnosticTests.add(test.trim());
        }
    }

    /**
     * Add a treatment option
     * @param treatment Treatment option to add
     */
    public void addTreatmentOption(String treatment) {
        if (treatment != null && !treatment.trim().isEmpty()) {
            if (this.treatmentOptions == null) {
                this.treatmentOptions = new ArrayList<>();
            }
            this.treatmentOptions.add(treatment.trim());
        }
    }

    /**
     * Add a contraindication
     * @param contraindication Contraindication to add
     */
    public void addContraindication(String contraindication) {
        if (contraindication != null && !contraindication.trim().isEmpty()) {
            if (this.contraindications == null) {
                this.contraindications = new ArrayList<>();
            }
            this.contraindications.add(contraindication.trim());
        }
    }

    /**
     * Add a comorbidity
     * @param comorbidity Comorbid condition to add
     */
    public void addComorbidity(String comorbidity) {
        if (comorbidity != null && !comorbidity.trim().isEmpty()) {
            if (this.comorbidities == null) {
                this.comorbidities = new ArrayList<>();
            }
            this.comorbidities.add(comorbidity.trim());
        }
    }

    /**
     * Add a preferred guideline
     * @param guideline Clinical guideline to prioritize
     */
    public void addPreferredGuideline(String guideline) {
        if (guideline != null && !guideline.trim().isEmpty()) {
            if (this.preferredGuidelines == null) {
                this.preferredGuidelines = new ArrayList<>();
            }
            this.preferredGuidelines.add(guideline.trim());
        }
    }

    /**
     * Check if this is an emergency decision
     * @return true if emergency urgency
     */
    public boolean isEmergencyDecision() {
        return "EMERGENCY".equals(urgency);
    }

    /**
     * Check if this is a pediatric case
     * @return true if patient is under 18
     */
    public boolean isPediatricCase() {
        if (patientAge != null && !patientAge.isEmpty()) {
            try {
                int age = Integer.parseInt(patientAge);
                return age < 18;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Check if this is a geriatric case
     * @return true if patient is 65 or older
     */
    public boolean isGeriatricCase() {
        if (patientAge != null && !patientAge.isEmpty()) {
            try {
                int age = Integer.parseInt(patientAge);
                return age >= 65;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Get complexity score for this decision
     * @return Complexity score (0-10)
     */
    public int getComplexityScore() {
        int score = 0;

        // Base complexity from scenario length
        if (clinicalScenario != null && clinicalScenario.length() > 1000) score += 2;

        // Add for each complexity factor
        if (comorbidities != null && !comorbidities.isEmpty()) score += comorbidities.size();
        if (contraindications != null && !contraindications.isEmpty()) score += contraindications.size();
        if (treatmentOptions != null && treatmentOptions.size() > 3) score += 1;

        // Add for special populations
        if (isPediatricCase() || isGeriatricCase()) score += 1;

        // Add for urgency
        if (isEmergencyDecision()) score += 2;
        else if ("HIGH".equals(urgency)) score += 1;

        // Add for decision type complexity
        if ("TREATMENT".equals(decisionType) || "DIAGNOSIS".equals(decisionType)) score += 1;

        return Math.min(score, 10); // Cap at 10
    }

    /**
     * Get all relevant medical conditions
     * @return Set of all medical conditions mentioned
     */
    public Set<String> getAllMedicalConditions() {
        Set<String> conditions = new HashSet<>();

        if (comorbidities != null) {
            conditions.addAll(comorbidities);
        }

        // Parse medical history for conditions
        if (medicalHistory != null && !medicalHistory.isEmpty()) {
            // Simple extraction - in practice would use NLP
            String[] words = medicalHistory.toLowerCase().split("[\\s,;]+");
            for (String word : words) {
                if (word.length() > 4) { // Filter very short words
                    conditions.add(word);
                }
            }
        }

        return conditions;
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "ClinicalDecisionRequest{" +
                "clinicalScenario='" + (clinicalScenario != null ?
                clinicalScenario.substring(0, Math.min(100, clinicalScenario.length())) + "..." : null) + '\'' +
                ", decisionType='" + decisionType + '\'' +
                ", urgency='" + urgency + '\'' +
                ", specialty='" + specialty + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", complexityScore=" + getComplexityScore() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalDecisionRequest that = (ClinicalDecisionRequest) o;

        if (clinicalScenario != null ? !clinicalScenario.equals(that.clinicalScenario) : that.clinicalScenario != null)
            return false;
        if (decisionType != null ? !decisionType.equals(that.decisionType) : that.decisionType != null) return false;
        if (urgency != null ? !urgency.equals(that.urgency) : that.urgency != null) return false;
        return specialty != null ? specialty.equals(that.specialty) : that.specialty == null;
    }

    @Override
    public int hashCode() {
        int result = clinicalScenario != null ? clinicalScenario.hashCode() : 0;
        result = 31 * result + (decisionType != null ? decisionType.hashCode() : 0);
        result = 31 * result + (urgency != null ? urgency.hashCode() : 0);
        result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
        return result;
    }
}