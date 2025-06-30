package com.cabinet360.ai.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.ArrayList;

/**
 * 💊 Drug Interaction Request DTO
 * Used for checking drug-drug interactions and clinical pharmacy consultations
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
public class DrugInteractionRequest {

    @NotEmpty(message = "Medications list cannot be empty")
    @Size(min = 2, max = 10, message = "Please provide between 2-10 medications for interaction checking")
    private List<String> medications;

    @Size(max = 1000, message = "Patient conditions description too long")
    private String patientConditions;

    @Pattern(regexp = "^\\d{1,3}$|^$", message = "Patient age must be a valid number (1-150)")
    private String patientAge;

    @Pattern(regexp = "^\\d{1,3}(\\.\\d{1,2})?$|^$", message = "Patient weight must be a valid number")
    private String patientWeight;

    @Size(max = 500, message = "Allergies description too long")
    private String allergies;

    @Size(max = 10, message = "Too many current symptoms listed")
    private List<String> currentSymptoms;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)?$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String patientGender;

    @Size(max = 500, message = "Kidney function description too long")
    private String kidneyFunction;

    @Size(max = 500, message = "Liver function description too long")
    private String liverFunction;

    @Pattern(regexp = "^(LOW|MODERATE|HIGH)?$", message = "Urgency must be LOW, MODERATE, or HIGH")
    private String urgency;

    @Size(max = 200, message = "Additional notes too long")
    private String additionalNotes;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public DrugInteractionRequest() {
        this.medications = new ArrayList<>();
        this.currentSymptoms = new ArrayList<>();
    }

    /**
     * Constructor with medications list
     * @param medications List of medications to check for interactions
     */
    public DrugInteractionRequest(@NotNull List<String> medications) {
        this.medications = new ArrayList<>(medications);
        this.currentSymptoms = new ArrayList<>();
    }

    /**
     * Constructor with medications and patient conditions
     * @param medications List of medications
     * @param patientConditions Patient's medical conditions
     */
    public DrugInteractionRequest(@NotNull List<String> medications, String patientConditions) {
        this.medications = new ArrayList<>(medications);
        this.patientConditions = patientConditions;
        this.currentSymptoms = new ArrayList<>();
    }

    // ================================
    // Getters and Setters
    // ================================

    /**
     * Get the list of medications to check for interactions
     * @return List of medication names
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Set the list of medications
     * @param medications List of medication names
     */
    public void setMedications(List<String> medications) {
        this.medications = medications != null ? new ArrayList<>(medications) : new ArrayList<>();
    }

    /**
     * Get patient's medical conditions
     * @return Patient conditions as string
     */
    public String getPatientConditions() {
        return patientConditions;
    }

    /**
     * Set patient's medical conditions
     * @param patientConditions Medical conditions description
     */
    public void setPatientConditions(String patientConditions) {
        this.patientConditions = patientConditions;
    }

    /**
     * Get patient's age
     * @return Patient age as string
     */
    public String getPatientAge() {
        return patientAge;
    }

    /**
     * Set patient's age
     * @param patientAge Age in years
     */
    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    /**
     * Get patient's weight
     * @return Patient weight as string
     */
    public String getPatientWeight() {
        return patientWeight;
    }

    /**
     * Set patient's weight
     * @param patientWeight Weight in kg
     */
    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    /**
     * Get patient's allergies
     * @return Allergies description
     */
    public String getAllergies() {
        return allergies;
    }

    /**
     * Set patient's allergies
     * @param allergies Known allergies description
     */
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    /**
     * Get current symptoms
     * @return List of current symptoms
     */
    public List<String> getCurrentSymptoms() {
        return currentSymptoms;
    }

    /**
     * Set current symptoms
     * @param currentSymptoms List of symptoms
     */
    public void setCurrentSymptoms(List<String> currentSymptoms) {
        this.currentSymptoms = currentSymptoms != null ? new ArrayList<>(currentSymptoms) : new ArrayList<>();
    }

    /**
     * Get patient's gender
     * @return Patient gender
     */
    public String getPatientGender() {
        return patientGender;
    }

    /**
     * Set patient's gender
     * @param patientGender Gender (MALE, FEMALE, OTHER)
     */
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    /**
     * Get kidney function status
     * @return Kidney function description
     */
    public String getKidneyFunction() {
        return kidneyFunction;
    }

    /**
     * Set kidney function status
     * @param kidneyFunction Renal function description
     */
    public void setKidneyFunction(String kidneyFunction) {
        this.kidneyFunction = kidneyFunction;
    }

    /**
     * Get liver function status
     * @return Liver function description
     */
    public String getLiverFunction() {
        return liverFunction;
    }

    /**
     * Set liver function status
     * @param liverFunction Hepatic function description
     */
    public void setLiverFunction(String liverFunction) {
        this.liverFunction = liverFunction;
    }

    /**
     * Get urgency level
     * @return Urgency level (LOW, MODERATE, HIGH)
     */
    public String getUrgency() {
        return urgency;
    }

    /**
     * Set urgency level
     * @param urgency Clinical urgency level
     */
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    /**
     * Get additional notes
     * @return Additional clinical notes
     */
    public String getAdditionalNotes() {
        return additionalNotes;
    }

    /**
     * Set additional notes
     * @param additionalNotes Extra clinical information
     */
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    // ================================
    // Utility Methods
    // ================================

    /**
     * Add a medication to the list
     * @param medication Medication name to add
     */
    public void addMedication(String medication) {
        if (medication != null && !medication.trim().isEmpty()) {
            if (this.medications == null) {
                this.medications = new ArrayList<>();
            }
            this.medications.add(medication.trim());
        }
    }

    /**
     * Add a symptom to the current symptoms list
     * @param symptom Symptom to add
     */
    public void addSymptom(String symptom) {
        if (symptom != null && !symptom.trim().isEmpty()) {
            if (this.currentSymptoms == null) {
                this.currentSymptoms = new ArrayList<>();
            }
            this.currentSymptoms.add(symptom.trim());
        }
    }

    /**
     * Check if this is a high-risk interaction check
     * @return true if high urgency or multiple medications
     */
    public boolean isHighRiskCheck() {
        return "HIGH".equals(urgency) ||
                (medications != null && medications.size() > 5) ||
                (patientAge != null && !patientAge.isEmpty() &&
                        (Integer.parseInt(patientAge) > 65 || Integer.parseInt(patientAge) < 18));
    }

    /**
     * Get the number of medications
     * @return Medication count
     */
    public int getMedicationCount() {
        return medications != null ? medications.size() : 0;
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "DrugInteractionRequest{" +
                "medications=" + medications +
                ", patientConditions='" + patientConditions + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", patientWeight='" + patientWeight + '\'' +
                ", urgency='" + urgency + '\'' +
                ", medicationCount=" + getMedicationCount() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugInteractionRequest that = (DrugInteractionRequest) o;

        if (medications != null ? !medications.equals(that.medications) : that.medications != null) return false;
        if (patientConditions != null ? !patientConditions.equals(that.patientConditions) : that.patientConditions != null)
            return false;
        if (patientAge != null ? !patientAge.equals(that.patientAge) : that.patientAge != null) return false;
        return patientWeight != null ? patientWeight.equals(that.patientWeight) : that.patientWeight == null;
    }

    @Override
    public int hashCode() {
        int result = medications != null ? medications.hashCode() : 0;
        result = 31 * result + (patientConditions != null ? patientConditions.hashCode() : 0);
        result = 31 * result + (patientAge != null ? patientAge.hashCode() : 0);
        result = 31 * result + (patientWeight != null ? patientWeight.hashCode() : 0);
        return result;
    }
}