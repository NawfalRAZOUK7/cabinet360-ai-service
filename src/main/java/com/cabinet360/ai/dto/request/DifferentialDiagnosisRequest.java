package com.cabinet360.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.ArrayList;

/**
 * 🎯 Differential Diagnosis Request DTO
 * Used for AI-powered differential diagnosis generation based on clinical presentation
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
public class DifferentialDiagnosisRequest {

    @NotBlank(message = "Symptoms description cannot be empty")
    @Size(min = 10, max = 2000, message = "Symptoms description must be between 10 and 2000 characters")
    private String symptoms;

    @Size(max = 1000, message = "Patient data description too long")
    private String patientData;

    @Pattern(regexp = "^(CARDIOLOGY|NEUROLOGY|PSYCHIATRY|PEDIATRICS|GERIATRICS|EMERGENCY|FAMILY|INTERNAL|SURGERY|GYNECOLOGY|DERMATOLOGY|ONCOLOGY|ENDOCRINOLOGY|RHEUMATOLOGY|PULMONOLOGY|GASTROENTEROLOGY|NEPHROLOGY|INFECTIOUS|HEMATOLOGY)?$",
            message = "Invalid specialty. Must be a valid medical specialty.")
    private String specialty;

    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|EMERGENCY)?$",
            message = "Urgency must be LOW, MEDIUM, HIGH, or EMERGENCY")
    private String urgency;

    @Pattern(regexp = "^\\d{1,3}$|^$",
            message = "Patient age must be a valid number (0-150)")
    private String patientAge;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)?$",
            message = "Gender must be MALE, FEMALE, or OTHER")
    private String patientGender;

    @Size(max = 500, message = "Medical history too long")
    private String medicalHistory;

    @Size(max = 500, message = "Current medications list too long")
    private String currentMedications;

    @Size(max = 10, message = "Too many vital signs listed")
    private List<String> vitalSigns;

    @Size(max = 15, message = "Too many physical exam findings listed")
    private List<String> physicalExamFindings;

    @Size(max = 10, message = "Too many lab results listed")
    private List<String> labResults;

    @Size(max = 500, message = "Family history too long")
    private String familyHistory;

    @Size(max = 500, message = "Social history too long")
    private String socialHistory;

    @Size(max = 300, message = "Allergies description too long")
    private String allergies;

    @Pattern(regexp = "^(ACUTE|CHRONIC|SUBACUTE)?$",
            message = "Onset must be ACUTE, CHRONIC, or SUBACUTE")
    private String onsetType;

    @Size(max = 100, message = "Duration description too long")
    private String symptomDuration;

    @Size(max = 500, message = "Additional notes too long")
    private String additionalNotes;

    @Pattern(regexp = "^(BASIC|DETAILED|COMPREHENSIVE)?$",
            message = "Analysis depth must be BASIC, DETAILED, or COMPREHENSIVE")
    private String analysisDepth;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public DifferentialDiagnosisRequest() {
        this.vitalSigns = new ArrayList<>();
        this.physicalExamFindings = new ArrayList<>();
        this.labResults = new ArrayList<>();
    }

    /**
     * Constructor with symptoms only
     * @param symptoms Clinical symptoms description
     */
    public DifferentialDiagnosisRequest(@NotNull String symptoms) {
        this();
        this.symptoms = symptoms;
    }

    /**
     * Constructor with symptoms and patient data
     * @param symptoms Clinical symptoms description
     * @param patientData Additional patient information
     */
    public DifferentialDiagnosisRequest(@NotNull String symptoms, String patientData) {
        this();
        this.symptoms = symptoms;
        this.patientData = patientData;
    }

    /**
     * Constructor with core clinical data
     * @param symptoms Clinical symptoms
     * @param patientData Patient information
     * @param specialty Medical specialty
     * @param urgency Clinical urgency level
     */
    public DifferentialDiagnosisRequest(@NotNull String symptoms, String patientData,
                                        String specialty, String urgency) {
        this();
        this.symptoms = symptoms;
        this.patientData = patientData;
        this.specialty = specialty;
        this.urgency = urgency;
    }

    // ================================
    // Getters and Setters
    // ================================

    /**
     * Get symptoms description
     * @return Clinical symptoms
     */
    public String getSymptoms() {
        return symptoms;
    }

    /**
     * Set symptoms description
     * @param symptoms Clinical symptoms presentation
     */
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    /**
     * Get patient data
     * @return Additional patient information
     */
    public String getPatientData() {
        return patientData;
    }

    /**
     * Set patient data
     * @param patientData Patient demographic and clinical data
     */
    public void setPatientData(String patientData) {
        this.patientData = patientData;
    }

    /**
     * Get medical specialty
     * @return Specialty focus for differential
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Set medical specialty
     * @param specialty Medical specialty for focused differential
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * Get urgency level
     * @return Clinical urgency (LOW, MEDIUM, HIGH, EMERGENCY)
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
     * Get patient age
     * @return Patient age in years
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
     * Get vital signs
     * @return List of vital signs
     */
    public List<String> getVitalSigns() {
        return vitalSigns;
    }

    /**
     * Set vital signs
     * @param vitalSigns Vital signs measurements
     */
    public void setVitalSigns(List<String> vitalSigns) {
        this.vitalSigns = vitalSigns != null ? new ArrayList<>(vitalSigns) : new ArrayList<>();
    }

    /**
     * Get physical exam findings
     * @return List of physical examination findings
     */
    public List<String> getPhysicalExamFindings() {
        return physicalExamFindings;
    }

    /**
     * Set physical exam findings
     * @param physicalExamFindings Physical examination results
     */
    public void setPhysicalExamFindings(List<String> physicalExamFindings) {
        this.physicalExamFindings = physicalExamFindings != null ?
                new ArrayList<>(physicalExamFindings) : new ArrayList<>();
    }

    /**
     * Get lab results
     * @return List of laboratory results
     */
    public List<String> getLabResults() {
        return labResults;
    }

    /**
     * Set lab results
     * @param labResults Laboratory test results
     */
    public void setLabResults(List<String> labResults) {
        this.labResults = labResults != null ? new ArrayList<>(labResults) : new ArrayList<>();
    }

    /**
     * Get family history
     * @return Family medical history
     */
    public String getFamilyHistory() {
        return familyHistory;
    }

    /**
     * Set family history
     * @param familyHistory Family medical history
     */
    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    /**
     * Get social history
     * @return Social and lifestyle history
     */
    public String getSocialHistory() {
        return socialHistory;
    }

    /**
     * Set social history
     * @param socialHistory Social and lifestyle factors
     */
    public void setSocialHistory(String socialHistory) {
        this.socialHistory = socialHistory;
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
     * Get onset type
     * @return Symptom onset pattern (ACUTE, CHRONIC, SUBACUTE)
     */
    public String getOnsetType() {
        return onsetType;
    }

    /**
     * Set onset type
     * @param onsetType How symptoms began
     */
    public void setOnsetType(String onsetType) {
        this.onsetType = onsetType;
    }

    /**
     * Get symptom duration
     * @return How long symptoms have been present
     */
    public String getSymptomDuration() {
        return symptomDuration;
    }

    /**
     * Set symptom duration
     * @param symptomDuration Time course of symptoms
     */
    public void setSymptomDuration(String symptomDuration) {
        this.symptomDuration = symptomDuration;
    }

    /**
     * Get additional notes
     * @return Extra clinical information
     */
    public String getAdditionalNotes() {
        return additionalNotes;
    }

    /**
     * Set additional notes
     * @param additionalNotes Additional clinical observations
     */
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    /**
     * Get analysis depth
     * @return Depth of differential analysis requested
     */
    public String getAnalysisDepth() {
        return analysisDepth;
    }

    /**
     * Set analysis depth
     * @param analysisDepth Level of detail for analysis (BASIC, DETAILED, COMPREHENSIVE)
     */
    public void setAnalysisDepth(String analysisDepth) {
        this.analysisDepth = analysisDepth;
    }

    // ================================
    // Utility Methods
    // ================================

    /**
     * Add a vital sign measurement
     * @param vitalSign Vital sign to add
     */
    public void addVitalSign(String vitalSign) {
        if (vitalSign != null && !vitalSign.trim().isEmpty()) {
            if (this.vitalSigns == null) {
                this.vitalSigns = new ArrayList<>();
            }
            this.vitalSigns.add(vitalSign.trim());
        }
    }

    /**
     * Add a physical exam finding
     * @param finding Physical exam finding to add
     */
    public void addPhysicalExamFinding(String finding) {
        if (finding != null && !finding.trim().isEmpty()) {
            if (this.physicalExamFindings == null) {
                this.physicalExamFindings = new ArrayList<>();
            }
            this.physicalExamFindings.add(finding.trim());
        }
    }

    /**
     * Add a lab result
     * @param labResult Laboratory result to add
     */
    public void addLabResult(String labResult) {
        if (labResult != null && !labResult.trim().isEmpty()) {
            if (this.labResults == null) {
                this.labResults = new ArrayList<>();
            }
            this.labResults.add(labResult.trim());
        }
    }

    /**
     * Check if this is an emergency case
     * @return true if marked as emergency urgency
     */
    public boolean isEmergencyCase() {
        return "EMERGENCY".equals(urgency) || "HIGH".equals(urgency);
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
     * Get the complexity score of this case
     * @return Complexity score (0-10)
     */
    public int getComplexityScore() {
        int score = 0;

        // Base complexity from symptoms length
        if (symptoms != null && symptoms.length() > 500) score += 2;

        // Add for each data type present
        if (medicalHistory != null && !medicalHistory.isEmpty()) score += 1;
        if (vitalSigns != null && !vitalSigns.isEmpty()) score += 1;
        if (physicalExamFindings != null && !physicalExamFindings.isEmpty()) score += 1;
        if (labResults != null && !labResults.isEmpty()) score += 2;
        if (currentMedications != null && !currentMedications.isEmpty()) score += 1;

        // Add for special populations
        if (isPediatricCase() || isGeriatricCase()) score += 1;

        // Add for urgency
        if (isEmergencyCase()) score += 1;

        return Math.min(score, 10); // Cap at 10
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "DifferentialDiagnosisRequest{" +
                "symptoms='" + (symptoms != null ? symptoms.substring(0, Math.min(50, symptoms.length())) + "..." : null) + '\'' +
                ", specialty='" + specialty + '\'' +
                ", urgency='" + urgency + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", patientGender='" + patientGender + '\'' +
                ", complexityScore=" + getComplexityScore() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DifferentialDiagnosisRequest that = (DifferentialDiagnosisRequest) o;

        if (symptoms != null ? !symptoms.equals(that.symptoms) : that.symptoms != null) return false;
        if (patientData != null ? !patientData.equals(that.patientData) : that.patientData != null) return false;
        if (specialty != null ? !specialty.equals(that.specialty) : that.specialty != null) return false;
        return patientAge != null ? patientAge.equals(that.patientAge) : that.patientAge == null;
    }

    @Override
    public int hashCode() {
        int result = symptoms != null ? symptoms.hashCode() : 0;
        result = 31 * result + (patientData != null ? patientData.hashCode() : 0);
        result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
        result = 31 * result + (patientAge != null ? patientAge.hashCode() : 0);
        return result;
    }
}