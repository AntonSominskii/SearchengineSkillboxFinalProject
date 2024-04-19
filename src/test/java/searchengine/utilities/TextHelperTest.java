package searchengine.utilities;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.parser.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import searchengine.exceptions.PageException;
import org.jsoup.nodes.Element;

import java.util.HashSet;

import io.qameta.allure.*;

@Epic("Тестирование вспомогательных функций")
@Feature("Вспомогательные функции для текста")
public class TextHelperTest {

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Обрезка конечного слэша в URL")
    @DisplayName("Удаление конечного слеша из URL")
    @Description("Проверка, что метод cutSlash корректно удаляет конечный слеш из URL")
    public void testCutSlash() {
        Assertions.assertEquals("http://example.com", TextHelper.cutSlash("http://example.com/"));
        assertEquals("http://example.com/dir", TextHelper.cutSlash("http://example.com/dir/"));
        assertEquals("", TextHelper.cutSlash("/"));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение стартовой страницы из URL")
    @DisplayName("Получение корня сайта из URL")
    @Description("Проверка, что метод getStartPage корректно извлекает стартовую страницу из URL")
    public void testGetStartPage() {
        assertEquals("http://example.com/", TextHelper.getStartPage("http://example.com/page"));
        assertEquals("https://example.com/", TextHelper.getStartPage("https://www.example.com/page"));
        assertEquals("https://example.com/", TextHelper.getStartPage("https://www.example.com"));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Обработка исключения для некорректного URL")
    @DisplayName("Генерация исключения для неправильного URL")
    @Description("Проверка, что метод getStartPage генерирует исключение PageException при неправильном URL")
    public void testGetStartPageWithInvalidUrl() {
        assertThrows(PageException.class, () -> {
            TextHelper.getStartPage("htp:/wrongurl.com");
        });
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.MINOR)
    @Story("Проверка наличия строки")
    @DisplayName("Проверка существования строки")
    @Description("Проверка, что метод isStringExists верно определяет наличие непустой строки")
    public void testIsStringExists() {
        assertTrue(TextHelper.isStringExists("hello"));
        assertFalse(TextHelper.isStringExists(""));
        assertFalse(TextHelper.isStringExists(" "));
        assertFalse(TextHelper.isStringExists(null));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка валидности href")
    @DisplayName("Валидация href")
    @Description("Проверка, что метод isHrefValid корректно определяет валидность href по заданным параметрам")
    public void testIsHrefValid() {
        assertTrue(TextHelper.isHrefValid("http://example.com/", "http://example.com/page", "pdf|jpg"));
        assertFalse(TextHelper.isHrefValid("http://example.com/", "http://example.com", "pdf|jpg"));
        assertFalse(TextHelper.isHrefValid("http://example.com/", "http://example.com/page.pdf", "pdf|jpg"));
        assertFalse(TextHelper.isHrefValid("http://example.com/", "http://malicious.com", "pdf|jpg"));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка добавления страницы")
    @DisplayName("Проверка наличия добавленной страницы")
    @Description("Проверка, что метод isPageAdded верно определяет, добавлена ли страница в набор")
    public void testIsPageAdded() {
        HashSet<String> pages = new HashSet<>();
        pages.add("http://example.com/page/");
        assertTrue(TextHelper.isPageAdded(pages, "http://example.com/page"));
        assertFalse(TextHelper.isPageAdded(pages, "http://example.com/otherpage"));
    }

    @Test
    @Owner("Соминский Антон")
    @Severity(SeverityLevel.MINOR)
    @Story("Извлечение href из элемента")
    @DisplayName("Извлечение href из тега привязки")
    @Description("Проверка, что метод getHrefFromAnchor корректно извлекает и обрабатывает href из элемента anchor")
    public void testGetHrefFromAnchor() {
        Element anchor = new Element(Tag.valueOf("a"), "").attr("href", "http://example.com/page ");
        assertEquals("http://example.com/page/", TextHelper.getHrefFromAnchor(anchor));
    }
}