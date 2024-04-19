package searchengine.services.indexing_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import searchengine.dto.query.ActionResponse;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование сервиса индексации")
@Feature("Индексация страниц")
public class IndexingServicePageIndexTest extends IndexingServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Индексация страницы")
    @DisplayName("Index Page When Page Belongs to Configured Site")
    @Description("Проверка корректной индексации страницы, принадлежащей настроенному сайту")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void indexPage_WhenPageBelongsToConfiguredSite_ShouldIndexPage() {
        String pagePath = "https://www.svetlovka.ru/";
        ResponseEntity<ActionResponse> response = indexingService.indexPage(pagePath);
        assertTrue(response.getBody().isResult(), "Ожидается успешный результат индексации");
        assertNull(response.getBody().getError(), "Ожидается, что ошибки нет");
    }
}