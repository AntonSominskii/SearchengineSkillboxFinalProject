package searchengine.services.interfaces;

import java.util.List;
import java.util.Map;

public interface LemmaService {

    Map<String, Integer> getLemmasCountMap(String text);

    List<String> getLemmatizedList(List<String> list);
}