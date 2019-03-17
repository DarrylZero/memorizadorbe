package com.steammachine.memorizador.service;

import com.steammachine.common.utils.commonutils.CommonUtils;
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
                .getSuggestions(BigInteger.valueOf(2128506));
    }

    @Test
    public void doReloadAndGetSuggestions2() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MemorySuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions(BigInteger.valueOf(79213955166L));
    }

    @Test
    public void doReloadAndGetSuggestions30() throws IOException {
        MemorySuggestionsService memorySuggestionsService = new MemorySuggestionsService();
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            memorySuggestionsService.doReload(stream);
        }
        MemorySuggestionsDTO suggestions = memorySuggestionsService
                .getSuggestions(new BigInteger("524241018154723"));


    }


}