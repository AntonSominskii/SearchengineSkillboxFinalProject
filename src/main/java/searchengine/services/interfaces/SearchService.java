package searchengine.services.interfaces;

import org.springframework.http.ResponseEntity;
import searchengine.dto.query.QueryResponseData;

public interface SearchService {

    ResponseEntity<QueryResponseData> search(String query, String site, int offset, int limit);
}