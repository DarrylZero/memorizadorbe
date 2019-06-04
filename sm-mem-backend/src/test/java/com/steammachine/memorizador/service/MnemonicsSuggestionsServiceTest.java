package com.steammachine.memorizador.service;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.steammachine.memorizador.dto.CheckSentenceDto;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

public class MnemonicsSuggestionsServiceTest {

    private static MnemonicSuggestionsService memorySuggestionsService;

    @BeforeClass
    public static void beforeClass() throws IOException {
        memorySuggestionsService = new MnemonicSuggestionsService();
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

/* ------------------------------------ suggestNumber ------------------------------------------- */

    @Test
    public void getNumber() {
        assertEquals(new CheckSentenceDto("от топота копыт пыль по полю летит",
                        "335315352552233"),
                memorySuggestionsService.suggestNumber("от топота копыт пыль по полю летит"));
        assertEquals(
                new CheckSentenceDto("ты по что боярыню обидел, смерд", "354309279"),
                memorySuggestionsService.suggestNumber("ты по что боярыню обидел, смерд"));
    }

/*  ----------------------------------- checkSentence -----------------------------------------  */

    @Test
    public void checkSentenceMustReturnCorrectValues() {
        assertTrue(memorySuggestionsService
                .checkSentence("79212955165", "седёлка углерод пупок шприц"));

        assertTrue(memorySuggestionsService
                .checkSentence("79212955165", "И седёлку углеродом и пупок шприцем"));

        assertFalse(memorySuggestionsService
                .checkSentence("79212955165", "И седёлку с углеродом и в пупок шприцем"));
    }



}