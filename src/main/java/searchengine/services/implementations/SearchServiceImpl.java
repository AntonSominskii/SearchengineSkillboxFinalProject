package searchengine.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import searchengine.dto.query.QueryResponseData;
import searchengine.dto.query.QueryResultPage;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.model.Status;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.interfaces.LemmaService;
import searchengine.services.interfaces.SearchService;
import searchengine.utilities.ConfigSettings;
import searchengine.utilities.TextHelper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SearchServiceImpl implements SearchService {

    private final LemmaService lemmaService;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final ConfigSettings properties;

    @Override
    public ResponseEntity<QueryResponseData> search(String query, String site, int offset, int limit) {
        QueryResponseData searchResult = new QueryResponseData();
        searchResult.setResult(false);
        if (!isQueryExists(query)) {
            searchResult.setError("Empty query query");
        } else if (isIndexingOrFailed(site)) {
            searchResult.setError("Indexing not finished yet successfully");
        } else {
            searchResult = getSearchResult(query, site, offset, limit);
        }
        return ResponseEntity.ok(searchResult);
    }

    private boolean isQueryExists(String query) {
        return TextHelper.isStringExists(query);
    }

    private boolean isIndexingOrFailed(String siteName) {
        siteName = siteName + "/";
        SiteEntity siteEntity = siteRepository.findSiteEntityByUrl(siteName);
        if (siteEntity != null) {
            return !siteEntity.getStatus().equals(Status.INDEXED);
        }
        return siteRepository.existsByStatus(Status.INDEXING) || siteRepository.existsByStatus(Status.FAILED);
    }

    private QueryResponseData getSearchResult(String query, String site, int offset, int limit) {
        site = site + "/";
        QueryResponseData searchResult = getSearchResult(query, site);

        int noOfPages = searchResult.getCount();
        int dataArrayEndIndex = Math.min(noOfPages, offset + limit);
        int dataValueSize;
        if (noOfPages < offset) {
            dataValueSize = 0;
        } else {
            dataValueSize = dataArrayEndIndex - offset;
        }
        List<QueryResultPage> allPages = searchResult.getData();
        List<QueryResultPage> limitedPages = new ArrayList<>();
        for (int i = offset; i < offset + dataValueSize; i++) {
            limitedPages.add(allPages.get(i));
        }
        searchResult.setData(limitedPages);
        return searchResult;
    }

    private QueryResponseData getSearchResult(String query, String siteUrl) {
        QueryResponseData queryResponseData = new QueryResponseData();
        queryResponseData.setResult(true);

        List<SiteEntity> siteEntityList = siteRepository.findAll();
        SiteEntity searchingSite = getSearchingSiteEntity(siteEntityList, siteUrl);
        List<LemmaEntity> sortedLemmasFromQuery = getSortedByFrequencyAscLemmasQueryList(query, searchingSite);
        List<LemmaEntity> frequentLemmas = getFrequentLemmas(siteEntityList, sortedLemmasFromQuery);
        if (sortedLemmasFromQuery.isEmpty() || sortedLemmasFromQuery.size() == frequentLemmas.size()) {
            return returnEmptySearchResult(queryResponseData);
        }

        Set<Integer> pagesIdSet = getAllPagesId(sortedLemmasFromQuery);
        List<PageEntity> pagesFound = pageRepository.findAllById(pagesIdSet);
        if (searchingSite == null) {
            pagesFound = filterPages(pagesFound, sortedLemmasFromQuery, frequentLemmas);
        }
        if (pagesFound.isEmpty()) {
            return returnEmptySearchResult(queryResponseData);
        }

        List<QueryResultPage> pagesFoundSorted = getSortedPages(pagesFound, sortedLemmasFromQuery);
        return putPagesIntoSearchResultResponse(queryResponseData, pagesFoundSorted);
    }

    private QueryResponseData putPagesIntoSearchResultResponse(
            QueryResponseData response, List<QueryResultPage> pageList
    ) {
        response.setCount(pageList.size());
        response.setData(pageList);
        return response;
    }

    private List<PageEntity> filterPages(
            List<PageEntity> pages, List<LemmaEntity> allLemmas, List<LemmaEntity> frequentLemmas
    ) {
        List<PageEntity> filteredPages = pages;
        Map<SiteEntity, List<LemmaEntity>> groupedLemmas =
                allLemmas.stream().collect(Collectors.groupingBy(LemmaEntity::getSite));
        Map<SiteEntity, List<LemmaEntity>> groupedFrequentLemmas =
                frequentLemmas.stream().collect(Collectors.groupingBy(LemmaEntity::getSite));
        for (SiteEntity site : groupedLemmas.keySet()) {
            if ((groupedLemmas.containsKey(site) && groupedFrequentLemmas.containsKey(site))
                    && groupedLemmas.get(site).size() == groupedFrequentLemmas.get(site).size()) {
                filteredPages = filteredPages.stream()
                        .filter(pageEntity -> !pageEntity.getSite().equals(site)).toList();
            }
        }
        return filteredPages;
    }

    private QueryResponseData returnEmptySearchResult(QueryResponseData searchResult) {
        log.info("Nothing found!");
        searchResult.setCount(0);
        return searchResult;
    }

    private SiteEntity getSearchingSiteEntity(List<SiteEntity> sites, String url) {
        for (SiteEntity siteEntity : sites) {
            if (siteEntity.getUrl().equals(url)) {
                return siteEntity;
            }
        }
        return null;
    }

    private List<QueryResultPage> getSortedPages(List<PageEntity> pages, List<LemmaEntity> lemmaList) {
        List<QueryResultPage> queryResultPageList = new ArrayList<>();
        List<Integer> lemmasIdList = lemmaList.stream().map(LemmaEntity::getId).toList();
        Set<String> lemmasStringSet = lemmaList.stream().map(LemmaEntity::getLemma).collect(Collectors.toSet());

        for (PageEntity pageEntity : pages) {
            QueryResultPage queryResultPage = createSearchResultPage(pageEntity, lemmasIdList, lemmasStringSet);
            if (queryResultPage.getSnippet().isEmpty()) {
                continue;
            }
            queryResultPageList.add(queryResultPage);
        }
        queryResultPageList.sort(Comparator.comparing(QueryResultPage::getRelevance).reversed()
                .thenComparing(QueryResultPage::getTitle));
        convertAbsoluteRelevanceToRelative(queryResultPageList);
        return queryResultPageList;
    }

    private QueryResultPage createSearchResultPage(
            PageEntity pageEntity, List<Integer> lemmasIdList, Set<String> lemmasStringSet
    ) {
        SiteEntity site = pageEntity.getSite();
        String siteUrl = TextHelper.cutSlash(site.getUrl());
        String siteName = site.getName();
        String pagePath = pageEntity.getPath();
        Document document = Jsoup.parse(pageEntity.getContent());
        String title = document.title();

        String snippet = getSnippet(document, lemmasStringSet);
        Float relevanceWrapped = indexRepository.getAbsRelevance(pageEntity.getId(), lemmasIdList);
        float relevance = relevanceWrapped == null ? 0 : relevanceWrapped;

        QueryResultPage queryResultPage = new QueryResultPage();
        queryResultPage.setSite(siteUrl);
        queryResultPage.setSiteName(siteName);
        queryResultPage.setUri(pagePath);
        queryResultPage.setTitle(title);
        queryResultPage.setSnippet(snippet);
        queryResultPage.setRelevance(relevance);
        return queryResultPage;
    }

    private Set<Integer> getAllPagesId(List<LemmaEntity> lemmasQueryList) {
        Map<String, Integer> frequencyLemmaMap = lemmasQueryList.stream()
                .collect(Collectors.groupingBy(LemmaEntity::getLemma, Collectors.summingInt(LemmaEntity::getFrequency)));

        List<String> lemmasSortedAscList =
                frequencyLemmaMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();

        Map<String, Set<Integer>> gropedLemmaIds = lemmasQueryList.stream().collect(
                Collectors.groupingBy(LemmaEntity::getLemma, Collectors.mapping(LemmaEntity::getId, Collectors.toSet()))
        );

        return findPagesId(lemmasSortedAscList, gropedLemmaIds);
    }

    private Set<Integer> findPagesId(List<String> lemmasSorted, Map<String, Set<Integer>> gropedLemmaIdMap) {
        String firstLemma = lemmasSorted.get(0);
        Set<Integer> firstLemmaIdsSet = gropedLemmaIdMap.get(firstLemma);
        Set<Integer> pagesIdResultSet = indexRepository.findPagesIdByLemmaIdIn(firstLemmaIdsSet);

        String currentLemma;
        Set<Integer> currentLemmasIdSet;
        Set<Integer> pagesIdTempSet = new HashSet<>();
        for (int i = 1; i < lemmasSorted.size(); i++) {
            pagesIdTempSet.clear();
            currentLemma = lemmasSorted.get(i);
            currentLemmasIdSet = gropedLemmaIdMap.get(currentLemma);
            pagesIdTempSet = indexRepository.findPagesIdByLemmaIdIn(currentLemmasIdSet);
            pagesIdResultSet.retainAll(pagesIdTempSet);
            if (pagesIdResultSet.isEmpty()) {
                return Collections.emptySet();
            }
        }
        return pagesIdResultSet;
    }

    private List<LemmaEntity> getSortedByFrequencyAscLemmasQueryList(String query, SiteEntity siteEntity) {
        Set<String> queryWordsSet = lemmaService.getLemmasCountMap(query).keySet();
        List<LemmaEntity> lemmaEntityList;
        if (siteEntity == null) {
            lemmaEntityList = lemmaRepository.findLemmaEntitiesByLemmaIn(queryWordsSet);
        } else {
            lemmaEntityList = lemmaRepository.findLemmaEntitiesByLemmaInAndSite(queryWordsSet, siteEntity);
        }
        lemmaEntityList.sort((l1, l2) -> l1.getFrequency() < l2.getFrequency() ? -1 : 1);
        return lemmaEntityList;
    }

    private void convertAbsoluteRelevanceToRelative(List<QueryResultPage> queryResultPageList) {
        float maxRelevanceValue = queryResultPageList.get(0).getRelevance();
        for (QueryResultPage result : queryResultPageList) {
            result.setRelevance(result.getRelevance() / maxRelevanceValue);
        }
    }

    private String getSnippet(Document document, Set<String> querySet) {
        String documentText = document.text();
        List<String> textList = new ArrayList<>(Arrays.asList(documentText.split("\\s+")));
        List<String> textListLemmatized = lemmaService.getLemmatizedList(textList);

        Map<Integer, String> textMapLemmatized =
                textListLemmatized.stream().collect(HashMap::new, (map, s) -> map.put(map.size(), s), Map::putAll);
        Map<Integer, String> filteredTextMapLemmatized = textMapLemmatized.entrySet().stream()
                .filter(e -> {
                    for (String queryWord : querySet) {
                        if (queryWord.equals(e.getValue())) {
                            return true;
                        }
                    }
                    return false;
                }).collect(HashMap::new, (map, e) -> map.put(e.getKey(), e.getValue()), Map::putAll);
        List<Integer> lemmasPositions = new ArrayList<>(filteredTextMapLemmatized.keySet());
        lemmasPositions.sort(Integer::compareTo);

        if (lemmasPositions.isEmpty()) {
            return "";
        }
        String fullSnippet = TextHelper.buildSnippet(textList, lemmasPositions, properties.getSnippetBorder());
        String[] snippetParts = fullSnippet.split("&emsp;&emsp;");
        String finalSnippet = Arrays.stream(snippetParts).max(Comparator.comparingInt(String::length)).orElse("");
        return "... ".concat(finalSnippet).concat(" ...");
    }

    private List<LemmaEntity> getFrequentLemmas(List<SiteEntity> siteEntityList, List<LemmaEntity> lemmaList) {
        Map<Integer, Float> frequentWordsBorderMap = getFrequencyLimitForEachSite(siteEntityList);
        List<LemmaEntity> frequentLemmas = new ArrayList<>();
        for (LemmaEntity lemma : new ArrayList<>(lemmaList)) {
            int siteId = lemma.getSite().getId();
            if (lemma.getFrequency() > frequentWordsBorderMap.get(siteId)) {
                frequentLemmas.add(lemma);
            }
        }
        return frequentLemmas;
    }

    private Map<Integer, Float> getFrequencyLimitForEachSite(List<SiteEntity> siteList) {
        Map<Integer, Float> frequencyMap = new HashMap<>();
        for (SiteEntity site : siteList) {
            int id = site.getId();
            float limit = pageRepository.getPageFrequencyOccurrence(properties.getPageFrequencyLimit(), id);
            frequencyMap.put(id, limit);
        }
        return frequencyMap;
    }
}
