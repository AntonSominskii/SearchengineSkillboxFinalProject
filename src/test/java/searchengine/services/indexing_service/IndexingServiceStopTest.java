package searchengine.services.indexing_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import searchengine.dto.query.ActionResponse;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование сервиса индексации")
@Feature("Управление индексацией")
public class IndexingServiceStopTest extends IndexingServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Остановка индексации")
    @DisplayName("Stop Indexing When Currently Indexing")
    @Description("Проверка корректной остановки активной индексации")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void stopIndexing_WhenCurrentlyIndexing_ShouldStopIndexing() {
        indexingService.startIndexing();
        ResponseEntity<ActionResponse> response = indexingService.stopIndexing();
        assertTrue(response.getBody().isResult());
        assertNull(response.getBody().getError(), "Ожидается, что ошибки нет при успешной остановке индексации");
    }
}