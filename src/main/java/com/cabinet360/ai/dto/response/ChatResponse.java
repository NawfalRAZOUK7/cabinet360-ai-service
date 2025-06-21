package com.cabinet360.ai.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponse {

    private String response;
    private Long conversationId;
    private String conversationTitle;
    private LocalDateTime timestamp;
    private Integer tokenCount;
    private List<String> suggestedQuestions;

    public ChatResponse() {}

    public ChatResponse(String response, Long conversationId) {
        this.response = response;
        this.conversationId = conversationId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public String getConversationTitle() { return conversationTitle; }
    public void setConversationTitle(String conversationTitle) { this.conversationTitle = conversationTitle; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Integer getTokenCount() { return tokenCount; }
    public void setTokenCount(Integer tokenCount) { this.tokenCount = tokenCount; }

    public List<String> getSuggestedQuestions() { return suggestedQuestions; }
    public void setSuggestedQuestions(List<String> suggestedQuestions) { this.suggestedQuestions = suggestedQuestions; }
}
