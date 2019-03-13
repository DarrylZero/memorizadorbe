package com.steammachine.memorizador.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class MemorySuggestionsDTO {

    private final List<String> items = new ArrayList<>();


}
