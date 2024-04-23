package searchengine.services.indexing_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import searchengine.dto.query.ActionResponse;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование сервиса индексации")
@Feature("Множественная индексация")
public class IndexingServiceConcurrentIndexingTest extends IndexingServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Множественная индексация")
    @DisplayName("Start Indexing When Already Indexing Should Not Start Again")
    @Description("Проверка корректности обработки попыток начать индексацию, когда одна уже выполняется")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void startIndexing_WhenAlreadyIndexing_ShouldNotStartAgain() {
        indexingService.startIndexing();

        ResponseEntity<ActionResponse> response = indexingService.startIndexing();

        assertFalse(response.getBody().isResult());
        assertEquals("Indexing already started", response.getBody().getError());
    }
}