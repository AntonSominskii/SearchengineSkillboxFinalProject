package searchengine.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.query.ActionResponse;
import searchengine.dto.query.QueryResponseData;
import searchengine.dto.query.StatisticsResponse;
import searchengine.services.interfaces.IndexingService;
import searchengine.services.interfaces.SearchService;
import searchengine.services.interfaces.StatisticsService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SearchService searchService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<ActionResponse> startIndexing() {
        return indexingService.startIndexing();
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<ActionResponse> stopIndexing() {
        return indexingService.stopIndexing();
    }

    @PostMapping("/indexPage")
    public ResponseEntity<ActionResponse> indexPage(@RequestParam(name = "url", required = false) String path) {
        return indexingService.indexPage(path);
    }

    @GetMapping("/search")
    public ResponseEntity<QueryResponseData> search(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "site", required = false) String site,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        logger.info("Search request received: query={}, site={}, offset={}, limit={}", query, site, offset, limit);
        ResponseEntity<QueryResponseData> response = searchService.search(query, site, offset, limit);
        logger.info("Search response: {}", response);
        return response;
    }
}