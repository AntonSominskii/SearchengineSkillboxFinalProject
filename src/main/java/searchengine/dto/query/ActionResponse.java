package searchengine.dto.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResponse {
    private boolean result;
    private String error;
}