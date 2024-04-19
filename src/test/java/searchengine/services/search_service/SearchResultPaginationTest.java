package searchengine.services.search_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import searchengine.dto.query.QueryResponseData;
import searchengine.model.PageEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("Тестирование сервиса поиска")
@Feature("Обработка результатов")
public class SearchResultPaginationTest extends SearchServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Пагинация результатов поиска")
    @DisplayName("Корректная пагинация результатов поиска")
    @Description("Проверка корректной пагинации результатов поиска")
    void testCorrectPagination() {
        when(pageRepository.findAllById(any())).thenReturn(mockPages());
        ResponseEntity<QueryResponseData> responseNoPages = searchService.search("example", "https://www.svetlovka.ru/", 16, 5);
        assertEquals(0, responseNoPages.getBody().getData().size(), "Should return zero when offset is beyond page count");
    }

    private List<PageEntity> mockPages() {
        List<PageEntity> pages = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PageEntity page = new PageEntity();
            page.setId(i);
            page.setPath("Page " + i);
            page.setContent("Content for page " + i);
            pages.add(page);
        }
        return pages;
    }
}