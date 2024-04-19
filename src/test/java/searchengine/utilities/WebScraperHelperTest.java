package searchengine.utilities;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import io.qameta.allure.*;

@Epic("Тестирование утилит веб-скрапинга")
@Feature("Утилиты веб-скрапера")
public class WebScraperHelperTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение соединения")
    @DisplayName("Создание соединения")
    @Description("Проверка, что метод getConnection создает соединение с заданными параметрами User-Agent")
    public void testGetConnection() {
        Connection connection = WebScraperHelper.getConnection("http://example.com", "Mozilla/5.0", "http://google.com");
        assertNotNull(connection);
        assertEquals("Mozilla/5.0", connection.request().header("User-Agent"));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Парсинг HTML")
    @DisplayName("Преобразование HTML в Document")
    @Description("Проверка, что метод parse корректно преобразует строку HTML в объект Document")
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body>Hello World!</body></html>";
        Document doc = WebScraperHelper.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение Document из Connection")
    @DisplayName("Получение Document из соединения")
    @Description("Проверка, что метод getDocument корректно извлекает объект Document из соединения")
    public void testGetDocument() throws Exception {
        Connection connection = WebScraperHelper.getConnection("http://example.com", "Mozilla/5.0", "http://google.com");
        Connection.Response response = connection.execute(); // This would require a live connection or a mock
        Document doc = WebScraperHelper.getDocument(connection);
        assertNotNull(doc);
    }
}