package com.steammachine.memorizador.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.steammachine.memorizador.dto.MnemonicNumberSuggestionDTO;
import com.steammachine.memorizador.dto.MnenonicSuggestionsDTO;
import com.steammachine.memorizador.service.MnemonicSuggestionsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class MemorySuggestionsControllerTest {

    private static final String MEMORY_SUGGESTIONS_SERVISE_PATH = "/memorizador/suggestions";
    private static final String GET_SUGGESTIONS = MEMORY_SUGGESTIONS_SERVISE_PATH +
            "/suggest/{number}";
    private static final String SUGGEST_NUMBER = MEMORY_SUGGESTIONS_SERVISE_PATH +
            "/sentence";
    private static final String HEALTH = MEMORY_SUGGESTIONS_SERVISE_PATH + "/health";


    @InjectMocks
    private MnemonicsController memorySuggestionsController;

    @Mock
    private MnemonicSuggestionsService memorySuggestionsService;


    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(memorySuggestionsController).build();
    }

    /**
     * {@link MnemonicsController#health()}
     */
    @Test
    public void testHealthCheckMethodAvailibility() throws Exception {
        mockMvc.perform(
                get(HEALTH).contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * {@link MnemonicsController#suggest(String)}
     */
    @Test
    public void testSuggestMethodAvailability() throws Exception {
        when(memorySuggestionsService.getSuggestions(any(String.class)))
                .thenReturn(new MnenonicSuggestionsDTO());
        mockMvc.perform(
                get(GET_SUGGESTIONS, 3456789)
                        .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
        verify(memorySuggestionsService).getSuggestions(eq("3456789"));
    }

    /**
     * {@link MnemonicsController#suggestNumber(String)}
     */
    @Test
    public void testSuggestNumberMethodAvailability() throws Exception {
        when(memorySuggestionsService.suggestNumber(any(String.class)))
                .thenReturn(new MnemonicNumberSuggestionDTO("21"));
        mockMvc.perform(
                get(SUGGEST_NUMBER)
                        .param("sentence", "привет")
                        .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
        verify(memorySuggestionsService).suggestNumber(eq("привет"));
    }


}