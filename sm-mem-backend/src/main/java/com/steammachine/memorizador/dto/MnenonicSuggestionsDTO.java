package com.steammachine.memorizador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice.AllArguments;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MnenonicSuggestionsDTO {

    @JsonProperty("number")
    private String number;


    @JsonProperty("suggestions")
    private List<MnemonicSuggestionDTO> suggestions = new ArrayList<>();

}
