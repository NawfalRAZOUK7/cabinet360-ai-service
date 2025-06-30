package com.cabinet360.ai.controller;

import com.cabinet360.ai.service.ChatbotService;
import com.cabinet360.ai.service.FreeAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@RestController
public class HealthController {

    private final ChatbotService chatbotService;
    private final FreeAiService freeAiService;

    @Value("${ai.provider:huggingface}")
    private String aiProvider;

    @Value("${gemini.model:gemini-pro}")
    private String geminiModel;

    public HealthController(ChatbotService chatbotService, FreeAiService freeAiService) {
        this.chatbotService = chatbotService;
        this.freeAiService = freeAiService;
    }

    /**
     * Root health endpoint - /health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ai-service",
                "timestamp", LocalDateTime.now(),
                "version", "1.0.0"
        ));
    }

    /**
     * Enhanced AI-specific health endpoint - /api/v1/ai/health
     */
    @GetMapping("/api/v1/ai/health")
    public ResponseEntity<Map<String, Object>> aiHealth() {
        Map<String, Object> healthData = new HashMap<>();

        // Basic service info
        healthData.put("service", "ai-service");
        healthData.put("module", "ai");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("version", "1.0.0");

        // AI Provider Configuration
        Map<String, Object> aiConfig = new HashMap<>();
        aiConfig.put("primaryProvider", aiProvider);
        aiConfig.put("model", getModelName());
        aiConfig.put("fallbackEnabled", true);

        // AI Service Status
        boolean aiServiceHealthy = false;
        String aiStatus = "DOWN";
        String aiMessage = "AI service unavailable";

        try {
            aiServiceHealthy = freeAiService.isServiceAvailable();
            if (aiServiceHealthy) {
                aiStatus = "UP";
                aiMessage = aiProvider + " AI service is operational";
            } else {
                aiMessage = aiProvider + " AI service is not responding";
            }
        } catch (Exception e) {
            aiMessage = "Error checking AI service: " + e.getMessage();
        }

        aiConfig.put("status", aiStatus);
        aiConfig.put("message", aiMessage);
        aiConfig.put("healthy", aiServiceHealthy);

        healthData.put("aiProvider", aiConfig);

        // Overall service status
        String overallStatus = aiServiceHealthy ? "UP" : "DEGRADED";
        healthData.put("status", overallStatus);

        return ResponseEntity.ok(healthData);
    }

    /**
     * Detailed AI service status endpoint
     */
    @GetMapping("/api/v1/ai/status")
    public ResponseEntity<Map<String, Object>> aiStatus() {
        Map<String, Object> statusData = new HashMap<>();

        // Test AI service
        boolean aiAvailable = false;
        String testResult = "Failed";
        String errorMessage = null;

        try {
            aiAvailable = freeAiService.isServiceAvailable();
            if (aiAvailable) {
                testResult = "Success";
            } else {
                testResult = "Service not responding";
            }
        } catch (Exception e) {
            testResult = "Error";
            errorMessage = e.getMessage();
        }

        statusData.put("timestamp", LocalDateTime.now());
        statusData.put("provider", Map.of(
                "name", aiProvider,
                "model", getModelName(),
                "available", aiAvailable,
                "testResult", testResult,
                "errorMessage", errorMessage
        ));

        // Chatbot service status
        boolean chatbotHealthy = false;
        try {
            chatbotHealthy = chatbotService.isAiServiceHealthy();
        } catch (Exception e) {
            // Ignore errors, just mark as unhealthy
        }

        statusData.put("services", Map.of(
                "chatbot", Map.of(
                        "status", chatbotHealthy ? "UP" : "DOWN",
                        "healthy", chatbotHealthy
                ),
                "pubmed", Map.of(
                        "status", "UP",
                        "healthy", true
                )
        ));

        statusData.put("overall", Map.of(
                "status", (aiAvailable && chatbotHealthy) ? "UP" : "DEGRADED",
                "healthy", aiAvailable && chatbotHealthy
        ));

        return ResponseEntity.ok(statusData);
    }

    /**
     * Service information endpoint
     */
    @GetMapping("/api/v1/ai/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "service", "Cabinet360 AI Service",
                "description", "Provides AI-powered medical assistance through chatbot and PubMed integration",
                "features", Map.of(
                        "chatbot", "Medical AI assistant for healthcare professionals",
                        "pubmed", "Search and analyze medical literature with AI summaries",
                        "nlp", "Natural language processing for medical texts",
                        "multiProvider", "Support for multiple AI providers with fallback"
                ),
                "providers", Map.of(
                        "gemini", "Google Gemini Pro (FREE tier available)",
                        "huggingface", "Free Hugging Face models",
                        "ollama", "Local Ollama models"
                ),
                "currentProvider", aiProvider,
                "currentModel", getModelName(),
                "timestamp", LocalDateTime.now()
        ));
    }

    /**
     * Quick AI test endpoint
     */
    @GetMapping("/api/v1/ai/test")
    public ResponseEntity<Map<String, Object>> testAi() {
        Map<String, Object> testData = new HashMap<>();
        testData.put("timestamp", LocalDateTime.now());
        testData.put("provider", aiProvider);
        testData.put("model", getModelName());

        try {
            // Quick test of AI service
            String testResponse = freeAiService.generateMedicalResponse(
                    "What is the normal heart rate?",
                    "Test query for health check"
            );

            boolean isValidResponse = testResponse != null &&
                    !testResponse.isEmpty() &&
                    !testResponse.contains("technical difficulties");

            testData.put("status", isValidResponse ? "SUCCESS" : "FAILED");
            testData.put("responseLength", testResponse != null ? testResponse.length() : 0);
            testData.put("responsePreview", testResponse != null ?
                    (testResponse.length() > 100 ? testResponse.substring(0, 100) + "..." : testResponse) :
                    "No response");
            testData.put("healthy", isValidResponse);

        } catch (Exception e) {
            testData.put("status", "ERROR");
            testData.put("error", e.getMessage());
            testData.put("healthy", false);
        }

        return ResponseEntity.ok(testData);
    }

    private String getModelName() {
        switch (aiProvider.toLowerCase()) {
            case "gemini":
                return geminiModel;
            case "huggingface":
                return "microsoft/DialoGPT-medium";
            case "ollama":
                return "medalpaca";
            default:
                return "unknown";
        }
    }
}