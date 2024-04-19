package searchengine.services.search_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import searchengine.dto.query.QueryResponseData;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование сервиса поиска")
@Feature("Валидация запроса")
public class EmptyQueryTest extends SearchServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Обработка пустого запроса")
    @DisplayName("Пустой запрос должен возвращать ошибку")
    @Description("Проверка, что сервис возвращает ошибку, если запрос пустой")
    void testEmptyQueryReturnsError() {
        ResponseEntity<QueryResponseData> response = searchService.search("", "https://www.svetlovka.ru/", 0, 10);
        assertFalse(response.getBody().isResult());
        assertEquals("Empty query query", response.getBody().getError(), "Ожидается сообщение об ошибке для пустого запроса");
    }
}