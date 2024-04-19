package searchengine.services.lemma_service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тестирование сервиса лемматизации")
@Feature("Лемматизация текста")
public class CaseInsensitivityTest extends LemmaServiceBaseTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка учета регистра букв")
    @DisplayName("Учет регистра букв при подсчете лемм")
    @Description("Проверка, что метод getLemmasCountMap не учитывает регистр букв")
    void getLemmasCountMap_CaseInsensitivity() {
        String mixedCaseText = "Леопард и ЛЕОПАРД должны считаться одним и тем же словом.";
        Map<String, Integer> actualMap = lemmaService.getLemmasCountMap(mixedCaseText);
        assertEquals(Integer.valueOf(2), actualMap.getOrDefault("леопард", 0),
                "Ожидается, что леммы не будут учитывать регистр букв, и их подсчет будет объединен");
    }
}