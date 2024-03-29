package com.steammachine.memorizador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MnemonicSuggestionDto {

    @JsonProperty("suggestion")
    private List<List<String>> suggestion = new ArrayList<>();



}
