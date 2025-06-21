package com.cabinet360.ai.controller;

import com.cabinet360.ai.dto.request.ChatRequest;
import com.cabinet360.ai.dto.response.ChatResponse;
import com.cabinet360.ai.dto.response.ConversationSummaryDto;
import com.cabinet360.ai.entity.ChatMessage;
import com.cabinet360.ai.service.ChatbotService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai/chatbot")
@PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        String userEmail = (String) httpRequest.getAttribute("userEmail");

        ChatResponse response = chatbotService.processMessage(
                request.getMessage(),
                request.getConversationId(),
                userId,
                userEmail,
                request.getMedicalContext()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationSummaryDto>> getConversations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ConversationSummaryDto> conversations = chatbotService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<ChatMessage>> getConversationMessages(
            @PathVariable Long conversationId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        List<ChatMessage> messages = chatbotService.getConversationMessages(conversationId, userId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<Map<String, String>> deleteConversation(
            @PathVariable Long conversationId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        chatbotService.deleteConversation(conversationId, userId);
        return ResponseEntity.ok(Map.of("message", "Conversation deleted successfully"));
    }

    @PutMapping("/conversations/{conversationId}/title")
    public ResponseEntity<Map<String, String>> updateConversationTitle(
            @PathVariable Long conversationId,
            @RequestBody Map<String, String> payload,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        String newTitle = payload.get("title");

        if (newTitle == null || newTitle.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Title cannot be empty"));
        }

        chatbotService.updateConversationTitle(conversationId, userId, newTitle.trim());
        return ResponseEntity.ok(Map.of("message", "Title updated successfully"));
    }

    @DeleteMapping("/conversations/clear")
    public ResponseEntity<Map<String, String>> clearAllConversations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        chatbotService.clearAllConversations(userId);
        return ResponseEntity.ok(Map.of("message", "All conversations cleared"));
    }

    @GetMapping("/usage/tokens")
    public ResponseEntity<Map<String, Object>> getTokenUsage(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long totalTokens = chatbotService.getTotalTokenUsage(userId);
        return ResponseEntity.ok(Map.of(
                "totalTokens", totalTokens,
                "estimatedCost", totalTokens * 0.002 // Rough estimate
        ));
    }
}


