package searchengine.services.indexing_service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.SiteEntity;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.implementations.IndexingServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
public abstract class IndexingServiceBaseTest {

    @Autowired
    protected IndexingServiceImpl indexingService;

    @MockBean
    protected SiteRepository siteRepository;

    @MockBean
    protected PageRepository pageRepository;

    @MockBean
    protected LemmaRepository lemmaRepository;

    @MockBean
    protected IndexRepository indexRepository;

    @MockBean
    protected SitesList sitesList;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    protected TransactionStatus transactionStatus;

    @BeforeEach
    void baseSetUp() throws ExecutionException, InterruptedException {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        SiteEntity mockSiteEntity = new SiteEntity();
        mockSiteEntity.setUrl("https://www.svetlovka.ru/");
        mockSiteEntity.setName("Библиотека им. М.А. Светлова");
        when(siteRepository.findSiteEntityByUrl(anyString())).thenReturn(mockSiteEntity);

        List<Site> configuredSites = new ArrayList<>();
        Site site = new Site();
        site.setUrl("https://www.svetlovka.ru/");
        site.setName("Библиотека им. М.А. Светлова");
        configuredSites.add(site);
        when(sitesList.getSites()).thenReturn(configuredSites);
    }

    @AfterEach
    void baseTearDown() {
        transactionManager.rollback(transactionStatus);
        reset(siteRepository);
    }
}