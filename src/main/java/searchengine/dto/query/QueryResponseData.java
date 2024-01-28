package searchengine.dto.query;

import lombok.Data;

import java.util.List;

@Data
public class QueryResponseData {
    private boolean result;
    private int count;
    private List<QueryResultPage> data;
    private String error;
}