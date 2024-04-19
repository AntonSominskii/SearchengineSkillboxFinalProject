package searchengine.services.lemma_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тестирование сервиса лемматизации")
@Feature("Лемматизация текста")
public class LemmaCountingTest extends LemmaServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Проверка подсчета лемм")
    @DisplayName("Проверка корректности подсчета лемм")
    @Description("Проверка, что метод getLemmasCountMap возвращает корректный словарь лемм")
    void getLemmasCountMap() {
        Map<String, Integer> actualMap = lemmaService.getLemmasCountMap(text);
        assertEquals(expectedMap, actualMap);
    }
}