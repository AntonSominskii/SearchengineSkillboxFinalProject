package searchengine.services.interfaces;

import org.springframework.http.ResponseEntity;
import searchengine.dto.query.ActionResponse;

public interface IndexingService {

    ResponseEntity<ActionResponse> startIndexing();

    ResponseEntity<ActionResponse> stopIndexing();

    ResponseEntity<ActionResponse> indexPage(String pagePath);
}
