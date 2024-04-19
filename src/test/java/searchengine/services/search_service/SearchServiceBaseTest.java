package searchengine.services.search_service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import searchengine.repository.*;
import searchengine.services.implementations.SearchServiceImpl;
import searchengine.services.interfaces.LemmaService;

@SpringBootTest
public abstract class SearchServiceBaseTest {

    @Autowired
    protected SearchServiceImpl searchService;

    @MockBean
    protected LemmaService lemmaService;
    @MockBean
    protected SiteRepository siteRepository;
    @MockBean
    protected PageRepository pageRepository;
    @MockBean
    protected LemmaRepository lemmaRepository;
    @MockBean
    protected IndexRepository indexRepository;
}