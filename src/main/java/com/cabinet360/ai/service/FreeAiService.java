package com.cabinet360.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 🆓 Enhanced FREE AI Service with Google Gemini Support
 * Now supports both Hugging Face and Google Gemini!
 */
@Service
public class FreeAiService {

    private static final Logger logger = LoggerFactory.getLogger(FreeAiService.class);

    private final WebClient huggingFaceClient;
    private final WebClient geminiClient;
    private final ObjectMapper objectMapper;

    // Configuration from application.yml
    @Value("${ai.provider:huggingface}")
    private String aiProvider;

    @Value("${ai.huggingface.model:microsoft/DialoGPT-medium}")
    private String huggingFaceModel;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-pro}")
    private String geminiModel;

    @Value("${ai.fallback.enabled:true}")
    private boolean fallbackEnabled;

    @Value("${ai.medical.system.prompt}")
    private String medicalSystemPrompt;

    public FreeAiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        // Hugging Face client
        this.huggingFaceClient = webClientBuilder
                .baseUrl("https://api-inference.huggingface.co")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();

        // Google Gemini client
        this.geminiClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2048 * 1024))
                .build();

        this.objectMapper = objectMapper;
    }

    /**
     * 🤖 Generate medical response using configured AI provider
     */
    public String generateMedicalResponse(String userMessage, String medicalContext) {
        String enhancedPrompt = buildEnhancedMedicalPrompt(userMessage, medicalContext);

        try {
            // Route to the configured AI provider
            switch (aiProvider.toLowerCase()) {
                case "gemini":
                    return callGeminiAPI(enhancedPrompt);
                case "huggingface":
                    return callHuggingFaceModel(huggingFaceModel, enhancedPrompt);
                default:
                    logger.warn("Unknown AI provider: {}, falling back to Gemini", aiProvider);
                    return callGeminiAPI(enhancedPrompt);
            }
        } catch (Exception e) {
            logger.warn("Primary AI service ({}) failed: {}", aiProvider, e.getMessage());
            return getFallbackResponse(userMessage, e);
        }
    }

    /**
     * 🧠 Enhanced medical prompt building
     */
    private String buildEnhancedMedicalPrompt(String userMessage, String medicalContext) {
        StringBuilder prompt = new StringBuilder();

        // Add medical system prompt
        prompt.append(medicalSystemPrompt).append("\n\n");

        // Add medical context if available
        if (medicalContext != null && !medicalContext.trim().isEmpty()) {
            prompt.append("MEDICAL CONTEXT: ").append(medicalContext).append("\n\n");
        }

        // Add specific medical instructions
        prompt.append("INSTRUCTIONS:\n");
        prompt.append("- Provide evidence-based medical information\n");
        prompt.append("- Include relevant clinical considerations\n");
        prompt.append("- Suggest when to consult healthcare professionals\n");
        prompt.append("- Be precise and professional\n");
        prompt.append("- Include safety disclaimers when appropriate\n\n");

        // Add the user's question
        prompt.append("MEDICAL QUESTION: ").append(userMessage).append("\n\n");
        prompt.append("RESPONSE:");

        return prompt.toString();
    }

    /**
     * 🔮 Call Google Gemini API (FREE!)
     */
    private String callGeminiAPI(String prompt) {
        Map<String, Object> request = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "temperature", 0.3,        // Lower for medical accuracy
                        "maxOutputTokens", 1000,   // Reasonable response length
                        "topP", 0.8,              // Focus on likely responses
                        "topK", 40                // Limit vocabulary diversity
                ),
                "safetySettings", List.of(
                        Map.of("category", "HARM_CATEGORY_MEDICAL", "threshold", "BLOCK_NONE"),
                        Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                        Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                        Map.of("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                        Map.of("category", "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE")
                )
        );

        try {
            logger.info("Calling Google Gemini API with model: {}", geminiModel);

            String response = geminiClient.post()
                    .uri("/models/" + geminiModel + ":generateContent?key=" + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            return extractGeminiResponse(response);

        } catch (WebClientResponseException e) {
            logger.error("Google Gemini API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Gemini AI service temporarily unavailable: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling Gemini: {}", e.getMessage());
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    /**
     * 📄 Generate article summary using configured AI
     */
    @Cacheable("article-summaries")
    public String generateArticleSummary(String title, String abstractText) {
        String prompt = String.format("""
            You are a medical expert. Summarize this research article for healthcare professionals.
            
            TITLE: %s
            
            ABSTRACT: %s
            
            Please provide:
            1. Key findings (2-3 sentences)
            2. Clinical relevance (1-2 sentences)
            3. Limitations or considerations (1 sentence)
            
            Keep the summary professional and focused on clinical applications.
            """, title, abstractText);

        try {
            switch (aiProvider.toLowerCase()) {
                case "gemini":
                    return callGeminiAPI(prompt);
                case "huggingface":
                    return callHuggingFaceModel(huggingFaceModel, prompt);
                default:
                    return callGeminiAPI(prompt);
            }
        } catch (Exception e) {
            logger.warn("Article summary failed with {}: {}", aiProvider, e.getMessage());
            return generateFallbackSummary(title, abstractText);
        }
    }

    /**
     * ❓ Generate suggested follow-up questions
     */
    public List<String> generateSuggestedQuestions(String conversationContext) {
        String prompt = String.format("""
            Based on this medical conversation, suggest 3 relevant follow-up questions that a healthcare professional might ask:
            
            CONVERSATION: %s
            
            Provide 3 specific, clinically relevant questions. Format as a simple list:
            1. [Question 1]
            2. [Question 2]
            3. [Question 3]
            """, conversationContext);

        try {
            String response;
            switch (aiProvider.toLowerCase()) {
                case "gemini":
                    response = callGeminiAPI(prompt);
                    break;
                case "huggingface":
                    response = callHuggingFaceModel(huggingFaceModel, prompt);
                    break;
                default:
                    response = callGeminiAPI(prompt);
            }

            return parseQuestions(response);
        } catch (Exception e) {
            logger.warn("Question generation failed: {}", e.getMessage());
            return getDefaultMedicalQuestions();
        }
    }

    /**
     * 📊 Extract response from Google Gemini
     */
    private String extractGeminiResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Check if there are candidates
            JsonNode candidates = root.get("candidates");
            if (candidates == null || candidates.size() == 0) {
                logger.warn("No candidates in Gemini response");
                return "I apologize, but I couldn't generate a proper medical response. Please try rephrasing your question.";
            }

            // Extract the text from the first candidate
            JsonNode content = candidates.get(0).get("content");
            if (content == null) {
                logger.warn("No content in Gemini candidate");
                return "I'm having trouble generating a response. Please try again.";
            }

            JsonNode parts = content.get("parts");
            if (parts == null || parts.size() == 0) {
                logger.warn("No parts in Gemini content");
                return "Unable to generate a complete response. Please rephrase your question.";
            }

            String generatedText = parts.get(0).get("text").asText();

            // Clean up and validate the response
            return cleanupMedicalResponse(generatedText);

        } catch (Exception e) {
            logger.error("Error parsing Gemini response: {}", e.getMessage());
            return "I'm experiencing technical difficulties. Please try again or consult your healthcare provider.";
        }
    }

    /**
     * 🌐 Call Hugging Face Model (fallback)
     */
    private String callHuggingFaceModel(String modelName, String prompt) {
        Map<String, Object> request = Map.of(
                "inputs", prompt,
                "parameters", Map.of(
                        "max_length", 200,
                        "temperature", 0.7,
                        "do_sample", true,
                        "top_p", 0.9,
                        "repetition_penalty", 1.1
                ),
                "options", Map.of(
                        "wait_for_model", true,
                        "use_cache", true
                )
        );

        try {
            String response = huggingFaceClient.post()
                    .uri("/models/" + modelName)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            return extractHuggingFaceResponse(response, prompt);

        } catch (WebClientResponseException e) {
            logger.error("Hugging Face API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("AI service temporarily unavailable", e);
        }
    }

    /**
     * 🧹 Clean up medical response
     */
    private String cleanupMedicalResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return "I'd be happy to help with your medical question. Could you please provide more details?";
        }

        // Remove any incomplete sentences and ensure medical disclaimer
        String cleaned = response.trim();

        // Add medical disclaimer if not present
        if (!cleaned.toLowerCase().contains("consult") && !cleaned.toLowerCase().contains("healthcare professional")) {
            cleaned += "\n\n⚠️ This information is for educational purposes. Please consult with a healthcare professional for personalized medical advice.";
        }

        return cleaned;
    }

    /**
     * 🚨 Enhanced fallback response with provider info
     */
    private String getFallbackResponse(String userMessage, Exception error) {
        if (!fallbackEnabled) {
            return "AI service is currently unavailable. Please try again later.";
        }

        // Try alternative provider if available
        try {
            if ("gemini".equals(aiProvider)) {
                logger.info("Gemini failed, trying Hugging Face fallback");
                return callHuggingFaceModel(huggingFaceModel, buildEnhancedMedicalPrompt(userMessage, null));
            } else {
                logger.info("Hugging Face failed, trying Gemini fallback");
                return callGeminiAPI(buildEnhancedMedicalPrompt(userMessage, null));
            }
        } catch (Exception fallbackError) {
            logger.error("Both AI providers failed. Original: {}, Fallback: {}",
                    error.getMessage(), fallbackError.getMessage());
        }

        return String.format("""
            I'm currently experiencing technical difficulties with the AI service.
            
            For your medical question: "%s"
            
            I recommend:
            🏥 Consulting with your healthcare provider
            📞 Contacting telehealth services
            🚨 Seeking immediate medical attention if urgent
            
            Please try again in a few minutes, or contact medical professionals directly.
            """, userMessage);
    }

    // ================================
    // Helper Methods
    // ================================

    private String extractHuggingFaceResponse(String jsonResponse, String originalPrompt) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            if (root.isArray() && root.size() > 0) {
                String generatedText = root.get(0).get("generated_text").asText();
                if (generatedText.startsWith(originalPrompt)) {
                    generatedText = generatedText.substring(originalPrompt.length()).trim();
                }
                return cleanupMedicalResponse(generatedText);
            }
        } catch (Exception e) {
            logger.error("Error parsing Hugging Face response: {}", e.getMessage());
        }
        return "I apologize, but I couldn't generate a proper response. Please try rephrasing your question.";
    }

    private List<String> parseQuestions(String response) {
        List<String> questions = new ArrayList<>();
        try {
            String[] lines = response.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (line.matches("^\\d+\\..*") || line.startsWith("-") || line.startsWith("•")) {
                    // Remove numbering and formatting
                    String question = line.replaceFirst("^\\d+\\.", "").replaceFirst("^[-•]", "").trim();
                    if (!question.isEmpty() && question.endsWith("?")) {
                        questions.add(question);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse questions from response: {}", e.getMessage());
        }

        return questions.isEmpty() ? getDefaultMedicalQuestions() : questions;
    }

    private List<String> getDefaultMedicalQuestions() {
        return List.of(
                "What are the potential risk factors for this condition?",
                "Are there any contraindications I should be aware of?",
                "What follow-up care or monitoring is recommended?"
        );
    }

    private String generateFallbackSummary(String title, String abstractText) {
        return String.format("""
            Article: %s
            
            Summary: Unable to generate AI summary at this time. Please review the original abstract for key findings.
            
            Abstract: %s
            
            ⚠️ Please consult medical literature databases or specialists for detailed analysis.
            """, title, abstractText.length() > 200 ? abstractText.substring(0, 200) + "..." : abstractText);
    }

    /**
     * 🔢 Estimate token count for cost tracking
     */
    public int estimateTokenCount(String text) {
        if (text == null) return 0;
        // Rough estimation: ~4 characters per token
        return Math.max(1, text.length() / 4);
    }

    /**
     * 🏥 Check if AI service is available
     */
    public boolean isServiceAvailable() {
        try {
            // Test based on current AI provider
            switch (aiProvider.toLowerCase()) {
                case "gemini":
                    return isGeminiAvailable();
                case "huggingface":
                    return isHuggingFaceAvailable();
                default:
                    return isGeminiAvailable(); // Default to Gemini
            }
        } catch (Exception e) {
            logger.warn("Error checking AI service availability: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 🔮 Check if Gemini is available
     */
    private boolean isGeminiAvailable() {
        try {
            // Simple test request to Gemini
            Map<String, Object> testRequest = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", "Hello")
                            ))
                    ),
                    "generationConfig", Map.of(
                            "maxOutputTokens", 10
                    )
            );

            String response = geminiClient.post()
                    .uri("/models/" + geminiModel + ":generateContent?key=" + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(testRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            return response != null && response.contains("candidates");
        } catch (Exception e) {
            logger.debug("Gemini availability check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 🤗 Check if Hugging Face is available
     */
    private boolean isHuggingFaceAvailable() {
        try {
            huggingFaceClient.get()
                    .uri("/models/" + huggingFaceModel)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return true;
        } catch (Exception e) {
            logger.debug("Hugging Face availability check failed: {}", e.getMessage());
            return false;
        }
    }
}