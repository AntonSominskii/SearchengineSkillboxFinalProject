package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.query.ActionResponse;
import searchengine.dto.query.QueryResponseData;
import searchengine.dto.query.StatisticsResponse;
import searchengine.services.interfaces.IndexingService;
import searchengine.services.interfaces.SearchService;
import searchengine.services.interfaces.StatisticsService;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class ApiController {

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SearchService searchService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        try {
            return ResponseEntity.ok(statisticsService.getStatistics());
        } catch (Exception e) {
            log.error("Error when getting statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<ActionResponse> startIndexing() {
        log.info("Indexing request received");
        ResponseEntity<ActionResponse> response = indexingService.startIndexing();
        log.info("Indexing started: {}", response.getBody());
        return response;
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<ActionResponse> stopIndexing() {
        log.info("Stop indexing request received");
        ResponseEntity<ActionResponse> response = indexingService.stopIndexing();
        log.info("Indexing stopped: {}", response.getBody());
        return response;
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
        log.info("Search request received: query={}, site={}, offset={}, limit={}", query, site, offset, limit);
        ResponseEntity<QueryResponseData> response = searchService.search(query, site, offset, limit);
        log.info("Search response: {}", response);
        return response;
    }
}