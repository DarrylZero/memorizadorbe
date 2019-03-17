package com.steammachine.memorizador.service;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemorySuggestionsServiceTest {

    private static MemorySuggestionsService memorySuggestionsService;

    @BeforeClass
    public static void beforeClass() throws IOException {
        memorySuggestionsService = new MemorySuggestionsService();
        memorySuggestionsService.reload();
    }

    @Test
    public void getSuggestions() {


    }

    @Test
    public void getDataStreamMustWorkProperly() {
        assertEquals(asList('к','д','л','л','в','т'),
                memorySuggestionsService.getCharacters(BigInteger.valueOf(192283)));
    }




}