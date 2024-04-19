package searchengine.controllers.api_controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import searchengine.controllers.ApiController;
import searchengine.services.interfaces.IndexingService;
import searchengine.services.interfaces.SearchService;
import searchengine.services.interfaces.StatisticsService;

@WebMvcTest(ApiController.class)
@ExtendWith(MockitoExtension.class)
public abstract class ApiControllerBaseTest {

    protected MockMvc mockMvc;

    @MockBean
    protected StatisticsService statisticsService;

    @MockBean
    protected IndexingService indexingService;

    @MockBean
    protected SearchService searchService;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}