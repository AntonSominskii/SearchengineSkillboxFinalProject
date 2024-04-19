package searchengine.services.statistics_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование сервиса статистики")
@Feature("Обработка ошибок базы данных")
public class StatisticsServiceDatabaseErrorTest extends StatisticsServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Обработка ошибок базы данных")
    @DisplayName("Проверка поведения сервиса при ошибке базы данных")
    @Description("Тест проверяет поведение сервиса при возникновении ошибок доступа к базе данных.")
    void testGetStatisticsWithDatabaseError() {
        when(siteRepository.findAll()).thenThrow(new RuntimeException("Database is down"));

        Exception exception = assertThrows(RuntimeException.class, () -> statisticsService.getStatistics());
        assertEquals("Database is down", exception.getMessage());
    }
}