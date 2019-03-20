package com.steammachine.memorizador.controllers;

import com.steammachine.memorizador.dto.MnenonicSuggestionsDTO;
import com.steammachine.memorizador.service.MnemonicSuggestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MnemonicsController.MEMORY_SUGGESTIONS_SERVISE_PATH)
public class MnemonicsController {

    static final String MEMORY_SUGGESTIONS_SERVISE_PATH = "/memorizador/suggestions";

    @Autowired
    private MnemonicSuggestionsService memorySuggestionsService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/suggest/{number}")
    MnenonicSuggestionsDTO suggest(@PathVariable("number") String number) {
        return memorySuggestionsService.getSuggestions(number);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/health")
    String health() {
        return MnemonicsController.class.getName() +  " works just fine";
    }


}
