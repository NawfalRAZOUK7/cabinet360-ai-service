package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 💬 Chat Message Repository
 * Repository interface for managing chat messages and conversation history
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // ================================
    // Basic Finder Methods
    // ================================

    /**
     * Find messages by conversation ID in chronological order
     * @param conversationId Conversation ID
     * @return List of messages ordered by timestamp
     */
    List<ChatMessage> findByConversationIdOrderByTimestampAsc(Long conversationId);

    /**
     * Find messages by conversation ID using JOIN query
     * @param conversationId Conversation ID
     * @return List of messages ordered by timestamp
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesByConversationId(@Param("conversationId") Long conversationId);

    /**
     * Find the last message in a conversation
     * @param conversationId Conversation ID
     * @return Last message in the conversation
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp DESC LIMIT 1")
    ChatMessage findLastMessageInConversation(@Param("conversationId") Long conversationId);

    /**
     * Find messages by conversation with pagination
     * @param conversationId Conversation ID
     * @param pageable Pagination parameters
     * @return Page of messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp ASC")
    Page<ChatMessage> findByConversationIdWithPagination(@Param("conversationId") Long conversationId, Pageable pageable);

    // ================================
    // Count Methods
    // ================================

    /**
     * Count messages in a conversation
     * @param conversationId Conversation ID
     * @return Number of messages
     */
    long countByConversationId(Long conversationId);

    /**
     * Count medical context messages after specified date
     * @param date Date threshold
     * @return Count of messages with medical context after the date
     */
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.medicalContext IS NOT NULL AND m.timestamp > :date")
    long countMedicalContextMessagesAfter(@Param("date") LocalDateTime date);

    /**
     * Count user messages in a conversation
     * @param conversationId Conversation ID
     * @return Number of user messages
     */
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversation.id = :conversationId AND m.role = 'USER'")
    long countUserMessagesByConversation(@Param("conversationId") Long conversationId);

    /**
     * Count assistant messages in a conversation
     * @param conversationId Conversation ID
     * @return Number of assistant messages
     */
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversation.id = :conversationId AND m.role = 'ASSISTANT'")
    long countAssistantMessagesByConversation(@Param("conversationId") Long conversationId);

    // ================================
    // Token Usage Tracking
    // ================================

    /**
     * Get total token usage by user
     * @param userId User ID
     * @return Total tokens used by user
     */
    @Query("SELECT SUM(m.tokenCount) FROM ChatMessage m WHERE m.conversation.userId = :userId")
    Long getTotalTokensByUser(@Param("userId") Long userId);

    /**
     * Get token usage by user within date range
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return Total tokens used in date range
     */
    @Query("SELECT SUM(m.tokenCount) FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.timestamp BETWEEN :startDate AND :endDate")
    Long getTokensByUserAndDateRange(@Param("userId") Long userId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Get token usage by conversation
     * @param conversationId Conversation ID
     * @return Total tokens used in conversation
     */
    @Query("SELECT SUM(m.tokenCount) FROM ChatMessage m WHERE m.conversation.id = :conversationId")
    Long getTokensByConversation(@Param("conversationId") Long conversationId);

    // ================================
    // Medical Context Queries
    // ================================

    /**
     * Find messages with medical context
     * @param conversationId Conversation ID
     * @return List of messages with medical context
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId " +
            "AND m.medicalContext IS NOT NULL ORDER BY m.timestamp ASC")
    List<ChatMessage> findMedicalContextMessagesByConversation(@Param("conversationId") Long conversationId);

    /**
     * Find messages with medical context by user
     * @param userId User ID
     * @return List of messages with medical context
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.medicalContext IS NOT NULL ORDER BY m.timestamp DESC")
    List<ChatMessage> findMedicalContextMessagesByUser(@Param("userId") Long userId);

    /**
     * Find recent medical context messages
     * @param userId User ID
     * @param daysAgo Number of days ago
     * @return List of recent medical context messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.medicalContext IS NOT NULL AND m.timestamp >= :daysAgo " +
            "ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentMedicalContextMessages(@Param("userId") Long userId,
                                                       @Param("daysAgo") LocalDateTime daysAgo);

    // ================================
    // Date Range Queries
    // ================================

    /**
     * Find messages within date range
     * @param conversationId Conversation ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of messages in date range
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId " +
            "AND m.timestamp BETWEEN :startDate AND :endDate ORDER BY m.timestamp ASC")
    List<ChatMessage> findByConversationIdAndDateRange(@Param("conversationId") Long conversationId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent messages by user
     * @param userId User ID
     * @param daysAgo Number of days ago
     * @return List of recent messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.timestamp >= :daysAgo ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentMessagesByUser(@Param("userId") Long userId,
                                               @Param("daysAgo") LocalDateTime daysAgo);

    /**
     * Find messages by user within date range
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of messages in date range
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.timestamp BETWEEN :startDate AND :endDate ORDER BY m.timestamp DESC")
    List<ChatMessage> findByUserIdAndDateRange(@Param("userId") Long userId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    // ================================
    // Role-based Queries
    // ================================

    /**
     * Find messages by role in conversation
     * @param conversationId Conversation ID
     * @param role Message role (USER, ASSISTANT)
     * @return List of messages with specified role
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId " +
            "AND m.role = :role ORDER BY m.timestamp ASC")
    List<ChatMessage> findByConversationIdAndRole(@Param("conversationId") Long conversationId,
                                                  @Param("role") String role);

    /**
     * Find user messages in conversation
     * @param conversationId Conversation ID
     * @return List of user messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId " +
            "AND m.role = 'USER' ORDER BY m.timestamp ASC")
    List<ChatMessage> findUserMessagesByConversation(@Param("conversationId") Long conversationId);

    /**
     * Find assistant messages in conversation
     * @param conversationId Conversation ID
     * @return List of assistant messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId " +
            "AND m.role = 'ASSISTANT' ORDER BY m.timestamp ASC")
    List<ChatMessage> findAssistantMessagesByConversation(@Param("conversationId") Long conversationId);

    // ================================
    // Search Queries
    // ================================

    /**
     * Search messages by content
     * @param conversationId Conversation ID
     * @param searchTerm Search term
     * @return List of matching messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId " +
            "AND LOWER(m.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> searchMessagesByContent(@Param("conversationId") Long conversationId,
                                              @Param("searchTerm") String searchTerm);

    /**
     * Search messages by user
     * @param userId User ID
     * @param searchTerm Search term
     * @return List of matching messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND LOWER(m.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY m.timestamp DESC")
    List<ChatMessage> searchMessagesByUser(@Param("userId") Long userId,
                                           @Param("searchTerm") String searchTerm);

    /**
     * Search messages with medical context
     * @param userId User ID
     * @param searchTerm Search term
     * @return List of matching medical messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.medicalContext IS NOT NULL " +
            "AND (LOWER(m.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.medicalContext) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY m.timestamp DESC")
    List<ChatMessage> searchMedicalMessages(@Param("userId") Long userId,
                                            @Param("searchTerm") String searchTerm);

    // ================================
    // Analytics Queries
    // ================================

    /**
     * Get message statistics by user
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of statistics
     */
    @Query("SELECT m.role, COUNT(m) as count, AVG(m.tokenCount) as avgTokens " +
            "FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "AND m.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY m.role")
    List<Object[]> getMessageStatsByUser(@Param("userId") Long userId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Get daily message counts
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of daily counts
     */
    @Query(value = "SELECT DATE(timestamp) as date, COUNT(*) as count " +
            "FROM chat_messages m " +
            "JOIN chat_conversations c ON m.conversation_id = c.id " +
            "WHERE c.user_id = :userId " +
            "AND m.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(timestamp) " +
            "ORDER BY date", nativeQuery = true)
    List<Object[]> getDailyMessageCounts(@Param("userId") Long userId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Get conversation activity summary
     * @param userId User ID
     * @return List of conversation activity data
     */
    @Query("SELECT c.id, c.title, COUNT(m) as messageCount, MAX(m.timestamp) as lastActivity " +
            "FROM ChatMessage m JOIN m.conversation c " +
            "WHERE c.userId = :userId AND c.active = true " +
            "GROUP BY c.id, c.title " +
            "ORDER BY lastActivity DESC")
    List<Object[]> getConversationActivityByUser(@Param("userId") Long userId);

    // ================================
    // Cleanup Queries
    // ================================

    /**
     * Find old messages for cleanup
     * @param beforeDate Date threshold
     * @return List of old messages
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.timestamp < :beforeDate")
    List<ChatMessage> findOldMessages(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * Find messages without token count
     * @return List of messages missing token count
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.tokenCount IS NULL")
    List<ChatMessage> findMessagesWithoutTokenCount();

    /**
     * Get conversation message count summary
     * @param conversationIds List of conversation IDs
     * @return List of conversation message counts
     */
    @Query("SELECT m.conversation.id, COUNT(m) as messageCount " +
            "FROM ChatMessage m WHERE m.conversation.id IN :conversationIds " +
            "GROUP BY m.conversation.id")
    List<Object[]> getMessageCountsByConversations(@Param("conversationIds") List<Long> conversationIds);

    // ================================
    // Advanced Queries
    // ================================

    /**
     * Find conversations with high token usage
     * @param minTokenCount Minimum token threshold
     * @return List of high token usage conversations
     */
    @Query("SELECT m.conversation.id, SUM(m.tokenCount) as totalTokens " +
            "FROM ChatMessage m WHERE m.tokenCount IS NOT NULL " +
            "GROUP BY m.conversation.id " +
            "HAVING SUM(m.tokenCount) >= :minTokenCount " +
            "ORDER BY totalTokens DESC")
    List<Object[]> findHighTokenUsageConversations(@Param("minTokenCount") Long minTokenCount);

    /**
     * Find most active conversations
     * @param userId User ID
     * @param limit Number of results to return
     * @return List of most active conversations
     */
    @Query("SELECT m.conversation.id, COUNT(m) as messageCount " +
            "FROM ChatMessage m WHERE m.conversation.userId = :userId " +
            "GROUP BY m.conversation.id " +
            "ORDER BY messageCount DESC " +
            "LIMIT :limit")
    List<Object[]> findMostActiveConversations(@Param("userId") Long userId,
                                               @Param("limit") int limit);

    /**
     * Get average response time between user and assistant messages
     * @param conversationId Conversation ID
     * @return Average response time in seconds
     */
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (a.timestamp - u.timestamp))) " +
            "FROM chat_messages u " +
            "JOIN chat_messages a ON a.conversation_id = u.conversation_id " +
            "WHERE u.conversation_id = :conversationId " +
            "AND u.role = 'USER' AND a.role = 'ASSISTANT' " +
            "AND a.timestamp > u.timestamp " +
            "AND NOT EXISTS (" +
            "  SELECT 1 FROM chat_messages m2 " +
            "  WHERE m2.conversation_id = u.conversation_id " +
            "  AND m2.timestamp BETWEEN u.timestamp AND a.timestamp " +
            "  AND m2.id != u.id AND m2.id != a.id" +
            ")", nativeQuery = true)
    Double getAverageResponseTime(@Param("conversationId") Long conversationId);

    // ================================
    // Optional Finder Methods
    // ================================

    /**
     * Find message by ID and conversation (security check)
     * @param id Message ID
     * @param conversationId Conversation ID
     * @return Optional message
     */
    Optional<ChatMessage> findByIdAndConversationId(Long id, Long conversationId);

    /**
     * Find message by ID and user ID (security check)
     * @param id Message ID
     * @param userId User ID
     * @return Optional message
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.id = :id AND m.conversation.userId = :userId")
    Optional<ChatMessage> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}