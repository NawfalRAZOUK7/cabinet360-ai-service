package com.cabinet360.ai.service;

import com.cabinet360.ai.dto.response.PubMedArticleDto;
import com.cabinet360.ai.dto.response.PubMedSearchResponse;
import com.cabinet360.ai.entity.PubMedArticle;
import com.cabinet360.ai.entity.PubMedSearch;
import com.cabinet360.ai.repository.PubMedArticleRepository;
import com.cabinet360.ai.repository.PubMedSearchRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 🆓 Updated PubMed Service using FREE AI for article summaries
 */
@Service
public class PubMedService {

    private static final Logger logger = LoggerFactory.getLogger(PubMedService.class);

    private final WebClient webClient;
    private final PubMedSearchRepository searchRepository;
    private final PubMedArticleRepository articleRepository;
    private final FreeAiService freeAiService; // 🆓 Using free AI instead of OpenAI
    private final ObjectMapper objectMapper;

    @Value("${pubmed.api.base.url}")
    private String pubmedBaseUrl;

    @Value("${pubmed.api.key:}")
    private String apiKey;

    @Value("${pubmed.max.results}")
    private int maxResults;

    public PubMedService(
            WebClient.Builder webClientBuilder,
            PubMedSearchRepository searchRepository,
            PubMedArticleRepository articleRepository,
            FreeAiService freeAiService, // 🆓 Free AI service
            ObjectMapper objectMapper
    ) {
        this.webClient = webClientBuilder.build();
        this.searchRepository = searchRepository;
        this.articleRepository = articleRepository;
        this.freeAiService = freeAiService;
        this.objectMapper = objectMapper;
    }

    /**
     * 🔍 Search PubMed with FREE AI-generated summaries
     */
    public PubMedSearchResponse searchPubMed(String query, Integer maxResults, Long userId, String patientContext) {
        try {
            logger.info("Searching PubMed for query: {}", query);

            // Save search to database
            PubMedSearch search = new PubMedSearch(userId, query, extractSearchTerms(query));
            search.setSearchContext(patientContext);

            // Step 1: Search for PMIDs
            List<String> pmids = searchPubMedIds(query, maxResults != null ? maxResults : this.maxResults);

            if (pmids.isEmpty()) {
                search.setResultsCount(0);
                searchRepository.save(search);
                return new PubMedSearchResponse(new ArrayList<>(), 0, query);
            }

            // Step 2: Fetch article details with FREE AI summaries
            List<PubMedArticleDto> articles = fetchArticleDetailsWithFreeAI(pmids, userId);

            search.setResultsCount(articles.size());
            searchRepository.save(search);

            logger.info("Found {} articles for query: {}", articles.size(), query);
            return new PubMedSearchResponse(articles, articles.size(), query);

        } catch (Exception e) {
            logger.error("Error searching PubMed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search PubMed", e);
        }
    }

    /**
     * 🔍 Search PubMed for article IDs
     */
    private List<String> searchPubMedIds(String query, int maxResults) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String searchUrl = String.format(
                "%s/esearch.fcgi?db=pubmed&term=%s&retmax=%d&retmode=json",
                pubmedBaseUrl, encodedQuery, maxResults
        );

        if (!apiKey.isEmpty()) {
            searchUrl += "&api_key=" + apiKey;
        }

        try {
            String response = webClient.get()
                    .uri(searchUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode idList = rootNode.path("esearchresult").path("idlist");

            List<String> pmids = new ArrayList<>();
            if (idList.isArray()) {
                for (JsonNode idNode : idList) {
                    pmids.add(idNode.asText());
                }
            }

            return pmids;

        } catch (Exception e) {
            logger.error("Error fetching PMIDs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 📚 Fetch article details with FREE AI summaries
     */
    private List<PubMedArticleDto> fetchArticleDetailsWithFreeAI(List<String> pmids, Long userId) {
        if (pmids.isEmpty()) return new ArrayList<>();

        String pmidList = String.join(",", pmids);
        String fetchUrl = String.format(
                "%s/efetch.fcgi?db=pubmed&id=%s&retmode=xml",
                pubmedBaseUrl, pmidList
        );

        if (!apiKey.isEmpty()) {
            fetchUrl += "&api_key=" + apiKey;
        }

        try {
            String xmlResponse = webClient.get()
                    .uri(fetchUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseXmlResponseWithFreeAI(xmlResponse);

        } catch (Exception e) {
            logger.error("Error fetching article details: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 📄 Parse XML response and generate FREE AI summaries
     */
    private List<PubMedArticleDto> parseXmlResponseWithFreeAI(String xmlResponse) {
        List<PubMedArticleDto> articles = new ArrayList<>();

        try {
            // Simple XML parsing - in production, use proper XML parser
            String[] articleBlocks = xmlResponse.split("<PubmedArticle>");

            for (int i = 1; i < articleBlocks.length; i++) {
                try {
                    PubMedArticleDto article = parseArticleBlock(articleBlocks[i]);
                    if (article != null) {

                        // Check if article exists in database
                        Optional<PubMedArticle> existingArticle = articleRepository.findByPmid(article.getPmid());

                        if (existingArticle.isPresent()) {
                            // Use existing article with AI summary if available
                            PubMedArticle existing = existingArticle.get();
                            article.setAiSummary(existing.getAiSummary());
                            article.setRelevanceScore(existing.getRelevanceScore());
                        } else {
                            // Save new article and generate FREE AI summary
                            PubMedArticle newArticle = convertToEntity(article);

                            // 🆓 Generate AI summary using FREE service
                            if (article.getAbstractText() != null && !article.getAbstractText().isEmpty()) {

                                // Generate summary asynchronously to avoid blocking
                                CompletableFuture.supplyAsync(() -> {
                                    try {
                                        return freeAiService.generateArticleSummary(
                                                article.getTitle(),
                                                article.getAbstractText()
                                        );
                                    } catch (Exception e) {
                                        logger.warn("Failed to generate AI summary for PMID {}: {}",
                                                article.getPmid(), e.getMessage());
                                        return generateFallbackSummary(article.getTitle(), article.getAbstractText());
                                    }
                                }).thenAccept(summary -> {
                                    // Update article with AI summary
                                    newArticle.setAiSummary(summary);
                                    articleRepository.save(newArticle);
                                    article.setAiSummary(summary);
                                });

                                // For immediate response, provide a quick summary
                                article.setAiSummary("🤖 AI summary generating... Please refresh in a moment for the full summary.");
                            }

                            // Calculate simple relevance score
                            newArticle.setRelevanceScore(calculateRelevanceScore(article));
                            article.setRelevanceScore(newArticle.getRelevanceScore());

                            articleRepository.save(newArticle);
                        }

                        articles.add(article);
                    }
                } catch (Exception e) {
                    logger.warn("Error parsing article block: {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.error("Error parsing XML response: {}", e.getMessage());
        }

        return articles;
    }

    /**
     * 📊 Calculate simple relevance score
     */
    private Double calculateRelevanceScore(PubMedArticleDto article) {
        double score = 0.5; // Base score

        // Boost score for recent articles
        if (article.getPublicationDate() != null) {
            try {
                int year = Integer.parseInt(article.getPublicationDate().replaceAll("\\D", ""));
                int currentYear = java.time.LocalDate.now().getYear();
                if (year >= currentYear - 5) {
                    score += 0.2; // Recent articles get boost
                }
            } catch (Exception e) {
                // Ignore parsing errors
            }
        }

        // Boost score for clinical trials and systematic reviews
        String title = article.getTitle().toLowerCase();
        String abstractText = article.getAbstractText() != null ? article.getAbstractText().toLowerCase() : "";

        if (title.contains("clinical trial") || abstractText.contains("randomized")) {
            score += 0.2;
        }
        if (title.contains("systematic review") || title.contains("meta-analysis")) {
            score += 0.3;
        }
        if (title.contains("guidelines") || title.contains("recommendation")) {
            score += 0.2;
        }

        return Math.min(1.0, score); // Cap at 1.0
    }

    /**
     * 🔄 Generate fallback summary when AI fails
     */
    private String generateFallbackSummary(String title, String abstractText) {
        if (abstractText != null && abstractText.length() > 200) {
            // Simple extractive summary - first two sentences
            String[] sentences = abstractText.split("\\.");
            if (sentences.length >= 2) {
                return sentences[0].trim() + ". " + sentences[1].trim() + ".";
            }
        }

        return "This medical article discusses " + title.toLowerCase() + ". Please review the full abstract for detailed information.";
    }

    private PubMedArticleDto parseArticleBlock(String articleBlock) {
        try {
            // Extract PMID
            String pmid = extractXmlContent(articleBlock, "<PMID Version=\"1\">", "</PMID>");
            if (pmid == null) return null;

            // Extract title
            String title = extractXmlContent(articleBlock, "<ArticleTitle>", "</ArticleTitle>");
            if (title == null) title = "No title available";

            // Extract abstract
            String abstractText = extractXmlContent(articleBlock, "<AbstractText>", "</AbstractText>");

            // Extract authors
            String authors = extractAuthors(articleBlock);

            // Extract journal
            String journal = extractXmlContent(articleBlock, "<Title>", "</Title>");

            // Extract publication date
            String pubDate = extractPublicationDate(articleBlock);

            // Extract DOI
            String doi = extractDoi(articleBlock);

            PubMedArticleDto article = new PubMedArticleDto(pmid, title, abstractText);
            article.setAuthors(authors);
            article.setJournal(journal);
            article.setPublicationDate(pubDate);
            article.setDoi(doi);

            return article;

        } catch (Exception e) {
            logger.warn("Error parsing article: {}", e.getMessage());
            return null;
        }
    }

    private String extractXmlContent(String xml, String startTag, String endTag) {
        int startIndex = xml.indexOf(startTag);
        if (startIndex == -1) return null;

        startIndex += startTag.length();
        int endIndex = xml.indexOf(endTag, startIndex);
        if (endIndex == -1) return null;

        return xml.substring(startIndex, endIndex).trim();
    }

    private String extractAuthors(String articleBlock) {
        List<String> authors = new ArrayList<>();
        String[] authorBlocks = articleBlock.split("<Author ValidYN=\"Y\">");

        for (int i = 1; i < authorBlocks.length && i <= 3; i++) { // Limit to 3 authors
            String lastName = extractXmlContent(authorBlocks[i], "<LastName>", "</LastName>");
            String foreName = extractXmlContent(authorBlocks[i], "<ForeName>", "</ForeName>");

            if (lastName != null) {
                String fullName = lastName;
                if (foreName != null) {
                    fullName = foreName + " " + lastName;
                }
                authors.add(fullName);
            }
        }

        String authorsString = String.join(", ", authors);
        if (authors.size() < authorBlocks.length - 1) {
            authorsString += " et al.";
        }

        return authorsString;
    }

    private String extractPublicationDate(String articleBlock) {
        String year = extractXmlContent(articleBlock, "<Year>", "</Year>");
        String month = extractXmlContent(articleBlock, "<Month>", "</Month>");

        if (year != null) {
            return month != null ? month + " " + year : year;
        }

        return null;
    }

    private String extractDoi(String articleBlock) {
        return extractXmlContent(articleBlock, "<ELocationID EIdType=\"doi\" ValidYN=\"Y\">", "</ELocationID>");
    }

    private PubMedArticle convertToEntity(PubMedArticleDto dto) {
        PubMedArticle article = new PubMedArticle(dto.getPmid(), dto.getTitle(), dto.getAbstractText());
        article.setAuthors(dto.getAuthors());
        article.setJournal(dto.getJournal());
        article.setPublicationDate(dto.getPublicationDate());
        article.setDoi(dto.getDoi());
        return article;
    }

    private String extractSearchTerms(String query) {
        return query.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
    }

    public List<PubMedSearch> getUserSearchHistory(Long userId, int limit) {
        return searchRepository.findByUserIdOrderBySearchDateDesc(userId)
                .stream()
                .limit(limit)
                .toList();
    }

    /**
     * 🔍 Get articles with AI summaries
     */
    public List<PubMedArticleDto> getArticlesWithAiSummaries() {
        return articleRepository.findArticlesWithAiSummary()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    /**
     * 🔄 Regenerate AI summary for an article
     */
    public void regenerateAiSummary(String pmid) {
        Optional<PubMedArticle> articleOpt = articleRepository.findByPmid(pmid);
        if (articleOpt.isPresent()) {
            PubMedArticle article = articleOpt.get();

            // 🆓 Generate new summary using free AI
            CompletableFuture.supplyAsync(() -> {
                return freeAiService.generateArticleSummary(article.getTitle(), article.getAbstractText());
            }).thenAccept(newSummary -> {
                article.setAiSummary(newSummary);
                articleRepository.save(article);
            });
        }
    }

    private PubMedArticleDto convertEntityToDto(PubMedArticle entity) {
        PubMedArticleDto dto = new PubMedArticleDto(entity.getPmid(), entity.getTitle(), entity.getAbstractText());
        dto.setAuthors(entity.getAuthors());
        dto.setJournal(entity.getJournal());
        dto.setPublicationDate(entity.getPublicationDate());
        dto.setDoi(entity.getDoi());
        dto.setAiSummary(entity.getAiSummary());
        dto.setRelevanceScore(entity.getRelevanceScore());
        return dto;
    }
}