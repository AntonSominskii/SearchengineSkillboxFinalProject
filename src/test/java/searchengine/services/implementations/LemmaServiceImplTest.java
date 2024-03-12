package searchengine.services.implementations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import searchengine.services.interfaces.LemmaService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LemmaServiceImplTest {

    private final LemmaService lemmaService;
    private String text;
    private Map<String, Integer> expectedMap;

    @Autowired
    public LemmaServiceImplTest(LemmaService lemmaService) {
        this.lemmaService = lemmaService;
    }

    @BeforeEach
    void setUp() {
        text = "Повторное появление леопарда в Осетии позволяет предположить, " +
                "что леопард постоянно обитает в некоторых районах Северного Кавказа.";
        expectedMap = new HashMap<>();
        expectedMap.put("повторный", 1);
        expectedMap.put("некоторый", 1);
        expectedMap.put("появление", 1);
        expectedMap.put("постоянно", 1);
        expectedMap.put("постоянный", 1);
        expectedMap.put("некоторые", 1);
        expectedMap.put("позволять", 1);
        expectedMap.put("предположить", 1);
        expectedMap.put("северный", 1);
        expectedMap.put("район", 1);
        expectedMap.put("кавказ", 1);
        expectedMap.put("осетия", 1);
        expectedMap.put("леопард", 2);
        expectedMap.put("обитать", 1);
    }

    @AfterEach
    void tearDown() {
        text = null;
        expectedMap = null;
    }

    @Test
    void getLemmasCountMap() {
        Map<String, Integer> actualMap = lemmaService.getLemmasCountMap(text);
        assertEquals(expectedMap, actualMap);

        System.out.println("Ожидаемый результат: " + expectedMap);
        System.out.println("Фактический результат: " + actualMap);
    }
}