package com.steammachine.memorizador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MnenonicSuggestionsDto {

    @JsonProperty("number")
    private String number;


    @JsonProperty("suggestions")
    private List<MnemonicSuggestionDto> suggestions = new ArrayList<>();

}
