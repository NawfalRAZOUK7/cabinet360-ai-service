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

/**
 * 🆓 FREE AI Service using Hugging Face Inference API
 * No API key required for basic usage!
 */
@Service
public class FreeAiService {

    private static final Logger logger = LoggerFactory.getLogger(FreeAiService.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // 🤗 Free Hugging Face models
    @Value("${ai.huggingface.model:microsoft/DialoGPT-medium}")
    private String conversationModel;

    @Value("${ai.huggingface.medical.model:dmis-lab/biobert-base-cased-v1.1}")
    private String medicalModel;

    @Value("${ai.fallback.enabled:true}")
    private boolean fallbackEnabled;

    public FreeAiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl("https://api-inference.huggingface.co")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
        this.objectMapper = objectMapper;
    }

    /**
     * 🤖 Generate medical chat response
     */
    public String generateMedicalResponse(String userMessage, String medicalContext) {
        String prompt = buildMedicalPrompt(userMessage, medicalContext);

        try {
            return callHuggingFaceModel(conversationModel, prompt);
        } catch (Exception e) {
            logger.warn("Primary AI service failed: {}", e.getMessage());
            return getFallbackResponse(userMessage);
        }
    }

    /**
     * 📄 Generate article summary
     */
    @Cacheable("article-summaries")
    public String generateArticleSummary(String title, String abstractText) {
        String prompt = String.format(
                "Summarize this medical article in 2-3 sentences for healthcare professionals:\n\nTitle: %s\n\nAbstract: %s\n\nSummary:",
                title, abstractText
        );

        try {
            return callHuggingFaceModel(conversationModel, prompt);
        } catch (Exception e) {
            logger.warn("Article summary failed: {}", e.getMessage());
            return generateFallbackSummary(title, abstractText);
        }
    }

    /**
     * ❓ Generate suggested follow-up questions
     */
    public List<String> generateSuggestedQuestions(String conversationContext) {
        String prompt = String.format(
                "Based on this medical conversation, suggest 3 relevant follow-up questions:\n\n%s\n\nQuestions:",
                conversationContext
        );

        try {
            String response = callHuggingFaceModel(conversationModel, prompt);
            return parseQuestions(response);
        } catch (Exception e) {
            logger.warn("Question generation failed: {}", e.getMessage());
            return getDefaultQuestions();
        }
    }

    /**
     * 🌐 Call Hugging Face Inference API
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
            String response = webClient.post()
                    .uri("/models/" + modelName)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            return extractGeneratedText(response, prompt);

        } catch (WebClientResponseException e) {
            logger.error("Hugging Face API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("AI service temporarily unavailable", e);
        }
    }

    /**
     * 📝 Extract generated text from Hugging Face response
     */
    private String extractGeneratedText(String jsonResponse, String originalPrompt) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);

            if (root.isArray() && root.size() > 0) {
                String generatedText = root.get(0).get("generated_text").asText();

                // Remove the original prompt from response
                if (generatedText.startsWith(originalPrompt)) {
                    generatedText = generatedText.substring(originalPrompt.length()).trim();
                }

                return cleanupResponse(generatedText);
            }

            return "I apologize, but I couldn't generate a proper response. Please try rephrasing your question.";

        } catch (Exception e) {
            logger.error("Error parsing AI response: {}", e.getMessage());
            return "I'm experiencing technical difficulties. Please try again.";
        }
    }

    /**
     * 🧹 Clean up AI response
     */
    private String cleanupResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return "I'd be happy to help with your medical question. Could you please provide more details?";
        }

        // Remove incomplete sentences at the end
        String[] sentences = response.split("\\.");
        if (sentences.length > 1) {
            StringBuilder cleaned = new StringBuilder();
            for (int i = 0; i < sentences.length - 1; i++) {
                cleaned.append(sentences[i].trim()).append(". ");
            }

            // Add last sentence if it seems complete
            String lastSentence = sentences[sentences.length - 1].trim();
            if (lastSentence.length() > 10 && !lastSentence.endsWith("...")) {
                cleaned.append(lastSentence).append(".");
            }

            return cleaned.toString().trim();
        }

        return response.trim();
    }

    /**
     * 🏥 Build medical-specific prompt
     */
    private String buildMedicalPrompt(String userMessage, String medicalContext) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a helpful medical AI assistant. ");
        prompt.append("Provide evidence-based medical information. ");
        prompt.append("Always recommend consulting healthcare professionals for medical decisions.\n\n");

        if (medicalContext != null && !medicalContext.trim().isEmpty()) {
            prompt.append("Medical Context: ").append(medicalContext).append("\n\n");
        }

        prompt.append("Question: ").append(userMessage).append("\n\n");
        prompt.append("Response: ");

        return prompt.toString();
    }

    /**
     * 🔄 Fallback response when AI fails
     */
    private String getFallbackResponse(String userMessage) {
        if (!fallbackEnabled) {
            return "AI service is currently unavailable. Please try again later.";
        }

        // Simple keyword-based responses
        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("diabetes")) {
            return "Diabetes is a chronic condition affecting blood sugar levels. Common symptoms include increased thirst, frequent urination, and fatigue. Please consult with a healthcare provider for proper diagnosis and treatment.";
        }

        if (lowerMessage.contains("hypertension") || lowerMessage.contains("blood pressure")) {
            return "Hypertension (high blood pressure) is often called the 'silent killer' as it may have no symptoms. Regular monitoring and lifestyle modifications are important. Please consult with a healthcare provider for evaluation.";
        }

        if (lowerMessage.contains("medication") || lowerMessage.contains("drug")) {
            return "For medication-related questions, please consult with a pharmacist or healthcare provider. They can provide information about drug interactions, dosing, and side effects specific to your situation.";
        }

        return "I understand you have a medical question. While I'm experiencing technical difficulties, I recommend consulting with a healthcare professional who can provide personalized medical advice based on your specific situation.";
    }

    /**
     * 📰 Generate fallback summary for articles
     */
    private String generateFallbackSummary(String title, String abstractText) {
        if (abstractText != null && abstractText.length() > 200) {
            // Simple extractive summary - first two sentences
            String[] sentences = abstractText.split("\\.");
            if (sentences.length >= 2) {
                return sentences[0].trim() + ". " + sentences[1].trim() + ".";
            }
        }

        return "This article discusses " + title.toLowerCase() + ". For detailed information, please review the full abstract and consult the complete publication.";
    }

    /**
     * ❓ Parse questions from AI response
     */
    private List<String> parseQuestions(String response) {
        String[] lines = response.split("\n");
        return List.of(lines)
                .stream()
                .map(String::trim)
                .filter(line -> line.contains("?"))
                .limit(3)
                .toList();
    }

    /**
     * ❓ Default follow-up questions
     */
    private List<String> getDefaultQuestions() {
        return List.of(
                "What additional symptoms should I look for?",
                "When should the patient seek immediate medical attention?",
                "What lifestyle modifications might be helpful?"
        );
    }

    /**
     * 🔢 Estimate token count (rough approximation)
     */
    public int estimateTokenCount(String text) {
        if (text == null) return 0;
        return text.split("\\s+").length; // Simple word count approximation
    }

    /**
     * 🏥 Check if AI service is available
     */
    public boolean isServiceAvailable() {
        try {
            webClient.get()
                    .uri("/models/" + conversationModel)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}