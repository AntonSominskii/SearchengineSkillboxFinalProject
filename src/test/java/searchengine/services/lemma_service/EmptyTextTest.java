package searchengine.services.lemma_service;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Тестирование сервиса лемматизации")
@Feature("Лемматизация текста")
public class EmptyTextTest extends LemmaServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.MINOR)
    @Story("Проверка работы с пустым текстом")
    @DisplayName("Подсчет лемм для пустого текста")
    @Description("Проверка, что метод getLemmasCountMap возвращает пустой словарь для пустого текста")
    void getLemmasCountMap_EmptyText() {
        String emptyText = "";
        Map<String, Integer> actualMap = lemmaService.getLemmasCountMap(emptyText);
        assertTrue(actualMap.isEmpty(), "Ожидается пустой словарь для пустого входного текста");
    }
}