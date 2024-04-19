package searchengine.services.indexing_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import searchengine.dto.query.ActionResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Тестирование сервиса индексации")
@Feature("Обработка недопустимых URL")
public class IndexingServiceInvalidUrlTest extends IndexingServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Обработка недопустимых URL")
    @DisplayName("Index Page With Invalid URL Should Return Error")
    @Description("Проверка корректной обработки недопустимого URL при индексации страницы")
    void indexPage_WithInvalidUrl_ShouldReturnError() {
        String invalidUrl = "https://thisisinvalidurl.com/";
        when(siteRepository.findSiteEntityByUrl(invalidUrl)).thenReturn(null);

        ResponseEntity<ActionResponse> response = indexingService.indexPage(invalidUrl);

        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getError());
        assertEquals("Page is located outside the sites specified in the configuration file", response.getBody().getError());
    }
}