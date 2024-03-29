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
public class CheckSentenceDto {

    @JsonProperty("sentence")
    private String sentence;

    @JsonProperty("number")
    private String number;

}
