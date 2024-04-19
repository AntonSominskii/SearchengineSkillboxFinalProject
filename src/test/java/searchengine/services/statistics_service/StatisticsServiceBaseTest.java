package searchengine.services.statistics_service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import searchengine.config.SitesList;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.services.implementations.StatisticsServiceImpl;

@SpringBootTest
public abstract class StatisticsServiceBaseTest {

    @Autowired
    protected StatisticsServiceImpl statisticsService;

    @MockBean
    protected SiteRepository siteRepository;
    @MockBean
    protected PageRepository pageRepository;
    @MockBean
    protected LemmaRepository lemmaRepository;
    @MockBean
    protected SitesList sites;
}