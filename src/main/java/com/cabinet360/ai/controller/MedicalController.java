package com.cabinet360.ai.controller;

import com.cabinet360.ai.dto.request.*;
import com.cabinet360.ai.dto.response.*;
import com.cabinet360.ai.service.MedicalAiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 🏥 Advanced Medical AI Controller
 * Provides specialized medical features: drug interactions, differential diagnosis, clinical decision support
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@RestController
@RequestMapping("/api/v1/ai/medical")
@PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
@CrossOrigin(origins = {"http://localhost:3000", "https://cabinet360.com"})
public class MedicalController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalController.class);

    private final MedicalAiService medicalAiService;

    @Value("${dev.mode:false}")
    private boolean devMode;

    @Value("${ai.medical.emergency.keywords}")
    private String emergencyKeywords;

    public MedicalController(MedicalAiService medicalAiService) {
        this.medicalAiService = medicalAiService;
    }

    // ================================
    // 💊 Drug Interaction Endpoints
    // ================================

    /**
     * Check drug-drug interactions
     */
    @PostMapping("/drug-interactions")
    public ResponseEntity<DrugInteractionResponse> checkDrugInteractions(
            @Valid @RequestBody DrugInteractionRequest request,
            HttpServletRequest httpRequest) {

        Long userId = getUserId(httpRequest);
        String userEmail = getUserEmail(httpRequest);

        logger.info("Drug interaction check requested by user: {} for {} medications",
                userEmail, request.getMedications().size());

        try {
            // Log high-risk requests
            if (request.isHighRiskCheck()) {
                logger.warn("High-risk drug interaction check: user={}, medications={}, urgency={}",
                        userEmail, request.getMedications(), request.getUrgency());
            }

            DrugInteractionResponse response = medicalAiService.analyzeDrugInteractions(request, userId);

            // Log critical interactions
            if (response.hasCriticalInteractions()) {
                logger.warn("CRITICAL drug interactions detected for user: {} - Risk Level: {}",
                        userEmail, response.getRiskLevel());
            }

            logDev("💊", "Drug Interaction Check",
                    String.format("Completed for %d medications, Risk: %s",
                            request.getMedicationCount(), response.getRiskLevel()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Drug interaction analysis failed for user: {} - Error: {}", userEmail, e.getMessage(), e);

            DrugInteractionResponse errorResponse = DrugInteractionResponse.error(
                    "Unable to complete drug interaction analysis. Please try again or consult a pharmacist.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Quick drug interaction check (simplified)
     */
    @PostMapping("/drug-interactions/quick")
    public ResponseEntity<DrugInteractionResponse> quickDrugCheck(
            @RequestBody Map<String, Object> requestData,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);
        Long userId = getUserId(httpRequest);

        try {
            @SuppressWarnings("unchecked")
            List<String> medications = (List<String>) requestData.get("medications");

            if (medications == null || medications.size() < 2) {
                return ResponseEntity.badRequest().body(
                        DrugInteractionResponse.error("At least 2 medications required for interaction checking"));
            }

            DrugInteractionRequest request = new DrugInteractionRequest(medications);
            request.setUrgency("LOW");

            DrugInteractionResponse response = medicalAiService.analyzeDrugInteractions(request, userId);

            logDev("💊", "Quick Drug Check",
                    String.format("Completed for %d medications", medications.size()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Quick drug check failed for user: {} - Error: {}", userEmail, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DrugInteractionResponse.error("Quick drug check failed"));
        }
    }

    // ================================
    // 🎯 Differential Diagnosis Endpoints
    // ================================

    /**
     * Generate differential diagnosis
     */
    @PostMapping("/differential-diagnosis")
    public ResponseEntity<DifferentialDiagnosisResponse> generateDifferentialDiagnosis(
            @Valid @RequestBody DifferentialDiagnosisRequest request,
            HttpServletRequest httpRequest) {

        Long userId = getUserId(httpRequest);
        String userEmail = getUserEmail(httpRequest);

        logger.info("Differential diagnosis requested by user: {} for specialty: {}",
                userEmail, request.getSpecialty());

        try {
            // Check for emergency symptoms
            if (containsEmergencyKeywords(request.getSymptoms())) {
                logger.warn("EMERGENCY symptoms detected in differential diagnosis request from user: {}", userEmail);

                DifferentialDiagnosisResponse emergencyResponse =
                        DifferentialDiagnosisResponse.emergency(request.getSymptoms());

                return ResponseEntity.ok(emergencyResponse);
            }

            // Log complex cases
            if (request.getComplexityScore() > 7) {
                logger.info("Complex differential diagnosis case: user={}, complexity={}, emergency={}",
                        userEmail, request.getComplexityScore(), request.isEmergencyCase());
            }

            DifferentialDiagnosisResponse response =
                    medicalAiService.generateDifferentialDiagnosis(request, userId);

            // Log urgent cases
            if (response.requiresImmediateAttention()) {
                logger.warn("Urgent differential diagnosis generated for user: {} - Urgency: {}",
                        userEmail, response.getUrgencyLevel());
            }

            logDev("🎯", "Differential Diagnosis",
                    String.format("Generated %d diagnoses, Urgency: %s",
                            response.getDiagnoses().size(), response.getUrgencyLevel()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Differential diagnosis failed for user: {} - Error: {}", userEmail, e.getMessage(), e);

            DifferentialDiagnosisResponse errorResponse = DifferentialDiagnosisResponse.error(
                    "Unable to generate differential diagnosis. Please consult with medical colleagues.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Quick symptom assessment
     */
    @PostMapping("/differential-diagnosis/quick")
    public ResponseEntity<DifferentialDiagnosisResponse> quickSymptomAssessment(
            @RequestBody Map<String, String> requestData,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);
        Long userId = getUserId(httpRequest);

        try {
            String symptoms = requestData.get("symptoms");
            String patientAge = requestData.get("patientAge");
            String urgency = requestData.getOrDefault("urgency", "MODERATE");

            if (symptoms == null || symptoms.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        DifferentialDiagnosisResponse.error("Symptoms description is required"));
            }

            // Check for emergency keywords
            if (containsEmergencyKeywords(symptoms)) {
                return ResponseEntity.ok(DifferentialDiagnosisResponse.emergency(symptoms));
            }

            DifferentialDiagnosisRequest request = new DifferentialDiagnosisRequest(symptoms);
            request.setPatientAge(patientAge);
            request.setUrgency(urgency);
            request.setAnalysisDepth("BASIC");

            DifferentialDiagnosisResponse response =
                    medicalAiService.generateDifferentialDiagnosis(request, userId);

            logDev("🎯", "Quick Assessment", "Completed basic differential diagnosis");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Quick symptom assessment failed for user: {} - Error: {}", userEmail, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DifferentialDiagnosisResponse.error("Quick assessment failed"));
        }
    }

    // ================================
    // 🩺 Clinical Decision Support Endpoints
    // ================================

    /**
     * Get clinical decision support
     */
    @PostMapping("/clinical-decision")
    public ResponseEntity<ClinicalDecisionResponse> getClinicalDecisionSupport(
            @Valid @RequestBody ClinicalDecisionRequest request,
            HttpServletRequest httpRequest) {

        Long userId = getUserId(httpRequest);
        String userEmail = getUserEmail(httpRequest);

        logger.info("Clinical decision support requested by user: {} for decision type: {}",
                userEmail, request.getDecisionType());

        try {
            // Check for emergency decision
            if (request.isEmergencyDecision()) {
                logger.warn("EMERGENCY clinical decision requested by user: {}", userEmail);

                ClinicalDecisionResponse emergencyResponse =
                        ClinicalDecisionResponse.emergency(request.getClinicalScenario());

                return ResponseEntity.ok(emergencyResponse);
            }

            // Log complex decisions
            if (request.getComplexityScore() > 7) {
                logger.info("Complex clinical decision: user={}, complexity={}, type={}",
                        userEmail, request.getComplexityScore(), request.getDecisionType());
            }

            ClinicalDecisionResponse response =
                    medicalAiService.provideClinicalDecisionSupport(request, userId);

            // Log emergency decisions
            if (response.isEmergencyDecision()) {
                logger.warn("Emergency clinical decision generated for user: {} - Type: {}",
                        userEmail, response.getDecisionType());
            }

            logDev("🩺", "Clinical Decision",
                    String.format("Provided %s decision support, Confidence: %s",
                            request.getDecisionType(), response.getConfidenceLevel()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Clinical decision support failed for user: {} - Error: {}", userEmail, e.getMessage(), e);

            ClinicalDecisionResponse errorResponse = ClinicalDecisionResponse.error(
                    "Unable to provide clinical decision support. Please consult with medical colleagues.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get treatment recommendations
     */
    @PostMapping("/treatment-recommendations")
    public ResponseEntity<ClinicalDecisionResponse> getTreatmentRecommendations(
            @RequestBody Map<String, Object> requestData,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);
        Long userId = getUserId(httpRequest);

        try {
            String condition = (String) requestData.get("condition");
            String patientData = (String) requestData.get("patientData");
            String urgency = (String) requestData.getOrDefault("urgency", "MODERATE");

            if (condition == null || condition.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        ClinicalDecisionResponse.error("Medical condition is required"));
            }

            ClinicalDecisionRequest request = new ClinicalDecisionRequest(
                    "Treatment recommendations needed for: " + condition);
            request.setDecisionType("TREATMENT");
            request.setPatientData(patientData);
            request.setUrgency(urgency);
            request.setAnalysisDepth("STANDARD");

            ClinicalDecisionResponse response =
                    medicalAiService.provideClinicalDecisionSupport(request, userId);

            logDev("💊", "Treatment Recommendations",
                    String.format("Provided for condition: %s", condition));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Treatment recommendations failed for user: {} - Error: {}", userEmail, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ClinicalDecisionResponse.error("Treatment recommendations failed"));
        }
    }

    // ================================
    // 📊 Medical Analytics Endpoints
    // ================================

    /**
     * Get medical service usage statistics
     */
    @GetMapping("/analytics/usage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getMedicalUsageAnalytics(
            @RequestParam(defaultValue = "30") int days,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);

        logger.info("Medical usage analytics requested by admin: {} for {} days", userEmail, days);

        try {
            Map<String, Object> analytics = medicalAiService.getUsageAnalytics(days);

            logDev("📊", "Usage Analytics", String.format("Generated for %d days", days));

            return ResponseEntity.ok(analytics);

        } catch (Exception e) {
            logger.error("Medical usage analytics failed for admin: {} - Error: {}", userEmail, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to generate analytics");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get accuracy metrics for medical AI
     */
    @GetMapping("/analytics/accuracy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAccuracyMetrics(
            @RequestParam(defaultValue = "30") int days,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);

        logger.info("Accuracy metrics requested by admin: {} for {} days", userEmail, days);

        try {
            Map<String, Object> metrics = medicalAiService.getAccuracyMetrics(days);

            logDev("📈", "Accuracy Metrics", String.format("Generated for %d days", days));

            return ResponseEntity.ok(metrics);

        } catch (Exception e) {
            logger.error("Accuracy metrics failed for admin: {} - Error: {}", userEmail, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to generate accuracy metrics");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // 🔍 Medical Knowledge Endpoints
    // ================================

    /**
     * Search medical guidelines
     */
    @PostMapping("/guidelines/search")
    public ResponseEntity<Map<String, Object>> searchMedicalGuidelines(
            @RequestBody Map<String, String> requestData,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);
        Long userId = getUserId(httpRequest);

        try {
            String query = requestData.get("query");
            String specialty = requestData.get("specialty");
            String organization = requestData.getOrDefault("organization", "ALL");

            if (query == null || query.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Search query is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Map<String, Object> guidelines = medicalAiService.searchMedicalGuidelines(
                    query, specialty, organization, userId);

            logDev("📚", "Guidelines Search",
                    String.format("Query: %s, Specialty: %s", query, specialty));

            return ResponseEntity.ok(guidelines);

        } catch (Exception e) {
            logger.error("Medical guidelines search failed for user: {} - Error: {}", userEmail, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Guidelines search failed");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get risk calculators
     */
    @GetMapping("/risk-calculators")
    public ResponseEntity<Map<String, Object>> getRiskCalculators(
            @RequestParam(required = false) String specialty,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);

        logger.info("Risk calculators requested by user: {} for specialty: {}", userEmail, specialty);

        try {
            Map<String, Object> calculators = medicalAiService.getAvailableRiskCalculators(specialty);

            logDev("🧮", "Risk Calculators",
                    String.format("Retrieved for specialty: %s", specialty));

            return ResponseEntity.ok(calculators);

        } catch (Exception e) {
            logger.error("Risk calculators retrieval failed for user: {} - Error: {}", userEmail, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to retrieve risk calculators");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // 🚨 Emergency & Safety Endpoints
    // ================================

    /**
     * Emergency medical assessment
     */
    @PostMapping("/emergency/assess")
    public ResponseEntity<Map<String, Object>> emergencyAssessment(
            @RequestBody Map<String, String> requestData,
            HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);
        Long userId = getUserId(httpRequest);

        try {
            String symptoms = requestData.get("symptoms");
            String context = requestData.get("context");

            if (symptoms == null || symptoms.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Symptoms description is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            logger.warn("EMERGENCY ASSESSMENT requested by user: {} - Symptoms: {}", userEmail, symptoms);

            Map<String, Object> assessment = medicalAiService.performEmergencyAssessment(
                    symptoms, context, userId);

            // Always log emergency assessments
            logger.warn("Emergency assessment completed for user: {} - Risk Level: {}",
                    userEmail, assessment.get("riskLevel"));

            return ResponseEntity.ok(assessment);

        } catch (Exception e) {
            logger.error("Emergency assessment failed for user: {} - Error: {}", userEmail, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Emergency assessment failed");
            errorResponse.put("recommendation", "🚨 SEEK IMMEDIATE MEDICAL ATTENTION 🚨");
            errorResponse.put("message", "When in doubt, always seek emergency care");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // 🔧 Service Health & Configuration
    // ================================

    /**
     * Get medical AI service health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getMedicalServiceHealth() {

        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("service", "medical-ai");
            health.put("timestamp", LocalDateTime.now());
            health.put("version", "1.0.0");

            // Check medical AI service health
            boolean aiHealthy = medicalAiService.isServiceHealthy();
            health.put("aiServiceHealthy", aiHealthy);
            health.put("overallStatus", aiHealthy ? "HEALTHY" : "DEGRADED");

            // Add feature status
            Map<String, Boolean> features = new HashMap<>();
            features.put("drugInteractions", true);
            features.put("differentialDiagnosis", true);
            features.put("clinicalDecision", true);
            features.put("emergencyDetection", true);
            health.put("features", features);

            logDev("🏥", "Health Check", "Medical AI service health checked");

            return ResponseEntity.ok(health);

        } catch (Exception e) {
            logger.error("Medical service health check failed: {}", e.getMessage());

            Map<String, Object> errorHealth = new HashMap<>();
            errorHealth.put("status", "DOWN");
            errorHealth.put("service", "medical-ai");
            errorHealth.put("error", e.getMessage());
            errorHealth.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorHealth);
        }
    }

    /**
     * Get medical service configuration
     */
    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getMedicalServiceConfig(HttpServletRequest httpRequest) {

        String userEmail = getUserEmail(httpRequest);

        logger.info("Medical service configuration requested by admin: {}", userEmail);

        try {
            Map<String, Object> config = new HashMap<>();
            config.put("service", "Cabinet360 Medical AI");
            config.put("version", "1.0.0");
            config.put("timestamp", LocalDateTime.now());

            // AI Configuration
            Map<String, Object> aiConfig = new HashMap<>();
            aiConfig.put("provider", "gemini"); // From your config
            aiConfig.put("model", "gemini-pro");
            aiConfig.put("emergencyDetection", true);
            aiConfig.put("devMode", devMode);
            config.put("aiConfiguration", aiConfig);

            // Feature flags
            Map<String, Boolean> features = new HashMap<>();
            features.put("drugInteractionChecking", true);
            features.put("differentialDiagnosis", true);
            features.put("clinicalDecisionSupport", true);
            features.put("emergencyDetection", true);
            features.put("medicalGuidelines", true);
            features.put("riskCalculators", true);
            config.put("enabledFeatures", features);

            return ResponseEntity.ok(config);

        } catch (Exception e) {
            logger.error("Medical service config retrieval failed for admin: {} - Error: {}", userEmail, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to retrieve configuration");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // 🛠️ Helper Methods
    // ================================

    /**
     * Extract user ID from request
     */
    private Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        return userId != null ? (Long) userId : 1L; // Default for dev
    }

    /**
     * Extract user email from request
     */
    private String getUserEmail(HttpServletRequest request) {
        Object userEmail = request.getAttribute("userEmail");
        return userEmail != null ? (String) userEmail : "unknown@cabinet360.com";
    }

    /**
     * Check if text contains emergency keywords
     */
    private boolean containsEmergencyKeywords(String text) {
        if (text == null || emergencyKeywords == null) {
            return false;
        }

        String lowerText = text.toLowerCase();
        String[] keywords = emergencyKeywords.toLowerCase().split(",");

        for (String keyword : keywords) {
            if (lowerText.contains(keyword.trim())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Development mode logging
     */
    private void logDev(String emoji, String title, String message) {
        if (devMode) {
            System.out.println("🏥 Medical AI - " + emoji + " " + title + ": " + message);
        }
    }

    // ================================
    // 🚨 Exception Handling
    // ================================

    /**
     * Handle validation errors
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException e) {

        logger.warn("Validation error in medical controller: {}", e.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation failed");
        errorResponse.put("message", "Please check your input data");
        errorResponse.put("details", e.getBindingResult().getFieldErrors());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {

        logger.error("Unhandled exception in medical controller: {}", e.getMessage(), e);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal server error");
        errorResponse.put("message", "An unexpected error occurred in the medical AI service");
        errorResponse.put("timestamp", LocalDateTime.now());

        if (devMode) {
            errorResponse.put("details", e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}