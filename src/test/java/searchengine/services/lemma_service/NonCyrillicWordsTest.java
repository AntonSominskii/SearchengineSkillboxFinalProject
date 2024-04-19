package searchengine.services.lemma_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тестирование сервиса лемматизации")
@Feature("Лемматизация текста")
public class NonCyrillicWordsTest extends LemmaServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка обработки некириллических слов")
    @DisplayName("Подсчет лемм для текста с некириллическими словами")
    @Description("Проверка, что метод getLemmasCountMap игнорирует некириллические слова")
    void getLemmasCountMap_NonCyrillicWords() {
        String mixedScriptText = "2021 год был годом леопарда, leopard.";
        Map<String, Integer> actualMap = lemmaService.getLemmasCountMap(mixedScriptText);
        assertEquals(Integer.valueOf(2), actualMap.get("год"),
                "Ожидается, что 'год' будет правильно посчитан, игнорируя слова на некириллическом алфавите");
    }
}