package searchengine.dto.query;

import lombok.Data;

@Data
public class QueryResultPage {
    private String site;
    private String siteName;
    private String uri;
    private String title;
    private String snippet;
    private float relevance;
}