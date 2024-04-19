package searchengine.services.search_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import searchengine.dto.query.QueryResponseData;
import searchengine.model.Status;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Тестирование сервиса поиска")
@Feature("Проверка статуса индексации")
public class IndexingStatusTest extends SearchServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Реакция на незавершенную индексацию")
    @DisplayName("Проверка статуса индексации при поиске")
    @Description("Сервис должен сообщать, если индексация не завершена")
    void testIndexingInProgressError() {
        when(siteRepository.existsByStatus(Status.INDEXING)).thenReturn(true);
        ResponseEntity<QueryResponseData> response = searchService.search("example", "https://www.svetlovka.ru/", 0, 10);
        assertFalse(response.getBody().isResult());
        assertEquals("Indexing not finished yet successfully", response.getBody().getError(), "Ожидается сообщение о незавершенной индексации");
    }
}