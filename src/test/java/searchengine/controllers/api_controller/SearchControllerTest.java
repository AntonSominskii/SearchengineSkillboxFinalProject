package searchengine.controllers.api_controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import searchengine.dto.query.QueryResponseData;
import searchengine.dto.query.QueryResultPage;

import java.util.Collections;
import java.util.List;

import io.qameta.allure.*;

public class SearchControllerTest extends ApiControllerBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Поиск данных")
    @DisplayName("Выполнение поиска по запросу")
    @Description("Проверка, что API корректно обрабатывает поисковые запросы и возвращает ожидаемые результаты")
    public void testSearch() throws Exception {
        QueryResultPage page = new QueryResultPage("testsite.com", "Test Site", "/test", "Test Title", "Snippet", 0.9f);

        List<QueryResultPage> pages = Collections.singletonList(page);

        QueryResponseData mockResponse = new QueryResponseData();
        mockResponse.setResult(true);
        mockResponse.setCount(1);
        mockResponse.setData(pages);

        ResponseEntity<QueryResponseData> responseEntity = ResponseEntity.ok(mockResponse);
        when(searchService.search(anyString(), any(), anyInt(), anyInt())).thenReturn(responseEntity);

        mockMvc.perform(get("/api/search")
                        .param("query", "test")
                        .param("site", "")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Title"));
    }
}