package com.steammachine.memorizador.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import com.steammachine.memorizador.service.MemorySuggestionsService;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class MemorySuggestionsControllerTest {

    private static final String MEMORY_SUGGESTIONS_SERVISE_PATH = "/memorizador/suggestions";
    private static final String GET_SUGGESTIONS = MEMORY_SUGGESTIONS_SERVISE_PATH +
            "/suggest/{number}";
    private static final String HEALTH = MEMORY_SUGGESTIONS_SERVISE_PATH + "/health";


    @InjectMocks
    private MemorySuggestionsController memorySuggestionsController;

    @Mock
    private MemorySuggestionsService memorySuggestionsService;


    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(memorySuggestionsController).build();
    }

    /**
     * {@link MemorySuggestionsController#health()}
     */
    @Test
    public void testHealthCheckMethodAvailibility() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(HEALTH).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * {@link MemorySuggestionsController#suggest(String)}
     */
    @Test
    public void testSuggestMethodAvailability() throws Exception {
        when(memorySuggestionsService.getSuggestions(any(String.class)))
                .thenReturn(new MemorySuggestionsDTO());
        mockMvc.perform(
                MockMvcRequestBuilders.get(GET_SUGGESTIONS, 3456789)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
        verify(memorySuggestionsService).getSuggestions(eq("3456789"));
    }


}