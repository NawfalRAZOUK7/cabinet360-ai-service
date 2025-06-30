package com.cabinet360.ai.repository;

import com.cabinet360.ai.entity.ClinicalDecision;
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
 * 🩺 Clinical Decision Repository
 * Repository interface for managing clinical decision support data
 *
 * @author Cabinet360 Development Team
 * @version 1.0.0
 * @since 2024-06-30
 */
@Repository
public interface ClinicalDecisionRepository extends JpaRepository<ClinicalDecision, Long> {

    // ================================
    // MISSING METHOD - FIXED
    // ================================

    /**
     * Count clinical decisions created after specified date
     * @param date Date threshold
     * @return Count of clinical decisions created after the date
     */
    long countByCreatedAtAfter(LocalDateTime date);

    // ================================
    // Basic Finder Methods
    // ================================

    /**
     * Find all clinical decisions for a specific user
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of clinical decisions ordered by creation date (newest first)
     */
    List<ClinicalDecision> findByUserIdAndIsActiveOrderByCreatedAtDesc(Long userId, Boolean isActive);

    /**
     * Find clinical decisions by user ID with pagination
     * @param userId User ID
     * @param isActive Filter by active status
     * @param pageable Pagination parameters
     * @return Page of clinical decisions
     */
    Page<ClinicalDecision> findByUserIdAndIsActive(Long userId, Boolean isActive, Pageable pageable);

    /**
     * Find clinical decision by ID and user ID (security check)
     * @param id Clinical decision ID
     * @param userId User ID
     * @return Optional clinical decision
     */
    Optional<ClinicalDecision> findByIdAndUserId(Long id, Long userId);

    /**
     * Find clinical decisions by user email
     * @param userEmail User email
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByUserEmailAndIsActiveOrderByCreatedAtDesc(String userEmail, Boolean isActive);

    // ================================
    // Decision Type and Specialty Queries
    // ================================

    /**
     * Find clinical decisions by decision type
     * @param decisionType Decision type (DIAGNOSIS, TREATMENT, MONITORING, etc.)
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByDecisionTypeAndIsActiveOrderByCreatedAtDesc(String decisionType, Boolean isActive);

    /**
     * Find clinical decisions by user and decision type
     * @param userId User ID
     * @param decisionType Decision type
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByUserIdAndDecisionTypeAndIsActiveOrderByCreatedAtDesc(
            Long userId, String decisionType, Boolean isActive);

    /**
     * Find clinical decisions by medical specialty
     * @param specialty Medical specialty
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findBySpecialtyAndIsActiveOrderByCreatedAtDesc(String specialty, Boolean isActive);

    /**
     * Find clinical decisions by user and specialty
     * @param userId User ID
     * @param specialty Medical specialty
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByUserIdAndSpecialtyAndIsActiveOrderByCreatedAtDesc(
            Long userId, String specialty, Boolean isActive);

    // ================================
    // Urgency and Emergency Queries
    // ================================

    /**
     * Find emergency clinical decisions
     * @param isActive Filter by active status
     * @return List of emergency clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.isActive = :isActive " +
            "AND (cd.urgencyLevel = 'EMERGENCY' OR cd.isEmergencyDecision = true) " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findEmergencyDecisions(@Param("isActive") Boolean isActive);

    /**
     * Find high-priority clinical decisions
     * @param isActive Filter by active status
     * @return List of high-priority clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.isActive = :isActive " +
            "AND (cd.urgencyLevel IN ('EMERGENCY', 'HIGH') OR cd.isEmergencyDecision = true " +
            "OR cd.isHighComplexity = true) ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findHighPriorityDecisions(@Param("isActive") Boolean isActive);

    /**
     * Find urgent clinical decisions for a user
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of urgent clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId AND cd.isActive = :isActive " +
            "AND (cd.urgencyLevel IN ('EMERGENCY', 'HIGH') OR cd.isEmergencyDecision = true) " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findUrgentDecisionsByUser(@Param("userId") Long userId,
                                                     @Param("isActive") Boolean isActive);

    // ================================
    // Date Range and Time-based Queries
    // ================================

    /**
     * Find clinical decisions within a date range
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId " +
            "AND cd.createdAt BETWEEN :startDate AND :endDate " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findByUserIdAndDateRange(@Param("userId") Long userId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    @Param("isActive") Boolean isActive);

    /**
     * Find recent clinical decisions for a user (last N days)
     * @param userId User ID
     * @param daysAgo Number of days ago
     * @param isActive Filter by active status
     * @return List of recent clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId " +
            "AND cd.createdAt >= :daysAgo AND cd.isActive = :isActive " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findRecentDecisionsByUser(@Param("userId") Long userId,
                                                     @Param("daysAgo") LocalDateTime daysAgo,
                                                     @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions by decision timestamp range
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.decisionTimestamp BETWEEN :startDate AND :endDate " +
            "AND cd.isActive = :isActive ORDER BY cd.decisionTimestamp DESC")
    List<ClinicalDecision> findByDecisionTimestampRange(@Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate,
                                                        @Param("isActive") Boolean isActive);

    // ================================
    // Complexity and Risk Queries
    // ================================

    /**
     * Find complex clinical decisions
     * @param minComplexityScore Minimum complexity score
     * @param isActive Filter by active status
     * @return List of complex clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.complexityScore >= :minComplexityScore " +
            "AND cd.isActive = :isActive ORDER BY cd.complexityScore DESC, cd.createdAt DESC")
    List<ClinicalDecision> findComplexDecisions(@Param("minComplexityScore") Integer minComplexityScore,
                                                @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions with contraindications
     * @param isActive Filter by active status
     * @return List of clinical decisions with contraindications
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.hasContraindications = true " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findDecisionsWithContraindications(@Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions requiring monitoring
     * @param isActive Filter by active status
     * @return List of clinical decisions requiring monitoring
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.requiresMonitoring = true " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findDecisionsRequiringMonitoring(@Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions requiring specialist referral
     * @param isActive Filter by active status
     * @return List of clinical decisions requiring specialist referral
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.requiresSpecialistReferral = true " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findDecisionsRequiringReferral(@Param("isActive") Boolean isActive);

    // ================================
    // Implementation and Outcome Tracking
    // ================================

    /**
     * Find implemented clinical decisions
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of implemented clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId " +
            "AND cd.isImplemented = true AND cd.isActive = :isActive " +
            "ORDER BY cd.implementationDate DESC")
    List<ClinicalDecision> findImplementedDecisionsByUser(@Param("userId") Long userId,
                                                          @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions with tracked outcomes
     * @param isActive Filter by active status
     * @return List of clinical decisions with outcomes
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.outcomeTracked = true " +
            "AND cd.isActive = :isActive ORDER BY cd.outcomeDate DESC")
    List<ClinicalDecision> findDecisionsWithTrackedOutcomes(@Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions by outcome status
     * @param outcomeStatus Outcome status (SUCCESS, PARTIAL_SUCCESS, FAILURE, PENDING)
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByOutcomeStatusAndIsActiveOrderByOutcomeDateDesc(
            String outcomeStatus, Boolean isActive);

    /**
     * Find pending implementation decisions
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of decisions awaiting implementation
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId " +
            "AND cd.isImplemented = false AND cd.isActive = :isActive " +
            "ORDER BY cd.createdAt ASC")
    List<ClinicalDecision> findPendingImplementationByUser(@Param("userId") Long userId,
                                                           @Param("isActive") Boolean isActive);

    // ================================
    // Review and Quality Tracking
    // ================================

    /**
     * Find clinical decisions requiring peer review
     * @param isActive Filter by active status
     * @return List of clinical decisions needing peer review
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.peerReviewRequested = true " +
            "AND cd.peerReviewed = false AND cd.isActive = :isActive " +
            "ORDER BY cd.reviewPriority DESC, cd.createdAt ASC")
    List<ClinicalDecision> findDecisionsNeedingPeerReview(@Param("isActive") Boolean isActive);

    /**
     * Find peer-reviewed clinical decisions
     * @param peerReviewer Reviewer identifier
     * @param isActive Filter by active status
     * @return List of peer-reviewed clinical decisions
     */
    List<ClinicalDecision> findByPeerReviewerAndIsActiveOrderByPeerReviewDateDesc(
            String peerReviewer, Boolean isActive);

    /**
     * Find clinical decisions flagged for review
     * @param isActive Filter by active status
     * @return List of flagged clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.flaggedForReview = true " +
            "AND cd.isActive = :isActive ORDER BY cd.reviewPriority DESC, cd.createdAt ASC")
    List<ClinicalDecision> findFlaggedForReview(@Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions with user feedback
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of clinical decisions with feedback
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId AND cd.isActive = :isActive " +
            "AND cd.userFeedbackRating IS NOT NULL ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findDecisionsWithFeedback(@Param("userId") Long userId,
                                                     @Param("isActive") Boolean isActive);

    // ================================
    // Confidence and Evidence Queries
    // ================================

    /**
     * Find clinical decisions by confidence level
     * @param confidenceLevel Confidence level (HIGH, MODERATE, LOW)
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByConfidenceLevelAndIsActiveOrderByCreatedAtDesc(
            String confidenceLevel, Boolean isActive);

    /**
     * Find clinical decisions by evidence level
     * @param evidenceLevel Evidence level (HIGH, MODERATE, LOW, EXPERT_OPINION)
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByEvidenceLevelAndIsActiveOrderByCreatedAtDesc(
            String evidenceLevel, Boolean isActive);

    /**
     * Find high-confidence clinical decisions
     * @param minConfidenceScore Minimum confidence score
     * @param isActive Filter by active status
     * @return List of high-confidence clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.decisionConfidenceScore >= :minConfidenceScore " +
            "AND cd.isActive = :isActive ORDER BY cd.decisionConfidenceScore DESC")
    List<ClinicalDecision> findHighConfidenceDecisions(@Param("minConfidenceScore") Double minConfidenceScore,
                                                       @Param("isActive") Boolean isActive);

    // ================================
    // Patient Demographics Queries - FIXED TYPE CASTING
    // ================================

    /**
     * Find clinical decisions by patient age group - FIXED
     * @param ageGroup Age group (pediatric, adult, geriatric)
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.isActive = :isActive " +
            "AND cd.patientAge IS NOT NULL AND cd.patientAge != '' " +
            "AND cd.patientAge ~ '^[0-9]+$' " +
            "AND (" +
            "(:ageGroup = 'pediatric' AND CAST(cd.patientAge AS integer) < 18) " +
            "OR (:ageGroup = 'geriatric' AND CAST(cd.patientAge AS integer) >= 65) " +
            "OR (:ageGroup = 'adult' AND CAST(cd.patientAge AS integer) BETWEEN 18 AND 64)" +
            ") ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findByAgeGroup(@Param("ageGroup") String ageGroup,
                                          @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions by patient gender
     * @param patientGender Patient gender
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByPatientGenderAndIsActiveOrderByCreatedAtDesc(
            String patientGender, Boolean isActive);

    /**
     * Find clinical decisions by clinical setting
     * @param clinicalSetting Clinical setting (INPATIENT, OUTPATIENT, EMERGENCY, etc.)
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByClinicalSettingAndIsActiveOrderByCreatedAtDesc(
            String clinicalSetting, Boolean isActive);

    // ================================
    // Statistics and Analytics Queries
    // ================================

    /**
     * Count clinical decisions by decision type for a user
     * @param userId User ID
     * @param decisionType Decision type
     * @param isActive Filter by active status
     * @return Count of clinical decisions
     */
    long countByUserIdAndDecisionTypeAndIsActive(Long userId, String decisionType, Boolean isActive);

    /**
     * Count total active clinical decisions for a user
     * @param userId User ID
     * @param isActive Filter by active status
     * @return Total count
     */
    long countByUserIdAndIsActive(Long userId, Boolean isActive);

    /**
     * Get clinical decision statistics by specialty for a date range
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of objects containing statistics
     */
    @Query("SELECT cd.specialty, cd.decisionType, COUNT(cd) as count FROM ClinicalDecision cd " +
            "WHERE cd.createdAt BETWEEN :startDate AND :endDate AND cd.isActive = :isActive " +
            "GROUP BY cd.specialty, cd.decisionType ORDER BY count DESC")
    List<Object[]> getDecisionStatsBySpecialtyAndType(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate,
                                                      @Param("isActive") Boolean isActive);

    /**
     * Get urgency level distribution
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of urgency level counts
     */
    @Query("SELECT cd.urgencyLevel, COUNT(cd) as count FROM ClinicalDecision cd " +
            "WHERE cd.createdAt BETWEEN :startDate AND :endDate AND cd.isActive = :isActive " +
            "GROUP BY cd.urgencyLevel ORDER BY count DESC")
    List<Object[]> getUrgencyLevelDistribution(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("isActive") Boolean isActive);

    /**
     * Get average complexity score by specialty
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of specialty and average complexity
     */
    @Query("SELECT cd.specialty, AVG(cd.complexityScore) as avgComplexity FROM ClinicalDecision cd " +
            "WHERE cd.createdAt BETWEEN :startDate AND :endDate AND cd.isActive = :isActive " +
            "AND cd.complexityScore IS NOT NULL " +
            "GROUP BY cd.specialty ORDER BY avgComplexity DESC")
    List<Object[]> getAverageComplexityBySpecialty(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("isActive") Boolean isActive);

    // ================================
    // AI Analysis Tracking
    // ================================

    /**
     * Find clinical decisions by AI provider
     * @param aiProvider AI provider name
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByAiProviderAndIsActiveOrderByCreatedAtDesc(String aiProvider, Boolean isActive);

    /**
     * Find clinical decisions by analysis version
     * @param analysisVersion Analysis version
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    List<ClinicalDecision> findByAnalysisVersionAndIsActiveOrderByCreatedAtDesc(
            String analysisVersion, Boolean isActive);

    /**
     * Find slow analysis performance (for optimization)
     * @param maxDurationMs Maximum analysis duration in milliseconds
     * @param isActive Filter by active status
     * @return List of slow clinical decision analyses
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.analysisDurationMs > :maxDurationMs " +
            "AND cd.isActive = :isActive ORDER BY cd.analysisDurationMs DESC")
    List<ClinicalDecision> findSlowAnalyses(@Param("maxDurationMs") Long maxDurationMs,
                                            @Param("isActive") Boolean isActive);

    /**
     * Find high token usage analyses
     * @param minTokenCount Minimum token count
     * @param isActive Filter by active status
     * @return List of high token usage clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.tokenCount >= :minTokenCount " +
            "AND cd.isActive = :isActive ORDER BY cd.tokenCount DESC")
    List<ClinicalDecision> findHighTokenUsage(@Param("minTokenCount") Integer minTokenCount,
                                              @Param("isActive") Boolean isActive);

    // ================================
    // Learning and Training Queries
    // ================================

    /**
     * Find clinical decisions eligible for training
     * @param isActive Filter by active status
     * @return List of clinical decisions suitable for training
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.contributesToLearning = true " +
            "AND cd.flaggedForReview = false AND cd.isActive = :isActive " +
            "AND (cd.userFeedbackRating IS NULL OR cd.userFeedbackRating >= 3) " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findEligibleForTraining(@Param("isActive") Boolean isActive);

    /**
     * Find anonymized clinical decisions for research
     * @param isActive Filter by active status
     * @return List of anonymized clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.anonymizedForResearch = true " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findAnonymizedForResearch(@Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions used for training
     * @param isActive Filter by active status
     * @return List of clinical decisions used in training
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.usedForTraining = true " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findUsedForTraining(@Param("isActive") Boolean isActive);

    // ================================
    // Search and Text-based Queries
    // ================================

    /**
     * Search clinical decisions by scenario content
     * @param searchTerm Search term
     * @param userId User ID
     * @param isActive Filter by active status
     * @return List of matching clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId " +
            "AND cd.isActive = :isActive " +
            "AND (LOWER(cd.clinicalScenario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(cd.primaryRecommendation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> searchByContent(@Param("searchTerm") String searchTerm,
                                           @Param("userId") Long userId,
                                           @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions by medical history keywords
     * @param keyword Medical history keyword
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.isActive = :isActive " +
            "AND LOWER(cd.medicalHistory) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findByMedicalHistoryKeyword(@Param("keyword") String keyword,
                                                       @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions by medication keywords
     * @param medication Medication name
     * @param isActive Filter by active status
     * @return List of clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.isActive = :isActive " +
            "AND LOWER(cd.currentMedications) LIKE LOWER(CONCAT('%', :medication, '%')) " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findByMedicationKeyword(@Param("medication") String medication,
                                                   @Param("isActive") Boolean isActive);

    // ================================
    // Monitoring and Follow-up Queries
    // ================================

    /**
     * Find clinical decisions requiring follow-up
     * @param beforeDate Date threshold for follow-up
     * @param isActive Filter by active status
     * @return List of clinical decisions needing follow-up
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.isImplemented = true " +
            "AND cd.implementationDate < :beforeDate AND cd.outcomeTracked = false " +
            "AND cd.isActive = :isActive ORDER BY cd.implementationDate ASC")
    List<ClinicalDecision> findRequiringFollowUp(@Param("beforeDate") LocalDateTime beforeDate,
                                                 @Param("isActive") Boolean isActive);

    /**
     * Find stale clinical decisions (no updates in specified period)
     * @param beforeDate Date threshold for staleness
     * @param isActive Filter by active status
     * @return List of stale clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.updatedAt < :beforeDate " +
            "AND cd.isActive = :isActive ORDER BY cd.updatedAt ASC")
    List<ClinicalDecision> findStaleDecisions(@Param("beforeDate") LocalDateTime beforeDate,
                                              @Param("isActive") Boolean isActive);

    // ================================
    // Quality Metrics Queries
    // ================================

    /**
     * Find clinical decisions with high user satisfaction
     * @param minRating Minimum feedback rating
     * @param isActive Filter by active status
     * @return List of highly rated clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userFeedbackRating >= :minRating " +
            "AND cd.isActive = :isActive ORDER BY cd.userFeedbackRating DESC, cd.createdAt DESC")
    List<ClinicalDecision> findHighRatedDecisions(@Param("minRating") Integer minRating,
                                                  @Param("isActive") Boolean isActive);

    /**
     * Find clinical decisions with successful outcomes
     * @param isActive Filter by active status
     * @return List of clinical decisions with successful outcomes
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.outcomeStatus IN ('SUCCESS', 'PARTIAL_SUCCESS') " +
            "AND cd.isActive = :isActive ORDER BY cd.outcomeDate DESC")
    List<ClinicalDecision> findSuccessfulOutcomes(@Param("isActive") Boolean isActive);

    /**
     * Get quality metrics summary
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of quality metrics
     */
    @Query("SELECT " +
            "AVG(CASE WHEN cd.userFeedbackRating IS NOT NULL THEN cd.userFeedbackRating ELSE 0 END) as avgRating, " +
            "AVG(CASE WHEN cd.decisionConfidenceScore IS NOT NULL THEN cd.decisionConfidenceScore ELSE 0 END) as avgConfidence, " +
            "COUNT(CASE WHEN cd.isAccurate = true THEN 1 END) as accurateCount, " +
            "COUNT(CASE WHEN cd.isHelpful = true THEN 1 END) as helpfulCount, " +
            "COUNT(*) as totalCount " +
            "FROM ClinicalDecision cd " +
            "WHERE cd.createdAt BETWEEN :startDate AND :endDate AND cd.isActive = :isActive")
    List<Object[]> getQualityMetricsSummary(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("isActive") Boolean isActive);

    // ================================
    // Cleanup and Maintenance Queries
    // ================================

    /**
     * Find old clinical decisions for archival
     * @param beforeDate Date threshold for archival
     * @param isActive Filter by active status
     * @return List of old clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.createdAt < :beforeDate " +
            "AND cd.isActive = :isActive ORDER BY cd.createdAt ASC")
    List<ClinicalDecision> findOldDecisionsForArchival(@Param("beforeDate") LocalDateTime beforeDate,
                                                       @Param("isActive") Boolean isActive);

    /**
     * Find duplicate clinical decisions for cleanup
     * @param userId User ID
     * @param clinicalScenario Clinical scenario text
     * @param isActive Filter by active status
     * @return List of duplicate clinical decisions
     */
    @Query("SELECT cd FROM ClinicalDecision cd WHERE cd.userId = :userId " +
            "AND cd.clinicalScenario = :clinicalScenario AND cd.isActive = :isActive " +
            "ORDER BY cd.createdAt DESC")
    List<ClinicalDecision> findDuplicateDecisions(@Param("userId") Long userId,
                                                  @Param("clinicalScenario") String clinicalScenario,
                                                  @Param("isActive") Boolean isActive);

    // ================================
    // Custom Update and Batch Operations
    // ================================

    /**
     * Mark clinical decisions as peer reviewed in batch
     * @param ids List of clinical decision IDs
     * @param reviewerEmail Reviewer email
     * @param reviewDate Review timestamp
     * @param agreement Review agreement
     * @return Number of updated records
     */
    @Query("UPDATE ClinicalDecision cd SET cd.peerReviewed = true, cd.peerReviewer = :reviewerEmail, " +
            "cd.peerReviewDate = :reviewDate, cd.peerReviewAgreement = :agreement, cd.flaggedForReview = false " +
            "WHERE cd.id IN :ids")
    int markAsPeerReviewed(@Param("ids") List<Long> ids,
                           @Param("reviewerEmail") String reviewerEmail,
                           @Param("reviewDate") LocalDateTime reviewDate,
                           @Param("agreement") Boolean agreement);

    /**
     * Archive clinical decisions in batch
     * @param ids List of clinical decision IDs
     * @param archiverEmail Archiver email
     * @param archiveReason Reason for archival
     * @param archiveDate Archive timestamp
     * @return Number of archived records
     */
    @Query("UPDATE ClinicalDecision cd SET cd.isArchived = true, cd.archivedBy = :archiverEmail, " +
            "cd.archiveReason = :archiveReason, cd.archivedAt = :archiveDate, cd.isActive = false " +
            "WHERE cd.id IN :ids")
    int archiveDecisions(@Param("ids") List<Long> ids,
                         @Param("archiverEmail") String archiverEmail,
                         @Param("archiveReason") String archiveReason,
                         @Param("archiveDate") LocalDateTime archiveDate);

    /**
     * Update implementation status in batch
     * @param ids List of clinical decision IDs
     * @param implementerEmail Implementer email
     * @param implementationDate Implementation timestamp
     * @return Number of updated records
     */
    @Query("UPDATE ClinicalDecision cd SET cd.isImplemented = true, cd.implementedBy = :implementerEmail, " +
            "cd.implementationDate = :implementationDate WHERE cd.id IN :ids")
    int markAsImplemented(@Param("ids") List<Long> ids,
                          @Param("implementerEmail") String implementerEmail,
                          @Param("implementationDate") LocalDateTime implementationDate);

    /**
     * Flag clinical decisions for review in batch
     * @param ids List of clinical decision IDs
     * @param flagReason Reason for flagging
     * @param priority Review priority (1-5)
     * @return Number of flagged records
     */
    @Query("UPDATE ClinicalDecision cd SET cd.flaggedForReview = true, cd.flagReason = :flagReason, " +
            "cd.reviewPriority = :priority WHERE cd.id IN :ids")
    int flagForReview(@Param("ids") List<Long> ids,
                      @Param("flagReason") String flagReason,
                      @Param("priority") Integer priority);

    // ================================
    // Advanced Analytics Queries
    // ================================

    /**
     * Get decision type trends over time
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of decision type trends
     */
    @Query(value = "SELECT DATE_TRUNC('day', created_at) as date, decision_type, COUNT(*) as count " +
            "FROM clinical_decisions " +
            "WHERE created_at BETWEEN :startDate AND :endDate AND is_active = :isActive " +
            "GROUP BY DATE_TRUNC('day', created_at), decision_type " +
            "ORDER BY date DESC", nativeQuery = true)
    List<Object[]> getDecisionTypeTrends(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         @Param("isActive") Boolean isActive);

    /**
     * Get user engagement metrics
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of user engagement data
     */
    @Query("SELECT cd.userId, COUNT(cd) as decisionCount, " +
            "AVG(CASE WHEN cd.userFeedbackRating IS NOT NULL THEN cd.userFeedbackRating ELSE 0 END) as avgRating " +
            "FROM ClinicalDecision cd " +
            "WHERE cd.createdAt BETWEEN :startDate AND :endDate AND cd.isActive = :isActive " +
            "GROUP BY cd.userId ORDER BY decisionCount DESC")
    List<Object[]> getUserEngagementMetrics(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("isActive") Boolean isActive);

    /**
     * Get performance metrics by specialty
     * @param startDate Start date
     * @param endDate End date
     * @param isActive Filter by active status
     * @return List of specialty performance data
     */
    @Query("SELECT cd.specialty, " +
            "COUNT(cd) as totalDecisions, " +
            "AVG(cd.analysisDurationMs) as avgDuration, " +
            "AVG(cd.complexityScore) as avgComplexity, " +
            "COUNT(CASE WHEN cd.isAccurate = true THEN 1 END) as accurateCount " +
            "FROM ClinicalDecision cd " +
            "WHERE cd.createdAt BETWEEN :startDate AND :endDate AND cd.isActive = :isActive " +
            "AND cd.specialty IS NOT NULL " +
            "GROUP BY cd.specialty ORDER BY totalDecisions DESC")
    List<Object[]> getPerformanceMetricsBySpecialty(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    @Param("isActive") Boolean isActive);
}