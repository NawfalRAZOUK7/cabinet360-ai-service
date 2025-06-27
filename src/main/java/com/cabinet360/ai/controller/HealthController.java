package com.cabinet360.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ai-service",
                "timestamp", LocalDateTime.now(),
                "version", "1.0.0"
        ));
    }

    // AJOUTE CE MAPPING À LA RACINE !
    @RestController
    public class RootHealthController {
        @GetMapping("/health")
        public ResponseEntity<Map<String, Object>> rootHealth() {
            return ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "service", "ai-service",
                    "timestamp", LocalDateTime.now(),
                    "version", "1.0.0"
            ));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "service", "Cabinet360 AI Service",
                "description", "Provides AI-powered medical assistance through chatbot and PubMed integration",
                "features", Map.of(
                        "chatbot", "Medical AI assistant for healthcare professionals",
                        "pubmed", "Search and analyze medical literature",
                        "nlp", "Natural language processing for medical texts"
                ),
                "timestamp", LocalDateTime.now()
        ));
    }
}