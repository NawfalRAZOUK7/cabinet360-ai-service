package com.cabinet360.ai.service;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAiChatService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiChatService.class);

    private final OpenAiService openAiService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.max.tokens}")
    private Integer maxTokens;

    public OpenAiChatService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    public ChatCompletionResult createMedicalChatCompletion(List<ChatMessage> conversationHistory, String userMessage, String medicalContext) {
        List<ChatMessage> messages = new ArrayList<>();

        // System message with medical context
        String systemPrompt = buildMedicalSystemPrompt(medicalContext);
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));

        // Add conversation history
        messages.addAll(conversationHistory);

        // Add current user message
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .maxTokens(maxTokens)
                .temperature(0.7)
                .topP(1.0)
                .frequencyPenalty(0.0)
                .presencePenalty(0.0)
                .build();

        try {
            return openAiService.createChatCompletion(request);
        } catch (Exception e) {
            logger.error("Error calling OpenAI API: {}", e.getMessage());
            throw new RuntimeException("Failed to generate AI response", e);
        }
    }

    public String generateArticleSummary(String title, String abstractText) {
        String prompt = String.format(
                "Summarize the following medical research article in 2-3 sentences, focusing on key findings and clinical relevance:\n\nTitle: %s\n\nAbstract: %s",
                title, abstractText
        );

        List<ChatMessage> messages = List.of(
                new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a medical expert that summarizes research articles for healthcare professionals."),
                new ChatMessage(ChatMessageRole.USER.value(), prompt)
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .maxTokens(200)
                .temperature(0.5)
                .build();

        try {
            ChatCompletionResult result = openAiService.createChatCompletion(request);
            return result.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            logger.error("Error generating article summary: {}", e.getMessage());
            return null;
        }
    }

    public List<String> generateSuggestedQuestions(String conversationContext) {
        String prompt = String.format(
                "Based on this medical conversation context, suggest 3 relevant follow-up questions a doctor might ask:\n\n%s",
                conversationContext
        );

        List<ChatMessage> messages = List.of(
                new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "Generate concise, clinically relevant follow-up questions. Return only the questions, one per line."),
                new ChatMessage(ChatMessageRole.USER.value(), prompt)
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .maxTokens(150)
                .temperature(0.8)
                .build();

        try {
            ChatCompletionResult result = openAiService.createChatCompletion(request);
            String response = result.getChoices().get(0).getMessage().getContent();
            return List.of(response.split("\n"));
        } catch (Exception e) {
            logger.error("Error generating suggested questions: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private String buildMedicalSystemPrompt(String medicalContext) {
        String basePrompt = """
            You are a medical AI assistant for healthcare professionals. Your role is to:
            
            1. Provide evidence-based medical information
            2. Suggest relevant diagnostic considerations
            3. Discuss treatment options based on current guidelines
            4. Help with clinical decision-making
            5. Reference medical literature when appropriate
            
            IMPORTANT DISCLAIMERS:
            - You are an assistant tool, not a replacement for clinical judgment
            - Always recommend consulting with colleagues or specialists when appropriate
            - Emphasize the importance of patient safety and standard medical practices
            - Do not provide specific dosing without recommending verification
            
            Be professional, accurate, and helpful while maintaining appropriate medical caution.
            """;

        if (medicalContext != null && !medicalContext.trim().isEmpty()) {
            basePrompt += "\n\nCurrent medical context: " + medicalContext;
        }

        return basePrompt;
    }

    public int estimateTokenCount(String text) {
        // Rough estimation: ~4 characters per token
        return text.length() / 4;
    }
}