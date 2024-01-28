package searchengine.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.exceptions.PageException;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.model.Status;
import searchengine.services.implementations.IndexingServiceImpl;
import searchengine.utilities.TextHelper;
import searchengine.utilities.WebScraperHelper;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
@Log4j2
public class WebPageHarvester extends RecursiveAction {

    private final transient IndexingServiceImpl service;
    private final transient SiteEntity siteEntity;
    private final String pagePath;

    @SneakyThrows
    @Override
    protected void compute() {
        log.info("NEW WebPageHarvester created for pagePath: {}", pagePath);
        try {
            Thread.sleep(500);
            handlePageData();
        } catch (UnsupportedMimeTypeException | ConnectException | PageException ignoredException) {
            log.warn("Exception '{}' ignored in WebPageHarvester while handling path: {}", ignoredException, pagePath);
        } catch (Exception exception) {
            log.warn("Exception '{}' in WebPageHarvester while handling path: {}. " +
                    "Indexing for site '{}' completed with error", exception, pagePath, siteEntity.getUrl());
            service.getSiteStatusMap().put(siteEntity.getUrl(), Status.FAILED);
            throw exception;
        }
    }

    private void handlePageData() throws IOException {
        List<WebPageHarvester> forkJoinPoolPagesList = new ArrayList<>();
        String userAgent = service.getProperties().getUseragent();
        String referrer = service.getProperties().getReferrer();
        Connection connection = WebScraperHelper.getConnection(pagePath, userAgent, referrer);
        int httpStatusCode = connection.execute().statusCode();
        if (httpStatusCode != 200) {
            connection = WebScraperHelper.getConnection(TextHelper.cutSlash(pagePath), userAgent, referrer);
            httpStatusCode = connection.execute().statusCode();
        }

        String pathToSave = TextHelper.cutProtocolAndHost(pagePath, siteEntity.getUrl());
        String html = "";
        PageEntity pageEntity = new PageEntity(pathToSave, httpStatusCode, html, siteEntity);
        if (httpStatusCode != 200) {
            service.savePageContentAndSiteStatusTime(pageEntity, html, siteEntity);
        } else {
            Document document = connection.get();
            html = document.outerHtml();
            service.savePageContentAndSiteStatusTime(pageEntity, html, siteEntity);
            service.extractLemmasAndIndexFromHtml(html, pageEntity, siteEntity);
            Elements anchors = document.select("body").select("a");
            handleAnchors(anchors, forkJoinPoolPagesList);
        }
        for (WebPageHarvester webPageHarvester : forkJoinPoolPagesList) {
            webPageHarvester.join();
        }
    }

    private void handleAnchors(Elements elements, List<WebPageHarvester> fjpList) {
        String fileExtensions = service.getProperties().getFileExtensions();
        for (Element anchor : elements) {
            String href = TextHelper.getHrefFromAnchor(anchor);
            if (TextHelper.isHrefValid(siteEntity.getUrl(), href, fileExtensions)
                    && !TextHelper.isPageAdded(service.getWebpagesPathSet(), href)) {
                service.getWebpagesPathSet().add(href);
                if (!service.getSiteStatusMap().get(siteEntity.getUrl()).equals(Status.INDEXING)) {
                    return;
                }
                WebPageHarvester webPageHarvester = new WebPageHarvester(service, siteEntity, href);
                fjpList.add(webPageHarvester);
                webPageHarvester.fork();
            }
        }
    }
}