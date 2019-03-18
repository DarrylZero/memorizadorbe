package com.steammachine.memorizador.service;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.steammachine.memorizador.dto.MemorySuggestionsDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class MemorySuggestionsService {

    @Value
    private class Item {

        private List<Character> characters;
        private List<String> strings;
    }


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

        public boolean hasSignificantChars() {
            return !significantChars().isEmpty();
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

    private static final int MIN_SYMBOLS = 2;
    private static final int AVERAGE_SYMBOLS = 4;
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


    public MemorySuggestionsDTO getSuggestions(@NonNull String number) {

        Map<List<Character>, List<String>> words = this.words;
        List<Character> chars = getCharacters(number);

        int wordLength = AVERAGE_SYMBOLS;

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
        this.words = new BufferedReader(
                new InputStreamReader(dictionaryData, StandardCharsets.UTF_8)).
                lines()
                .map(String::toLowerCase)
                .sorted()
                .map(this::convert2Word)
                .filter(Word::hasSignificantChars)
                .collect(toMap(Word::significantChars, word -> singletonList(word.word()),
                        MemorySuggestionsService::mergeList, HashMap::new))
                .entrySet()
                .stream()
                .map(e -> new Item(e.getKey(), getStrings(e.getValue())))
                .collect(Collectors.toMap(Item::getCharacters, Item::getStrings));
    }

    private List<String> getStrings(List<String> value) {
        List<String> strings = new ArrayList<>(new HashSet<>(value));
        strings.sort(Comparator.naturalOrder());
        return strings;
    }

    public List<Character> getCharacters(@NonNull String numberString) {
        new BigInteger(numberString);
        return Stream
                .of(numberString)
                .flatMap(data -> Stream.iterate(0, i -> i + 1).limit(data.length())
                        .map(data::charAt)).map(MemorySuggestionsService::recode).collect(toList());
    }

    /* ------------------------------------------ privates -----------------------------------  */

    private static List<String> mergeList(List<String> s, List<String> s2) {
        List<String> list = new ArrayList<>();
        list.addAll(s);
        list.addAll(s2);
        return list;
    }

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
        check(() -> NUMBER_MAP.containsKey(numberCharacter),
                () -> new IllegalStateException(
                        "character " + numberCharacter + " cannot be mapped"));
        return NUMBER_MAP.get(numberCharacter);
    }

}
