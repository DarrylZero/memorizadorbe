package com.steammachine.memorizador.controllers;

import com.steammachine.memorizador.dto.CheckSentenceDto;
import com.steammachine.memorizador.dto.CheckSentenceResultDto;
import com.steammachine.memorizador.dto.MnemonicNumberSuggestionParam;
import com.steammachine.memorizador.dto.MnenonicSuggestionsDto;
import com.steammachine.memorizador.service.MnemonicSuggestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MnemonicsController.MEMORY_SUGGESTIONS_SERVISE_PATH)
@CrossOrigin
public class MnemonicsController {

    static final String MEMORY_SUGGESTIONS_SERVISE_PATH = "/memorizador/suggestions";

    @Autowired
    private MnemonicSuggestionsService memorySuggestionsService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/health")
    private String health() {
        return MnemonicsController.class.getName() + " works just fine";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/suggest/{number}")
    MnenonicSuggestionsDto suggest(@PathVariable("number") String number) {
        return memorySuggestionsService.getSuggestions(number);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sentence")
    CheckSentenceDto suggestNumber(@RequestParam("sentence") String number) {
        return memorySuggestionsService.suggestNumber(number);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sentence")
    CheckSentenceDto suggestNumber(
            @RequestBody MnemonicNumberSuggestionParam number) {
        return memorySuggestionsService.suggestNumber(number.getSentence());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/check")
    CheckSentenceResultDto checkSentence(@RequestBody CheckSentenceDto checkSentenceDto) {
        return new CheckSentenceResultDto(
                memorySuggestionsService.checkSentence(checkSentenceDto.getNumber(),
                checkSentenceDto.getSentence())) ;
    }
}
