package com.steammachine.memorizador.service;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class MemorySuggestionsService {

    public List<MemorySuggestionsDTO> getSuggestions(@NonNull BigInteger number) {



        return new ArrayList<>();
    }

    public void reload() {

    }


}
