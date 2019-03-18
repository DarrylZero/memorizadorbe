package com.steammachine.memorizador.service;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

public class MemorySuggestionsServiceReloadAndSearchTest {

    @Test
    public void getReloadTest() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        memorySuggestionsService.reloadDictianaries();
    }

    @Test
    public void doReload() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = MemorySuggestionsServiceReloadAndSearchTest.class
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
    }


    @Test
    public void doReloadAndGetSuggestions() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MemorySuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("2128506");
    }

    @Test
    public void doReloadAndGetSuggestions2() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MemorySuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("79213955166");


    }

    @Test
    public void doReloadAndGetSuggestions30() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MemorySuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("524241018154723");
    }

    @Test
    public void doReloadMustNotGiveManySameWords() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/test_dict1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MemorySuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions("5222");

        assertEquals(singletonList(singletonList("параллелограмм")), suggestions.getItems());
    }


}