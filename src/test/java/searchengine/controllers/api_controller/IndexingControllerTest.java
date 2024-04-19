package searchengine.controllers.api_controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import searchengine.dto.query.ActionResponse;

import io.qameta.allure.*;

public class IndexingControllerTest extends ApiControllerBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Запуск индексации")
    @DisplayName("Запуск процесса индексации")
    @Description("Проверка, что API корректно обрабатывает запрос на запуск индексации")
    public void testStartIndexing() throws Exception {
        ActionResponse mockResponse = new ActionResponse();
        mockResponse.setResult(true);
        ResponseEntity<ActionResponse> responseEntity = ResponseEntity.ok(mockResponse);
        when(indexingService.startIndexing()).thenReturn(responseEntity);

        mockMvc.perform(get("/api/startIndexing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Остановка индексации")
    @DisplayName("Остановка процесса индексации")
    @Description("Проверка, что API корректно обрабатывает запрос на остановку индексации")
    public void testStopIndexing() throws Exception {
        ActionResponse mockResponse = new ActionResponse();
        mockResponse.setResult(true);
        ResponseEntity<ActionResponse> responseEntity = ResponseEntity.ok(mockResponse);
        when(indexingService.stopIndexing()).thenReturn(responseEntity);

        mockMvc.perform(get("/api/stopIndexing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }
}