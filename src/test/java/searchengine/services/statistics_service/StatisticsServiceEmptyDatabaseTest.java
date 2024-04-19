package searchengine.services.statistics_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import searchengine.dto.query.StatisticsResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

@Epic("Тестирование сервиса статистики")
@Feature("Получение статистики")
public class StatisticsServiceEmptyDatabaseTest extends StatisticsServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Обработка пустой базы данных")
    @DisplayName("Проверка сервиса на пустой базе данных")
    @Description("Тест проверяет поведение сервиса, когда в базе данных нет сайтов.")
    void testGetStatisticsWithEmptyDatabase() {
        when(siteRepository.findAll()).thenReturn(Collections.emptyList());

        StatisticsResponse response = statisticsService.getStatistics();

        assertNotNull(response);
        assertEquals(0, response.getStatistics().getTotal().getSites());
        assertEquals(0, response.getStatistics().getTotal().getPages());
        assertEquals(0, response.getStatistics().getTotal().getLemmas());
        assertTrue(response.getStatistics().getDetailed().isEmpty());
    }
}