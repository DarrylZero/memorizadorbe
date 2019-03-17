package com.steammachine.memorizador.service;

import org.junit.BeforeClass;
import org.junit.Test;

public class MemorySuggestionsServiceTest {

    private static MemorySuggestionsService memorySuggestionsService;

    @BeforeClass
    public static void beforeClass() {
        memorySuggestionsService = new MemorySuggestionsService();
        memorySuggestionsService.reload();

    }

    @Test
    public void getSuggestions() {


    }
}