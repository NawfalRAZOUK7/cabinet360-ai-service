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
 * 💊 Drug Interaction Response DTO
 * Contains comprehensive drug interaction analysis results
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugInteractionResponse {

    private List<DrugInteraction> majorInteractions;
    private List<DrugInteraction> moderateInteractions;
    private List<DrugInteraction> minorInteractions;
    private List<FoodDrugInteraction> foodInteractions;
    private String overallRiskAssessment;
    private String riskLevel; // LOW, MODERATE, HIGH, CRITICAL
    private List<String> recommendations;
    private List<String> monitoringRecommendations;
    private List<String> alternativeOptions;
    private String pharmacistConsultation;
    private String disclaimer;
    private InteractionSummary summary;
    private List<String> emergencyWarnings;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String analysisVersion;
    private String dataSource;
    private Map<String, Object> metadata;

    // ================================
    // Constructors
    // ================================

    /**
     * Default constructor
     */
    public DrugInteractionResponse() {
        this.majorInteractions = new ArrayList<>();
        this.moderateInteractions = new ArrayList<>();
        this.minorInteractions = new ArrayList<>();
        this.foodInteractions = new ArrayList<>();
        this.recommendations = new ArrayList<>();
        this.monitoringRecommendations = new ArrayList<>();
        this.alternativeOptions = new ArrayList<>();
        this.emergencyWarnings = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.timestamp = LocalDateTime.now();
        this.analysisVersion = "1.0";
        this.dataSource = "Cabinet360-AI";
    }

    /**
     * Constructor with interaction lists
     * @param major Major interactions
     * @param moderate Moderate interactions
     * @param minor Minor interactions
     */
    public DrugInteractionResponse(@NotNull List<DrugInteraction> major,
                                   @NotNull List<DrugInteraction> moderate,
                                   @NotNull List<DrugInteraction> minor) {
        this();
        this.majorInteractions = new ArrayList<>(major);
        this.moderateInteractions = new ArrayList<>(moderate);
        this.minorInteractions = new ArrayList<>(minor);
        this.generateSummary();
        this.assessOverallRisk();
    }

    /**
     * Constructor with risk assessment
     * @param major Major interactions
     * @param moderate Moderate interactions
     * @param minor Minor interactions
     * @param overallRisk Overall risk assessment
     */
    public DrugInteractionResponse(@NotNull List<DrugInteraction> major,
                                   @NotNull List<DrugInteraction> moderate,
                                   @NotNull List<DrugInteraction> minor,
                                   String overallRisk) {
        this(major, moderate, minor);
        this.overallRiskAssessment = overallRisk;
        this.determineRiskLevel();
    }

    // ================================
    // Static Factory Methods
    // ================================

    /**
     * Create error response
     * @param message Error message
     * @return Error response
     */
    public static DrugInteractionResponse error(String message) {
        DrugInteractionResponse response = new DrugInteractionResponse();
        response.disclaimer = "Error: " + message;
        response.riskLevel = "UNKNOWN";
        response.overallRiskAssessment = "Unable to complete interaction analysis due to: " + message;
        return response;
    }

    /**
     * Create no interactions found response
     * @param medications List of medications checked
     * @return No interactions response
     */
    public static DrugInteractionResponse noInteractionsFound(List<String> medications) {
        DrugInteractionResponse response = new DrugInteractionResponse();
        response.riskLevel = "LOW";
        response.overallRiskAssessment = "No significant drug-drug interactions identified among the " +
                medications.size() + " medications reviewed.";
        response.recommendations.add("Continue current medication regimen");
        response.recommendations.add("Monitor for any new symptoms or side effects");
        response.recommendations.add("Inform healthcare providers of all medications when seeking new treatments");
        response.disclaimer = "This analysis is based on known drug interactions. Always consult with a pharmacist or healthcare provider for personalized advice.";
        return response;
    }

    // ================================
    // Getters and Setters
    // ================================

    /**
     * Get major interactions
     * @return List of major (contraindicated) interactions
     */
    public List<DrugInteraction> getMajorInteractions() {
        return majorInteractions;
    }

    /**
     * Set major interactions
     * @param majorInteractions Contraindicated drug interactions
     */
    public void setMajorInteractions(List<DrugInteraction> majorInteractions) {
        this.majorInteractions = majorInteractions != null ?
                new ArrayList<>(majorInteractions) : new ArrayList<>();
    }

    /**
     * Get moderate interactions
     * @return List of moderate interactions requiring monitoring
     */
    public List<DrugInteraction> getModerateInteractions() {
        return moderateInteractions;
    }

    /**
     * Set moderate interactions
     * @param moderateInteractions Interactions requiring close monitoring
     */
    public void setModerateInteractions(List<DrugInteraction> moderateInteractions) {
        this.moderateInteractions = moderateInteractions != null ?
                new ArrayList<>(moderateInteractions) : new ArrayList<>();
    }

    /**
     * Get minor interactions
     * @return List of minor interactions with minimal clinical significance
     */
    public List<DrugInteraction> getMinorInteractions() {
        return minorInteractions;
    }

    /**
     * Set minor interactions
     * @param minorInteractions Interactions with minimal clinical impact
     */
    public void setMinorInteractions(List<DrugInteraction> minorInteractions) {
        this.minorInteractions = minorInteractions != null ?
                new ArrayList<>(minorInteractions) : new ArrayList<>();
    }

    /**
     * Get food-drug interactions
     * @return List of food-drug interactions
     */
    public List<FoodDrugInteraction> getFoodInteractions() {
        return foodInteractions;
    }

    /**
     * Set food-drug interactions
     * @param foodInteractions Food and drug interactions
     */
    public void setFoodInteractions(List<FoodDrugInteraction> foodInteractions) {
        this.foodInteractions = foodInteractions != null ?
                new ArrayList<>(foodInteractions) : new ArrayList<>();
    }

    /**
     * Get overall risk assessment
     * @return Comprehensive risk analysis narrative
     */
    public String getOverallRiskAssessment() {
        return overallRiskAssessment;
    }

    /**
     * Set overall risk assessment
     * @param overallRiskAssessment Clinical risk evaluation
     */
    public void setOverallRiskAssessment(String overallRiskAssessment) {
        this.overallRiskAssessment = overallRiskAssessment;
    }

    /**
     * Get risk level
     * @return Risk classification (LOW, MODERATE, HIGH, CRITICAL)
     */
    public String getRiskLevel() {
        return riskLevel;
    }

    /**
     * Set risk level
     * @param riskLevel Overall risk classification
     */
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    /**
     * Get recommendations
     * @return List of clinical recommendations
     */
    public List<String> getRecommendations() {
        return recommendations;
    }

    /**
     * Set recommendations
     * @param recommendations Clinical management recommendations
     */
    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations != null ?
                new ArrayList<>(recommendations) : new ArrayList<>();
    }

    /**
     * Get monitoring recommendations
     * @return List of monitoring parameters
     */
    public List<String> getMonitoringRecommendations() {
        return monitoringRecommendations;
    }

    /**
     * Set monitoring recommendations
     * @param monitoringRecommendations Parameters to monitor
     */
    public void setMonitoringRecommendations(List<String> monitoringRecommendations) {
        this.monitoringRecommendations = monitoringRecommendations != null ?
                new ArrayList<>(monitoringRecommendations) : new ArrayList<>();
    }

    /**
     * Get alternative options
     * @return List of alternative medication options
     */
    public List<String> getAlternativeOptions() {
        return alternativeOptions;
    }

    /**
     * Set alternative options
     * @param alternativeOptions Alternative medication suggestions
     */
    public void setAlternativeOptions(List<String> alternativeOptions) {
        this.alternativeOptions = alternativeOptions != null ?
                new ArrayList<>(alternativeOptions) : new ArrayList<>();
    }

    /**
     * Get pharmacist consultation recommendation
     * @return Pharmacist consultation advice
     */
    public String getPharmacistConsultation() {
        return pharmacistConsultation;
    }

    /**
     * Set pharmacist consultation recommendation
     * @param pharmacistConsultation When to consult pharmacist
     */
    public void setPharmacistConsultation(String pharmacistConsultation) {
        this.pharmacistConsultation = pharmacistConsultation;
    }

    /**
     * Get disclaimer
     * @return Legal/clinical disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * Set disclaimer
     * @param disclaimer Medical disclaimer text
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     * Get interaction summary
     * @return Summary statistics
     */
    public InteractionSummary getSummary() {
        return summary;
    }

    /**
     * Set interaction summary
     * @param summary Summary of all interactions
     */
    public void setSummary(InteractionSummary summary) {
        this.summary = summary;
    }

    /**
     * Get emergency warnings
     * @return List of urgent warnings
     */
    public List<String> getEmergencyWarnings() {
        return emergencyWarnings;
    }

    /**
     * Set emergency warnings
     * @param emergencyWarnings Urgent clinical warnings
     */
    public void setEmergencyWarnings(List<String> emergencyWarnings) {
        this.emergencyWarnings = emergencyWarnings != null ?
                new ArrayList<>(emergencyWarnings) : new ArrayList<>();
    }

    /**
     * Get timestamp
     * @return Analysis timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp
     * @param timestamp When analysis was performed
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
     * Get data source
     * @return Source of interaction data
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Set data source
     * @param dataSource Where interaction data came from
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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
     * Add a major interaction
     * @param interaction Major interaction to add
     */
    public void addMajorInteraction(DrugInteraction interaction) {
        if (interaction != null) {
            this.majorInteractions.add(interaction);
        }
    }

    /**
     * Add a recommendation
     * @param recommendation Clinical recommendation to add
     */
    public void addRecommendation(String recommendation) {
        if (recommendation != null && !recommendation.trim().isEmpty()) {
            this.recommendations.add(recommendation.trim());
        }
    }

    /**
     * Add an emergency warning
     * @param warning Emergency warning to add
     */
    public void addEmergencyWarning(String warning) {
        if (warning != null && !warning.trim().isEmpty()) {
            this.emergencyWarnings.add(warning.trim());
        }
    }

    /**
     * Check if any critical interactions exist
     * @return true if major interactions or emergency warnings present
     */
    public boolean hasCriticalInteractions() {
        return (majorInteractions != null && !majorInteractions.isEmpty()) ||
                (emergencyWarnings != null && !emergencyWarnings.isEmpty());
    }

    /**
     * Get total interaction count
     * @return Total number of interactions
     */
    public int getTotalInteractionCount() {
        int count = 0;
        if (majorInteractions != null) count += majorInteractions.size();
        if (moderateInteractions != null) count += moderateInteractions.size();
        if (minorInteractions != null) count += minorInteractions.size();
        return count;
    }

    /**
     * Generate summary automatically
     */
    private void generateSummary() {
        this.summary = new InteractionSummary();
        this.summary.setMajorCount(majorInteractions != null ? majorInteractions.size() : 0);
        this.summary.setModerateCount(moderateInteractions != null ? moderateInteractions.size() : 0);
        this.summary.setMinorCount(minorInteractions != null ? minorInteractions.size() : 0);
        this.summary.setTotalCount(getTotalInteractionCount());
    }

    /**
     * Assess overall risk automatically
     */
    private void assessOverallRisk() {
        if (majorInteractions != null && !majorInteractions.isEmpty()) {
            this.riskLevel = "CRITICAL";
            this.overallRiskAssessment = "Critical drug interactions identified. Immediate review required.";
        } else if (moderateInteractions != null && moderateInteractions.size() > 2) {
            this.riskLevel = "HIGH";
            this.overallRiskAssessment = "Multiple moderate interactions requiring close monitoring.";
        } else if (moderateInteractions != null && !moderateInteractions.isEmpty()) {
            this.riskLevel = "MODERATE";
            this.overallRiskAssessment = "Moderate interactions present. Monitor patient closely.";
        } else if (minorInteractions != null && !minorInteractions.isEmpty()) {
            this.riskLevel = "LOW";
            this.overallRiskAssessment = "Minor interactions with minimal clinical significance.";
        } else {
            this.riskLevel = "LOW";
            this.overallRiskAssessment = "No significant drug interactions identified.";
        }
    }

    /**
     * Determine risk level from interaction severity
     */
    private void determineRiskLevel() {
        if (this.riskLevel == null) {
            assessOverallRisk();
        }
    }

    // ================================
    // Inner Classes
    // ================================

    /**
     * Individual drug interaction details
     */
    public static class DrugInteraction {
        private String drug1;
        private String drug2;
        private String severity; // MAJOR, MODERATE, MINOR
        private String mechanism;
        private String clinicalSignificance;
        private String management;
        private String onset; // RAPID, DELAYED
        private String documentation; // ESTABLISHED, PROBABLE, SUSPECTED
        private List<String> symptoms;
        private String recommendation;

        // Constructors
        public DrugInteraction() {
            this.symptoms = new ArrayList<>();
        }

        public DrugInteraction(String drug1, String drug2, String severity) {
            this();
            this.drug1 = drug1;
            this.drug2 = drug2;
            this.severity = severity;
        }

        // Getters and Setters
        public String getDrug1() { return drug1; }
        public void setDrug1(String drug1) { this.drug1 = drug1; }

        public String getDrug2() { return drug2; }
        public void setDrug2(String drug2) { this.drug2 = drug2; }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }

        public String getMechanism() { return mechanism; }
        public void setMechanism(String mechanism) { this.mechanism = mechanism; }

        public String getClinicalSignificance() { return clinicalSignificance; }
        public void setClinicalSignificance(String clinicalSignificance) { this.clinicalSignificance = clinicalSignificance; }

        public String getManagement() { return management; }
        public void setManagement(String management) { this.management = management; }

        public String getOnset() { return onset; }
        public void setOnset(String onset) { this.onset = onset; }

        public String getDocumentation() { return documentation; }
        public void setDocumentation(String documentation) { this.documentation = documentation; }

        public List<String> getSymptoms() { return symptoms; }
        public void setSymptoms(List<String> symptoms) {
            this.symptoms = symptoms != null ? new ArrayList<>(symptoms) : new ArrayList<>();
        }

        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

        @Override
        public String toString() {
            return String.format("%s + %s (%s)", drug1, drug2, severity);
        }
    }

    /**
     * Food-drug interaction details
     */
    public static class FoodDrugInteraction {
        private String drug;
        private String food;
        private String effect;
        private String recommendation;
        private String timing;

        // Constructors
        public FoodDrugInteraction() {}

        public FoodDrugInteraction(String drug, String food, String effect) {
            this.drug = drug;
            this.food = food;
            this.effect = effect;
        }

        // Getters and Setters
        public String getDrug() { return drug; }
        public void setDrug(String drug) { this.drug = drug; }

        public String getFood() { return food; }
        public void setFood(String food) { this.food = food; }

        public String getEffect() { return effect; }
        public void setEffect(String effect) { this.effect = effect; }

        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

        public String getTiming() { return timing; }
        public void setTiming(String timing) { this.timing = timing; }
    }

    /**
     * Summary statistics of interactions
     */
    public static class InteractionSummary {
        private int majorCount;
        private int moderateCount;
        private int minorCount;
        private int totalCount;
        private String riskCategory;
        private boolean requiresImmediateAttention;

        // Constructors
        public InteractionSummary() {}

        // Getters and Setters
        public int getMajorCount() { return majorCount; }
        public void setMajorCount(int majorCount) {
            this.majorCount = majorCount;
            this.requiresImmediateAttention = majorCount > 0;
        }

        public int getModerateCount() { return moderateCount; }
        public void setModerateCount(int moderateCount) { this.moderateCount = moderateCount; }

        public int getMinorCount() { return minorCount; }
        public void setMinorCount(int minorCount) { this.minorCount = minorCount; }

        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

        public String getRiskCategory() { return riskCategory; }
        public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }

        public boolean isRequiresImmediateAttention() { return requiresImmediateAttention; }
        public void setRequiresImmediateAttention(boolean requiresImmediateAttention) {
            this.requiresImmediateAttention = requiresImmediateAttention;
        }
    }

    // ================================
    // Object Methods
    // ================================

    @Override
    public String toString() {
        return "DrugInteractionResponse{" +
                "riskLevel='" + riskLevel + '\'' +
                ", totalInteractions=" + getTotalInteractionCount() +
                ", majorInteractions=" + (majorInteractions != null ? majorInteractions.size() : 0) +
                ", moderateInteractions=" + (moderateInteractions != null ? moderateInteractions.size() : 0) +
                ", minorInteractions=" + (minorInteractions != null ? minorInteractions.size() : 0) +
                ", timestamp=" + timestamp +
                '}';
    }
}