package com.steammachine.memorizador.service;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemorySuggestionsServiceTest {

    private static MemorySuggestionsService memorySuggestionsService;

    @BeforeClass
    public static void beforeClass() throws IOException {
        memorySuggestionsService = new MemorySuggestionsService();
        memorySuggestionsService.reloadDictianaries();
    }

    @Test
    public void getDataStreamMustWorkProperly() {
        assertEquals(asList('к', 'д', 'л', 'л', 'в', 'т'),
                memorySuggestionsService.getCharacters("192283"));
        assertEquals(asList('т', 'л', 'к', 'в', 'п', 'к', 'л', 'в', 'т', 'с', 'ш', 'п', 'к', 'л'),
                memorySuggestionsService.getCharacters("32185128376512"));
        assertEquals(asList('н', 'к', 'л', 'т', 'ч', 'п', 'ш', 'с', 'в', 'д'),
                memorySuggestionsService.getCharacters("0123456789"));
    }

}