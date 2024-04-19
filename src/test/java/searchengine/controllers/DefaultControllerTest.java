package searchengine.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Description;

@WebMvcTest(DefaultController.class)
@Epic("Тестирование контроллеров")
@Feature("Тестирование DefaultController")
public class DefaultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка главной страницы")
    @Description("Тест проверяет, что главная страница корректно отображается и возвращает правильное представление 'index'.")
    public void testIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}