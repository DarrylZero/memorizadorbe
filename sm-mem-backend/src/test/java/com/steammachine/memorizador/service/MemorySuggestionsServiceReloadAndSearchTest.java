package com.steammachine.memorizador.service;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import org.junit.Test;

public class MemorySuggestionsServiceReloadAndSearchTest {

    @Test
    public void getReloadTest() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        memorySuggestionsService.reload();
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
        List<MemorySuggestionsDTO> suggestions = memorySuggestionsService
                .getSuggestions(BigInteger.valueOf(2128506));

        suggestions.clear();
    }


}