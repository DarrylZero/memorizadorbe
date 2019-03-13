package com.steammachine.memorizador.controllers;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import com.steammachine.memorizador.service.MemorySuggestionsService;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController(MemorySuggestions.MEMORY_SUGGESTIONS_SERVISE_PATH)
public class MemorySuggestions {

    public static final String MEMORY_SUGGESTIONS_SERVISE_PATH = "/memorizador/suggestions";

    @Autowired
    private MemorySuggestionsService memorySuggestionsService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{number}")
    public List<MemorySuggestionsDTO> suggest(@PathVariable("number") BigInteger number) {
        return memorySuggestionsService.getSuggestions(number);
    }


}
