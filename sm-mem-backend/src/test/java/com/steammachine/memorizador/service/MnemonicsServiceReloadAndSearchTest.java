package com.steammachine.memorizador.service;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import com.steammachine.memorizador.dto.MnemonicSuggestionDTO;
import com.steammachine.memorizador.dto.MnenonicSuggestionsDTO;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class MnemonicsServiceReloadAndSearchTest {

    @Test
    public void getReloadTest() throws IOException {
        MnemonicSuggestionsService memorySuggestionsService = new MnemonicSuggestionsService();
        memorySuggestionsService.reloadDictianaries();
    }

    @Test
    public void doReload() throws IOException {
        MnemonicSuggestionsService memorySuggestionsService = new MnemonicSuggestionsService();
        try (InputStream stream = MnemonicsServiceReloadAndSearchTest.class
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
    }


    @Test
    public void doReloadAndGetSuggestions() throws IOException {
        MnemonicSuggestionsService memorySuggestionsService = new MnemonicSuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MnenonicSuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("2128506");
    }

    @Test
    public void doReloadAndGetSuggestions2() throws IOException {
        MnemonicSuggestionsService memorySuggestionsService = new MnemonicSuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MnenonicSuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("79213955166");


    }

    @Test
    public void doReloadAndGetSuggestions30() throws IOException {
        MnemonicSuggestionsService memorySuggestionsService = new MnemonicSuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MnenonicSuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("524241018154723");
    }

    @Test
    public void doReloadMustNotGiveManySameWords() throws IOException {
        MnemonicSuggestionsService memorySuggestionsService = new MnemonicSuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/test_dict1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MnenonicSuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("5222");

        assertEquals("5222", suggestions.getNumber());
        assertEquals(new MnenonicSuggestionsDTO("5222",
                        asList(
                                new MnemonicSuggestionDTO(singletonList(singletonList("параллелограмм"))),
                                new MnemonicSuggestionDTO(singletonList(singletonList("параллелограмм"))))),
                suggestions);
    }


}