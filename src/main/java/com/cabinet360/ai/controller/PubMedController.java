package com.cabinet360.ai.controller;

import com.cabinet360.ai.dto.request.PubMedSearchRequest;
import com.cabinet360.ai.dto.response.PubMedSearchResponse;
import com.cabinet360.ai.entity.PubMedSearch;
import com.cabinet360.ai.service.PubMedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai/pubmed")
@PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
public class PubMedController {

    private final PubMedService pubMedService;

    public PubMedController(PubMedService pubMedService) {
        this.pubMedService = pubMedService;
    }

    @PostMapping("/search")
    public ResponseEntity<PubMedSearchResponse> searchPubMed(
            @Valid @RequestBody PubMedSearchRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");

        PubMedSearchResponse response = pubMedService.searchPubMed(
                request.getQuery(),
                request.getMaxResults(),
                userId,
                request.getPatientContext()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/history")
    public ResponseEntity<List<PubMedSearch>> getSearchHistory(
            @RequestParam(defaultValue = "20") int limit,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        List<PubMedSearch> history = pubMedService.getUserSearchHistory(userId, limit);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/quick-search")
    public ResponseEntity<PubMedSearchResponse> quickSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int maxResults,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");

        PubMedSearchResponse response = pubMedService.searchPubMed(
                query,
                maxResults,
                userId,
                null
        );

        return ResponseEntity.ok(response);
    }
}
