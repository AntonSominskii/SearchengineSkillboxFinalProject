package searchengine.services.lemma_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тестирование сервиса лемматизации")
@Feature("Лемматизация текста")
public class SpecialCharactersTest extends LemmaServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка обработки специальных символов")
    @DisplayName("Подсчет лемм для текста со специальными символами")
    @Description("Проверка, что метод getLemmasCountMap корректно обрабатывает текст со специальными символами")
    void getLemmasCountMap_SpecialCharacters() {
        String textWithSpecialChars = "Привет! Как твои дела? Леопард... ещё один леопард.";
        Map<String, Integer> actualMap = lemmaService.getLemmasCountMap(textWithSpecialChars);
        assertEquals(Integer.valueOf(1), actualMap.get("привет"),
                "Ожидается правильный подсчет 'привет'");
        assertEquals(Integer.valueOf(2), actualMap.get("леопард"),
                "Ожидается правильный подсчет 'леопард'");
    }
}