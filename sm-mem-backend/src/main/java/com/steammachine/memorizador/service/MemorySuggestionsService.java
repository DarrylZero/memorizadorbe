package com.steammachine.memorizador.service;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class MemorySuggestionsService {

    private static final int MIN_SYMBOLS = 2;
    private static final int AVARAGE_SYMBOLS = 4;

    public static class Word {

        private final String word;
        private volatile List<Character> characters;

        private Word(@NonNull String word) {
            this.word = word;
        }

        public List<Character> significantChars() {
            if (characters == null) {
                synchronized (this) {
                    if (characters == null) {
                        characters = doGetSignificantCharacters(word);
                    }
                }
            }
            return characters;
        }

        public String word() {
            return word;
        }

        /*  --------------------------------- privates  --------------------------------------- */

        public static List<Character> doGetSignificantCharacters(@NonNull String word) {
            return Stream.iterate(0, i -> i + 1).limit(word.length())
                    .map(word::charAt)
                    .filter(NUMBER_MAP::containsValue)
                    .collect(toList());
        }

    }

    private static final Map<Character, Character> NUMBER_MAP;

    static {
        HashMap<Character, Character> m = new HashMap<>();
        m.put('0', 'н');
        m.put('1', 'к');
        m.put('2', 'л');
        m.put('3', 'т');
        m.put('4', 'ч');
        m.put('5', 'п');
        m.put('6', 'ш');
        m.put('7', 'с');
        m.put('8', 'в');
        m.put('9', 'д');
        NUMBER_MAP = unmodifiableMap(m);
    }

    private volatile Map<List<Character>, List<String>> words = new HashMap<>();
    private volatile int maxWordLength = 5;


    public MemorySuggestionsDTO getSuggestions(@NonNull BigInteger number) {

        Map<List<Character>, List<String>> words = this.words;
        List<Character> chars = getCharacters(number);

        int wordLength = AVARAGE_SYMBOLS;

        while (wordLength >= MIN_SYMBOLS) {
            List<List<String>> suggestions = getLists(words, chars, wordLength);
            boolean emptyMatches = suggestions.stream().anyMatch(Objects::isNull);
            if (emptyMatches) {
                wordLength--;
                continue;
            }

            return new MemorySuggestionsDTO(suggestions);
        }

        return new MemorySuggestionsDTO();
    }

    @PostConstruct
    public void reloadDictianaries() throws IOException {
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            doReload(stream);
        }
    }

    void doReload(@NonNull InputStream dictionaryData) {
        Map<List<Character>, List<String>> collect = new BufferedReader(
                new InputStreamReader(dictionaryData, StandardCharsets.UTF_8)).
                lines()
                .map(String::toLowerCase)
                .sorted()
                .map(this::convert2Word)
                .filter(w -> !w.significantChars().isEmpty())
                .collect(Collectors
                        .toMap(Word::significantChars, word -> singletonList(word.word()),
                                (s, s2) -> {
                                    List<String> list = new ArrayList<>();
                                    list.addAll(s);
                                    list.addAll(s2);
                                    return list;
                                }, HashMap::new));

        this.maxWordLength = collect.values().stream()
                .filter(e -> e.size() >= MIN_SYMBOLS)
                .mapToInt(List::size)
                .max().orElse(MIN_SYMBOLS);

        this.maxWordLength = collect.entrySet().stream()
                .filter(e -> e.getValue().size() >= MIN_SYMBOLS)
                .mapToInt(value -> value.getValue().size())
                .max().orElse(MIN_SYMBOLS);
        this.words = collect;
    }

    public List<Character> getCharacters(@NonNull BigInteger number) {
        String dataAsString = number.toString(10);
        return Stream
                .of(dataAsString)
                .flatMap(data -> Stream.iterate(0, i -> i + 1).limit(data.length())
                        .map(data::charAt)).map(MemorySuggestionsService::recode).collect(toList());
    }

    /* ------------------------------------------ privates -----------------------------------  */

    private static List<List<String>> getLists(Map<List<Character>, List<String>> words,
            List<Character> chars, int wordLength) {
        List<List<Character>> result = new ArrayList<>();
        int ind = 0;
        while (ind < chars.size()) {
            int nextIndex = chars.size() - ind >= wordLength ? ind + wordLength : chars.size();
            result.add(chars.subList(ind, nextIndex));
            ind = nextIndex;
        }
        return result.stream().map(words::get).collect(toList());
    }

    private Word convert2Word(String word) {
        return new Word(word);
    }

    private static Character recode(Character numberCharacter) {
        check(() -> NUMBER_MAP.containsKey(numberCharacter), () -> new IllegalStateException(
                "character " + numberCharacter + " cannot be mapped"));
        return NUMBER_MAP.get(numberCharacter);
    }


}
