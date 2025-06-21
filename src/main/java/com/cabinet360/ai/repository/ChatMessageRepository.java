package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationIdOrderByTimestampAsc(Long conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesByConversationId(Long conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp DESC LIMIT 1")
    ChatMessage findLastMessageInConversation(Long conversationId);

    long countByConversationId(Long conversationId);

    @Query("SELECT SUM(m.tokenCount) FROM ChatMessage m WHERE m.conversation.userId = :userId")
    Long getTotalTokensByUser(Long userId);
}