package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

    List<ChatConversation> findByUserIdAndActiveOrderByUpdatedAtDesc(Long userId, boolean active);

    Optional<ChatConversation> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT c FROM ChatConversation c WHERE c.userId = :userId AND c.active = true ORDER BY c.updatedAt DESC")
    List<ChatConversation> findActiveConversationsByUser(Long userId);

    long countByUserIdAndActive(Long userId, boolean active);
}