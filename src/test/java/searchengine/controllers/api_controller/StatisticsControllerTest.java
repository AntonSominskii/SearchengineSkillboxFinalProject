package searchengine.controllers.api_controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import searchengine.dto.query.StatisticsResponse;
import searchengine.dto.statistics.StatisticsData;

import io.qameta.allure.*;

public class StatisticsControllerTest extends ApiControllerBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Получение статистики")
    @DisplayName("Получение статистических данных")
    @Description("Проверка, что API возвращает корректные статистические данные")
    public void testStatistics() throws Exception {
        StatisticsData statisticsData = new StatisticsData();
        StatisticsResponse mockResponse = new StatisticsResponse();
        mockResponse.setResult(true);
        mockResponse.setStatistics(statisticsData);
        ResponseEntity<StatisticsResponse> responseEntity = ResponseEntity.ok(mockResponse);
        when(statisticsService.getStatistics()).thenReturn(responseEntity.getBody());

        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }
}