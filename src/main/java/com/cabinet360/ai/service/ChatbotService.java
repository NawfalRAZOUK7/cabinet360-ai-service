package com.cabinet360.ai.service;

import com.cabinet360.ai.dto.response.ChatResponse;
import com.cabinet360.ai.dto.response.ConversationSummaryDto;
import com.cabinet360.ai.entity.ChatConversation;
import com.cabinet360.ai.entity.ChatMessage;
import com.cabinet360.ai.enums.MessageRole;
import com.cabinet360.ai.repository.ChatConversationRepository;
import com.cabinet360.ai.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 🆓 Updated Chatbot Service using FREE AI instead of OpenAI
 */
@Service
@Transactional
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final FreeAiService freeAiService; // 🆓 Using free AI instead of OpenAI

    @Value("${dev.mode:false}")
    private boolean devMode;

    public ChatbotService(
            ChatConversationRepository conversationRepository,
            ChatMessageRepository messageRepository,
            FreeAiService freeAiService // 🆓 Injecting free AI service
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.freeAiService = freeAiService;
    }

    /**
     * 💬 Process user message and generate AI response
     */
    public ChatResponse processMessage(String userMessage, Long conversationId, Long userId, String userEmail, String medicalContext) {
        try {
            logger.info("Processing message for user: {}", userEmail);

            // Get or create conversation
            ChatConversation conversation = getOrCreateConversation(conversationId, userId, userEmail, userMessage);

            // Get conversation history for context
            List<ChatMessage> history = messageRepository.findByConversationIdOrderByTimestampAsc(conversation.getId());

            // Save user message
            ChatMessage userChatMessage = new ChatMessage(conversation, MessageRole.USER, userMessage);
            userChatMessage.setMedicalContext(medicalContext);
            userChatMessage.setTokenCount(freeAiService.estimateTokenCount(userMessage));
            messageRepository.save(userChatMessage);

            // Build conversation context for AI
            String conversationContext = buildConversationContext(history, medicalContext);

            // 🆓 Get AI response using free service
            String aiResponse = freeAiService.generateMedicalResponse(userMessage, conversationContext);

            // Save AI response
            ChatMessage aiChatMessage = new ChatMessage(conversation, MessageRole.ASSISTANT, aiResponse);
            aiChatMessage.setTokenCount(freeAiService.estimateTokenCount(aiResponse));
            messageRepository.save(aiChatMessage);

            // Update conversation
            conversation.setUpdatedAt(LocalDateTime.now());
            if (conversation.getTitle().equals("New Conversation")) {
                conversation.setTitle(generateConversationTitle(userMessage));
            }
            conversationRepository.save(conversation);

            // 🆓 Generate suggested questions using free AI
            List<String> suggestedQuestions = freeAiService.generateSuggestedQuestions(
                    buildConversationContext(history, userMessage, aiResponse)
            );

            // Build response
            ChatResponse response = new ChatResponse(aiResponse, conversation.getId());
            response.setConversationTitle(conversation.getTitle());
            response.setTokenCount(aiChatMessage.getTokenCount());
            response.setSuggestedQuestions(suggestedQuestions);

            logDev("💬", "Chat Response", "Generated FREE AI response for conversation " + conversation.getId());

            return response;

        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);

            // Return fallback response instead of throwing exception
            return createFallbackResponse(conversationId, userMessage);
        }
    }

    /**
     * 🔄 Create fallback response when AI fails
     */
    private ChatResponse createFallbackResponse(Long conversationId, String userMessage) {
        String fallbackMessage = "I'm experiencing technical difficulties right now. " +
                "Please try again in a moment, or consult with a healthcare professional " +
                "for immediate medical assistance.";

        ChatResponse response = new ChatResponse(fallbackMessage, conversationId);
        response.setSuggestedQuestions(List.of(
                "Can you help me understand these symptoms?",
                "What should I do if this is urgent?",
                "When should I contact a doctor?"
        ));

        return response;
    }

    /**
     * 🧠 Build conversation context for AI
     */
    private String buildConversationContext(List<ChatMessage> history, String medicalContext) {
        StringBuilder context = new StringBuilder();

        // Add medical context if available
        if (medicalContext != null && !medicalContext.trim().isEmpty()) {
            context.append("Medical Context: ").append(medicalContext).append("\n\n");
        }

        // Add recent conversation history (last 6 messages for context)
        int startIndex = Math.max(0, history.size() - 6);
        for (int i = startIndex; i < history.size(); i++) {
            ChatMessage msg = history.get(i);
            context.append(msg.getRole().name()).append(": ").append(msg.getContent()).append("\n");
        }

        return context.toString();
    }

    /**
     * 🧠 Build conversation context with new messages
     */
    private String buildConversationContext(List<ChatMessage> history, String userMessage, String aiResponse) {
        StringBuilder context = new StringBuilder();

        // Add last few messages for context
        int startIndex = Math.max(0, history.size() - 4);
        for (int i = startIndex; i < history.size(); i++) {
            ChatMessage msg = history.get(i);
            context.append(msg.getRole().name()).append(": ").append(msg.getContent()).append("\n");
        }

        context.append("USER: ").append(userMessage).append("\n");
        context.append("ASSISTANT: ").append(aiResponse);

        return context.toString();
    }

    private ChatConversation getOrCreateConversation(Long conversationId, Long userId, String userEmail, String userMessage) {
        if (conversationId != null) {
            return conversationRepository.findByIdAndUserId(conversationId, userId)
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));
        }

        // Create new conversation
        String title = generateConversationTitle(userMessage);
        return conversationRepository.save(new ChatConversation(userId, userEmail, title));
    }

    private String generateConversationTitle(String firstMessage) {
        if (firstMessage.length() > 50) {
            return firstMessage.substring(0, 47) + "...";
        }
        return firstMessage.isEmpty() ? "New Conversation" : firstMessage;
    }

    public List<ConversationSummaryDto> getUserConversations(Long userId) {
        List<ChatConversation> conversations = conversationRepository.findActiveConversationsByUser(userId);

        return conversations.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    public List<ChatMessage> getConversationMessages(Long conversationId, Long userId) {
        // Verify user owns this conversation
        conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }

    public void deleteConversation(Long conversationId, Long userId) {
        ChatConversation conversation = conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        conversation.setActive(false);
        conversationRepository.save(conversation);

        logDev("🗑️", "Conversation Deleted", "Conversation " + conversationId + " marked as inactive");
    }

    public ChatConversation updateConversationTitle(Long conversationId, Long userId, String newTitle) {
        ChatConversation conversation = conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        conversation.setTitle(newTitle);
        conversation.setUpdatedAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    private ConversationSummaryDto convertToSummaryDto(ChatConversation conversation) {
        ConversationSummaryDto dto = new ConversationSummaryDto(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );

        // Get message count
        long messageCount = messageRepository.countByConversationId(conversation.getId());
        dto.setMessageCount((int) messageCount);

        // Get last message
        ChatMessage lastMessage = messageRepository.findLastMessageInConversation(conversation.getId());
        if (lastMessage != null) {
            String content = lastMessage.getContent();
            if (content.length() > 100) {
                content = content.substring(0, 97) + "...";
            }
            dto.setLastMessage(content);
        }

        return dto;
    }

    public Long getTotalTokenUsage(Long userId) {
        Long total = messageRepository.getTotalTokensByUser(userId);
        return total != null ? total : 0L;
    }

    public void clearAllConversations(Long userId) {
        List<ChatConversation> conversations = conversationRepository.findActiveConversationsByUser(userId);

        for (ChatConversation conversation : conversations) {
            conversation.setActive(false);
        }

        conversationRepository.saveAll(conversations);

        logDev("🧹", "Conversations Cleared", "All conversations cleared for user " + userId);
    }

    /**
     * 🔍 Check AI service health
     */
    public boolean isAiServiceHealthy() {
        return freeAiService.isServiceAvailable();
    }

    private void logDev(String emoji, String title, String message) {
        if (devMode) {
            System.out.println("🛠️ AI Service - " + emoji + " " + title + ": " + message);
        }
    }
}