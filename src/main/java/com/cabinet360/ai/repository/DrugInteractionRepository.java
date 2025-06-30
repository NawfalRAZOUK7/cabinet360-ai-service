package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.DrugInteraction;
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
 * 💊 Drug Interaction Repository
 * Repository interface for managing drug interaction analysis data
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@Repository
public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, Long> {

    // ================================
    // CRITICAL METHOD FOR ANALYTICS - MUST BE FIRST
    // ================================

    /**
     * Count drug interactions created after specified date
     * @param date Date threshold
     * @return Count of drug interactions created after the date
     */
    long countByCreatedAtAfter(LocalDateTime date);

    // ================================
    // Basic Finder Methods
    // ================================

    /**
     * Find all drug interactions for a specific user
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of drug interactions ordered by creation date (newest first)
     */
    List<DrugInteraction> findByUserIdAndIsActiveOrderByCreatedAtDesc(Long userId, Boolean isActive);

    /**
     * Find drug interactions by user ID with pagination
     * @param userId User ID
     * @param isActive Filter by active status
     * @param pageable Pagination parameters
     * @return Page of drug interactions
     */
    Page<DrugInteraction> findByUserIdAndIsActive(Long userId, Boolean isActive, Pageable pageable);

    /**
     * Find drug interaction by ID and user ID (security check)
     * @param id Drug interaction ID
     * @param userId User ID
     * @return Optional drug interaction
     */
    Optional<DrugInteraction> findByIdAndUserId(Long id, Long userId);

    // ================================
    // Risk Level and Urgency Queries
    // ================================

    /**
     * Find drug interactions by risk level
     * @param riskLevel Risk level (LOW, MODERATE, HIGH, CRITICAL)
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    List<DrugInteraction> findByRiskLevelAndIsActiveOrderByCreatedAtDesc(String riskLevel, Boolean isActive);

    /**
     * Find drug interactions by user and risk level
     * @param userId User ID
     * @param riskLevel Risk level
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    List<DrugInteraction> findByUserIdAndRiskLevelAndIsActiveOrderByCreatedAtDesc(
            Long userId, String riskLevel, Boolean isActive);

    /**
     * Find urgent drug interactions requiring immediate attention
     * @param isActive Filter by active status
     * @return List of urgent drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.isActive = :isActive " +
            "AND (di.urgency = 'HIGH' OR di.requiresImmediateAttention = true OR di.hasCriticalInteractions = true) " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findUrgentInteractions(@Param("isActive") Boolean isActive);

    /**
     * Find critical drug interactions for a user
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of critical drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userId = :userId AND di.isActive = :isActive " +
            "AND (di.hasCriticalInteractions = true OR di.hasEmergencyWarnings = true) " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findCriticalInteractionsByUser(@Param("userId") Long userId,
                                                         @Param("isActive") Boolean isActive);

    // ================================
    // Date Range and Time-based Queries
    // ================================

    /**
     * Find drug interactions within a date range
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userId = :userId " +
            "AND di.createdAt BETWEEN :startDate AND :endDate " +
            "AND di.isActive = :isActive ORDER BY di.createdAt DESC")
    List<DrugInteraction> findByUserIdAndDateRange(@Param("userId") Long userId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("isActive") Boolean isActive);

    /**
     * Find recent drug interactions for a user (last N days)
     * @param userId User ID
     * @param daysAgo Number of days ago
     * @param isActive Filter by active status
     * @return List of recent drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userId = :userId " +
            "AND di.createdAt >= :daysAgo AND di.isActive = :isActive " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findRecentInteractionsByUser(@Param("userId") Long userId,
                                                       @Param("daysAgo") LocalDateTime daysAgo,
                                                       @Param("isActive") Boolean isActive);

    // ================================
    // Review and Quality Tracking
    // ================================

    /**
     * Find drug interactions requiring review
     * @param isActive Filter by active status
     * @return List of drug interactions needing review
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.isActive = :isActive " +
            "AND (di.isReviewed = false AND (di.riskLevel IN ('HIGH', 'CRITICAL') " +
            "OR di.hasCriticalInteractions = true)) ORDER BY di.createdAt ASC")
    List<DrugInteraction> findInteractionsNeedingReview(@Param("isActive") Boolean isActive);

    /**
     * Find reviewed drug interactions by reviewer
     * @param reviewedBy Reviewer email/identifier
     * @param isActive Filter by active status
     * @return List of reviewed drug interactions
     */
    List<DrugInteraction> findByReviewedByAndIsActiveOrderByReviewedAtDesc(String reviewedBy, Boolean isActive);

    /**
     * Find drug interactions with user feedback
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of drug interactions with feedback
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userId = :userId AND di.isActive = :isActive " +
            "AND di.userFeedbackRating IS NOT NULL ORDER BY di.createdAt DESC")
    List<DrugInteraction> findInteractionsWithFeedback(@Param("userId") Long userId,
                                                       @Param("isActive") Boolean isActive);

    // ================================
    // Medication-specific Queries
    // ================================

    /**
     * Find drug interactions involving specific medications
     * @param medicationName Medication name (partial match)
     * @param isActive Filter by active status
     * @return List of drug interactions containing the medication
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.isActive = :isActive " +
            "AND LOWER(di.medicationList) LIKE LOWER(CONCAT('%', :medicationName, '%')) " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findByMedicationName(@Param("medicationName") String medicationName,
                                               @Param("isActive") Boolean isActive);

    /**
     * Find drug interactions by medication count
     * @param medicationCount Number of medications
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    List<DrugInteraction> findByMedicationCountAndIsActiveOrderByCreatedAtDesc(
            Integer medicationCount, Boolean isActive);

    /**
     * Find complex drug interactions (high medication count)
     * @param minMedicationCount Minimum number of medications
     * @param isActive Filter by active status
     * @return List of complex drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.medicationCount >= :minMedicationCount " +
            "AND di.isActive = :isActive ORDER BY di.medicationCount DESC, di.createdAt DESC")
    List<DrugInteraction> findComplexInteractions(@Param("minMedicationCount") Integer minMedicationCount,
                                                  @Param("isActive") Boolean isActive);

    // ================================
    // Statistics and Analytics Queries
    // ================================

    /**
     * Count drug interactions by risk level for a user
     * @param userId User ID
     * @param riskLevel Risk level
     * @param isActive Filter by active status
     * @return Count of drug interactions
     */
    long countByUserIdAndRiskLevelAndIsActive(Long userId, String riskLevel, Boolean isActive);

    /**
     * Count total active drug interactions for a user
     * @param userId User ID
     * @param isActive Filter by active status
     * @return Total count
     */
    long countByUserIdAndIsActive(Long userId, Boolean isActive);

    /**
     * Get drug interaction statistics for a date range
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of objects containing statistics
     */
    @Query("SELECT di.riskLevel, COUNT(di) as count FROM DrugInteraction di " +
            "WHERE di.createdAt BETWEEN :startDate AND :endDate AND di.isActive = :isActive " +
            "GROUP BY di.riskLevel ORDER BY count DESC")
    List<Object[]> getInteractionStatsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  @Param("isActive") Boolean isActive);

    /**
     * Find most frequently analyzed medication combinations
     * @param limit Maximum number of results
     * @param isActive Filter by active status
     * @return List of medication combinations with counts
     */
    @Query(value = "SELECT medication_list, COUNT(*) as frequency " +
            "FROM drug_interactions " +
            "WHERE is_active = :isActive " +
            "GROUP BY medication_list " +
            "ORDER BY frequency DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findMostFrequentMedicationCombinations(@Param("limit") Integer limit,
                                                          @Param("isActive") Boolean isActive);

    // ================================
    // AI Analysis Tracking
    // ================================

    /**
     * Find drug interactions by AI provider
     * @param aiProvider AI provider name
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    List<DrugInteraction> findByAiProviderAndIsActiveOrderByCreatedAtDesc(String aiProvider, Boolean isActive);

    /**
     * Find drug interactions by confidence score range
     * @param minConfidence Minimum confidence score
     * @param maxConfidence Maximum confidence score
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.confidenceScore BETWEEN :minConfidence AND :maxConfidence " +
            "AND di.isActive = :isActive ORDER BY di.confidenceScore DESC")
    List<DrugInteraction> findByConfidenceScoreRange(@Param("minConfidence") Double minConfidence,
                                                     @Param("maxConfidence") Double maxConfidence,
                                                     @Param("isActive") Boolean isActive);

    /**
     * Find slow analysis performance (for optimization)
     * @param maxDurationMs Maximum analysis duration in milliseconds
     * @param isActive Filter by active status
     * @return List of slow drug interaction analyses
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.analysisDurationMs > :maxDurationMs " +
            "AND di.isActive = :isActive ORDER BY di.analysisDurationMs DESC")
    List<DrugInteraction> findSlowAnalyses(@Param("maxDurationMs") Long maxDurationMs,
                                           @Param("isActive") Boolean isActive);

    // ================================
    // Patient Safety Queries
    // ================================

    /**
     * Find all high-risk drug interactions for patient safety monitoring
     * @param isActive Filter by active status
     * @return List of high-risk drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.isActive = :isActive " +
            "AND (di.riskLevel IN ('HIGH', 'CRITICAL') OR di.hasCriticalInteractions = true " +
            "OR di.hasEmergencyWarnings = true OR di.requiresImmediateAttention = true) " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findHighRiskInteractionsForSafety(@Param("isActive") Boolean isActive);

    /**
     * Find drug interactions by patient age group (for population studies)
     * @param ageGroup Age group (pediatric, adult, geriatric)
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.isActive = :isActive " +
            "AND ((di.patientAge LIKE '%pediatric%' AND :ageGroup = 'pediatric') " +
            "OR (di.patientAge LIKE '%geriatric%' AND :ageGroup = 'geriatric') " +
            "OR (di.patientAge NOT LIKE '%pediatric%' AND di.patientAge NOT LIKE '%geriatric%' " +
            "AND :ageGroup = 'adult')) ORDER BY di.createdAt DESC")
    List<DrugInteraction> findByAgeGroup(@Param("ageGroup") String ageGroup,
                                         @Param("isActive") Boolean isActive);

    // ================================
    // Cleanup and Maintenance Queries
    // ================================

    /**
     * Find old drug interactions for archival
     * @param beforeDate Date threshold for archival
     * @param isActive Filter by active status
     * @return List of old drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.createdAt < :beforeDate " +
            "AND di.isActive = :isActive ORDER BY di.createdAt ASC")
    List<DrugInteraction> findOldInteractionsForArchival(@Param("beforeDate") LocalDateTime beforeDate,
                                                         @Param("isActive") Boolean isActive);

    /**
     * Find duplicate drug interactions for cleanup
     * @param userId User ID
     * @param medicationList Medication list JSON
     * @param isActive Filter by active status
     * @return List of duplicate drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userId = :userId " +
            "AND di.medicationList = :medicationList AND di.isActive = :isActive " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findDuplicateInteractions(@Param("userId") Long userId,
                                                    @Param("medicationList") String medicationList,
                                                    @Param("isActive") Boolean isActive);

    // ================================
    // Custom Update Queries
    // ================================

    /**
     * Mark drug interactions as reviewed in batch
     * @param ids List of drug interaction IDs
     * @param reviewedBy Reviewer identifier
     * @param reviewedAt Review timestamp
     * @return Number of updated records
     */
    @Query("UPDATE DrugInteraction di SET di.isReviewed = true, di.reviewedBy = :reviewedBy, " +
            "di.reviewedAt = :reviewedAt WHERE di.id IN :ids")
    int markAsReviewed(@Param("ids") List<Long> ids,
                       @Param("reviewedBy") String reviewedBy,
                       @Param("reviewedAt") LocalDateTime reviewedAt);

    /**
     * Archive old drug interactions in batch
     * @param ids List of drug interaction IDs
     * @return Number of archived records
     */
    @Query("UPDATE DrugInteraction di SET di.isActive = false WHERE di.id IN :ids")
    int archiveInteractions(@Param("ids") List<Long> ids);

    // ================================
    // Advanced Analytics Queries
    // ================================

    /**
     * Get interaction trends over time
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of interaction trends
     */
    @Query(value = "SELECT DATE_TRUNC('day', created_at) as date, risk_level, COUNT(*) as count " +
            "FROM drug_interactions " +
            "WHERE created_at BETWEEN :startDate AND :endDate AND is_active = :isActive " +
            "GROUP BY DATE_TRUNC('day', created_at), risk_level " +
            "ORDER BY date DESC", nativeQuery = true)
    List<Object[]> getInteractionTrends(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        @Param("isActive") Boolean isActive);

    /**
     * Get user engagement metrics for drug interactions
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of user engagement data
     */
    @Query("SELECT di.userId, COUNT(di) as interactionCount, " +
            "AVG(CASE WHEN di.userFeedbackRating IS NOT NULL THEN di.userFeedbackRating ELSE 0 END) as avgRating " +
            "FROM DrugInteraction di " +
            "WHERE di.createdAt BETWEEN :startDate AND :endDate AND di.isActive = :isActive " +
            "GROUP BY di.userId ORDER BY interactionCount DESC")
    List<Object[]> getUserEngagementMetrics(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("isActive") Boolean isActive);

    /**
     * Get performance metrics by risk level
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of performance data by risk level
     */
    @Query("SELECT di.riskLevel, " +
            "COUNT(di) as totalInteractions, " +
            "AVG(di.analysisDurationMs) as avgDuration, " +
            "AVG(di.complexityScore) as avgComplexity, " +
            "COUNT(CASE WHEN di.isAccurate = true THEN 1 END) as accurateCount " +
            "FROM DrugInteraction di " +
            "WHERE di.createdAt BETWEEN :startDate AND :endDate AND di.isActive = :isActive " +
            "AND di.riskLevel IS NOT NULL " +
            "GROUP BY di.riskLevel ORDER BY totalInteractions DESC")
    List<Object[]> getPerformanceMetricsByRiskLevel(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    @Param("isActive") Boolean isActive);

    // ================================
    // Search and Discovery Queries
    // ================================

    /**
     * Search drug interactions by content
     * @param searchTerm Search term
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of matching drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userId = :userId " +
            "AND di.isActive = :isActive " +
            "AND (LOWER(di.medicationList) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(di.overallRiskAssessment) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> searchByContent(@Param("searchTerm") String searchTerm,
                                          @Param("userId") Long userId,
                                          @Param("isActive") Boolean isActive);

    /**
     * Find drug interactions by patient condition keywords
     * @param condition Patient condition keyword
     * @param isActive Filter by active status
     * @return List of drug interactions
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.isActive = :isActive " +
            "AND LOWER(di.patientConditions) LIKE LOWER(CONCAT('%', :condition, '%')) " +
            "ORDER BY di.createdAt DESC")
    List<DrugInteraction> findByPatientCondition(@Param("condition") String condition,
                                                 @Param("isActive") Boolean isActive);

    /**
     * Find interactions requiring follow-up
     * @param beforeDate Date threshold for follow-up
     * @param isActive Filter by active status
     * @return List of interactions needing follow-up
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.riskLevel IN ('HIGH', 'CRITICAL') " +
            "AND di.createdAt < :beforeDate AND di.isReviewed = false " +
            "AND di.isActive = :isActive ORDER BY di.createdAt ASC")
    List<DrugInteraction> findRequiringFollowUp(@Param("beforeDate") LocalDateTime beforeDate,
                                                @Param("isActive") Boolean isActive);

    /**
     * Find high-rated drug interaction analyses
     * @param minRating Minimum feedback rating
     * @param isActive Filter by active status
     * @return List of highly rated analyses
     */
    @Query("SELECT di FROM DrugInteraction di WHERE di.userFeedbackRating >= :minRating " +
            "AND di.isActive = :isActive ORDER BY di.userFeedbackRating DESC, di.createdAt DESC")
    List<DrugInteraction> findHighRatedAnalyses(@Param("minRating") Integer minRating,
                                                @Param("isActive") Boolean isActive);

    /**
     * Get quality metrics summary
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of quality metrics
     */
    @Query("SELECT " +
            "AVG(CASE WHEN di.userFeedbackRating IS NOT NULL THEN di.userFeedbackRating ELSE 0 END) as avgRating, " +
            "AVG(CASE WHEN di.confidenceScore IS NOT NULL THEN di.confidenceScore ELSE 0 END) as avgConfidence, " +
            "COUNT(CASE WHEN di.isAccurate = true THEN 1 END) as accurateCount, " +
            "COUNT(CASE WHEN di.isHelpful = true THEN 1 END) as helpfulCount, " +
            "COUNT(*) as totalCount " +
            "FROM DrugInteraction di " +
            "WHERE di.createdAt BETWEEN :startDate AND :endDate AND di.isActive = :isActive")
    List<Object[]> getQualityMetricsSummary(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("isActive") Boolean isActive);
}