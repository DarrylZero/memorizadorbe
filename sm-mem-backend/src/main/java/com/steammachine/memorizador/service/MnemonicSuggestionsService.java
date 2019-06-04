package com.steammachine.memorizador.service;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.steammachine.memorizador.dto.CheckSentenceDto;
import com.steammachine.memorizador.dto.MnemonicSuggestionDto;
import com.steammachine.memorizador.dto.MnenonicSuggestionsDto;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class MnemonicSuggestionsService {

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
                    .filter(NUMBER_2_CHAR::containsValue)
                    .collect(toList());
        }

    }

    private static final int MIN_SYMBOLS = 2;
    private static final int AVERAGE_SYMBOLS = 6;
    private static final Map<Character, Character> NUMBER_2_CHAR;
    private static final Map<Character, Character> CHAR_2_NUMBER;

    static {
        Map<Character, Character> number2Char = new HashMap<>();
        number2Char.put('0', 'н');
        number2Char.put('1', 'к');
        number2Char.put('2', 'л');
        number2Char.put('3', 'т');
        number2Char.put('4', 'ч');
        number2Char.put('5', 'п');
        number2Char.put('6', 'ш');
        number2Char.put('7', 'с');
        number2Char.put('8', 'в');
        number2Char.put('9', 'д');
        NUMBER_2_CHAR = unmodifiableMap(number2Char);

        Map<Character, Character> char2Number = new HashMap<>();
        char2Number.put('н', '0');
        char2Number.put('к', '1');
        char2Number.put('л', '2');
        char2Number.put('т', '3');
        char2Number.put('ч', '4');
        char2Number.put('п', '5');
        char2Number.put('ш', '6');
        char2Number.put('с', '7');
        char2Number.put('в', '8');
        char2Number.put('д', '9');
        CHAR_2_NUMBER = unmodifiableMap(char2Number);
    }

    private volatile Map<List<Character>, List<String>> words = new HashMap<>();

    public MnenonicSuggestionsDto getSuggestions(@NonNull String number) {
        Map<List<Character>, List<String>> currentWords = this.words;
        List<Character> chars = getCharacters(number);

        MnenonicSuggestionsDto collection = new MnenonicSuggestionsDto();
        collection.setNumber(number);

        asLongAsPossible(currentWords, chars)
                .ifPresent(suggestions -> collection.getSuggestions().add(suggestions));

        getShortSuggestion(currentWords, chars)
                .ifPresent(suggestions -> collection.getSuggestions().add(suggestions));

        return collection;
    }

    @PostConstruct
    public void reloadDictianaries() throws IOException {
        try (InputStream stream = getClass()
                .getResourceAsStream("/dictionaries/dictionary1.txt")) {
            doReload(stream);
        }
    }

    public List<Character> getCharacters(@NonNull String numberString) {
        new BigInteger(numberString);
        return Stream
                .of(numberString)
                .flatMap(data -> Stream.iterate(0, i -> i + 1).limit(data.length())
                        .map(data::charAt)).map(MnemonicSuggestionsService::recode)
                .collect(toList());
    }

    public CheckSentenceDto suggestNumber(@NonNull String numberString) {
        return new CheckSentenceDto(numberString, getSuggestedNumber(numberString));
    }

    public boolean checkSentence (
            @NonNull String numberString,
            @NonNull String sentence) {
        return Objects.equals(getSuggestedNumber(sentence), numberString);
    }

    /* ------------------------------------------ privates -----------------------------------  */

    private String getSuggestedNumber(@NonNull String numberString) {
        return Stream.iterate(0, i -> i + 1)
                .limit(numberString.length())
                .map(numberString::charAt)
                .map(Character::toLowerCase)
                .filter(CHAR_2_NUMBER::containsKey)
                .map(CHAR_2_NUMBER::get)
                .map(c -> "" + c)
                .collect(joining());
    }

    private Optional<MnemonicSuggestionDto> getShortSuggestion(
            Map<List<Character>, List<String>> currentWords,
            List<Character> chars) {

        int wordLength = AVERAGE_SYMBOLS;
        while (wordLength >= MIN_SYMBOLS) {
            List<List<String>> suggestions = getLists(currentWords, chars, wordLength);
            boolean emptyMatches = suggestions.stream().anyMatch(Objects::isNull);
            if (emptyMatches) {
                wordLength--;
                continue;
            }
            return Optional.of(new MnemonicSuggestionDto(suggestions));
        }
        return Optional.empty();
    }

    private Optional<MnemonicSuggestionDto> asLongAsPossible(
            Map<List<Character>, List<String>> currentWords,
            List<Character> chars) {

        int index = 0;
        List<List<String>> suggestions = new ArrayList<>();
        while (index <= chars.size()) {
            int wordLength = AVERAGE_SYMBOLS;
            while (wordLength >= MIN_SYMBOLS) {
                Optional<List<String>> aSuggestion = getSliceWords(currentWords, chars, index,
                        wordLength);
                if (!aSuggestion.isPresent()) {
                    wordLength--;
                } else {
                    suggestions.add(aSuggestion.get());
                    index = index + wordLength;
                    break;
                }
            }
        }
        return Optional.of(new MnemonicSuggestionDto(suggestions));
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
                        MnemonicSuggestionsService::mergeList, HashMap::new))
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

    private static Optional<List<String>> getSliceWords(
            Map<List<Character>, List<String>> words, List<Character> chars,
            int start, int wordLength) {
        int toIndex = start + wordLength < chars.size() ? start + wordLength : chars.size();
        List<Character> key = chars.subList(start, toIndex);
        return Optional.ofNullable(words.get(key));
    }

    private Word convert2Word(String word) {
        return new Word(word);
    }

    private static Character recode(Character numberCharacter) {
        check(() -> NUMBER_2_CHAR.containsKey(numberCharacter),
                () -> new IllegalStateException(
                        "character " + numberCharacter + " cannot be mapped"));
        return NUMBER_2_CHAR.get(numberCharacter);
    }

}
