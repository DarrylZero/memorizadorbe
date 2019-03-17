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
public class MemorySuggestionsDTO {

    @JsonProperty("items")
    private List<List<String>> items = new ArrayList<>();



}
