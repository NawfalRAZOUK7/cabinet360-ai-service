package com.cabinet360.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

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
     * AI-specific health endpoint - /api/v1/ai/health
     */
    @GetMapping("/api/v1/ai/health")
    public ResponseEntity<Map<String, Object>> aiHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ai-service",
                "module", "ai",
                "timestamp", LocalDateTime.now(),
                "version", "1.0.0"
        ));
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
                        "pubmed", "Search and analyze medical literature",
                        "nlp", "Natural language processing for medical texts"
                ),
                "providers", Map.of(
                        "huggingface", "Free Hugging Face models",
                        "gemini", "Google Gemini API",
                        "ollama", "Local Ollama models"
                ),
                "timestamp", LocalDateTime.now()
        ));
    }
}