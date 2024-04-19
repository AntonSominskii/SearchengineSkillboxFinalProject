package searchengine.services.lemma_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Epic("Тестирование сервиса лемматизации")
@Feature("Лемматизация текста")
public class NullInputTest extends LemmaServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Проверка обработки null")
    @DisplayName("Проверка обработки null в качестве входных данных")
    @Description("Проверка, что метод getLemmasCountMap не выбрасывает исключение при передаче null")
    void getLemmasCountMap_NullInput() {
        assertDoesNotThrow(() -> lemmaService.getLemmasCountMap(null),
                "Метод не должен выбрасывать исключение при передаче null в качестве входных данных.");
    }
}