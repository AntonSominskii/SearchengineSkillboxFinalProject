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
public class IndexingServiceStartTest extends IndexingServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Запуск индексации")
    @DisplayName("Start Indexing When Not Currently Indexing")
    @Description("Проверка корректного запуска индексации, когда индексация не активна")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void startIndexing_WhenNotCurrentlyIndexing_ShouldStartIndexing() {
        ResponseEntity<ActionResponse> response = indexingService.startIndexing();
        assertTrue(response.getBody().isResult());
        assertNull(response.getBody().getError(), "Ожидается, что ошибки нет при успешном запуске индексации");
    }
}