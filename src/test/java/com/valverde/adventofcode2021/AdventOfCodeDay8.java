package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class AdventOfCodeDay8 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day8-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Line> lines = parseInput(txtLines);
        int i1 = countOccurrencesOfStringWithLength(2, lines);
        int i4 = countOccurrencesOfStringWithLength(4, lines);
        int i7 = countOccurrencesOfStringWithLength(3, lines);
        int i8 = countOccurrencesOfStringWithLength(7, lines);
        System.out.println(i1 + i4 + i7 + i8);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day8-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Line> lines = parseInput(txtLines);
        final List<Digit> digits = getDigits();
        int sum = 0;
        for (Line line : lines) {
            final Map<String, String> decodedCharacters = decodeCharacterForLine(line);
            final Integer value = parseDigitsInLines(line.afterDelimiter, digits, decodedCharacters);
            sum += value;
        }
        System.out.println(sum);
    }


    private int countOccurrencesOfStringWithLength(final int length, final List<Line> lines) {
        int occurrence = 0;
        for (Line line : lines) {
            for (String b : line.afterDelimiter) {
                if (b.length() == length) {
                    occurrence++;
                }
            }
        }
        return occurrence;
    }

    private List<Line> parseInput(final List<String> txtLines) {
        return txtLines
                .stream()
                .map(l -> {
                    String[] split = l.split("\\|");
                    List<String> before = Arrays.stream(split[0].split(" ")).filter(s -> !s.equals("")).collect(Collectors.toList());
                    List<String> after = Arrays.stream(split[1].split(" ")).filter(s -> !s.equals("")).collect(Collectors.toList());
                    return new Line(before, after);
                })
                .collect(Collectors.toList());
    }

    private Integer parseDigitsInLines(List<String> afterDelimiter, List<Digit> digits, Map<String, String> decodedCharacters) {
        List<Integer> numbers = afterDelimiter
                .stream()
                .map(s -> {
                    StringBuilder decoded = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        String decodedChar = "";
                        for (Map.Entry<String, String> entry : decodedCharacters.entrySet()) {
                            if (entry.getValue().equals(String.valueOf(s.charAt(i)))) {
                                decodedChar = entry.getKey();
                            }
                        }
                        decoded.append(decodedChar);
                    }
                    return decodeDigit(digits, decoded.toString());
                })
                .collect(Collectors.toList());
        int value = numbers.get(0) * 1000;
        value += numbers.get(1) * 100;
        value += numbers.get(2) * 10;
        value += numbers.get(3);
        return value;
    }

    private int decodeDigit(final List<Digit> digits, final String di) {
        for (Digit d : digits) {
            if (d.characters.length == di.length() && containsAllCharacters(d, di)) {
                return d.value;
            }
        }
        throw new RuntimeException(di);
    }

    boolean containsAllCharacters(final Digit digit, final String di) {
        for (int i = 0; i < digit.characters.length; i++) {
            if (!di.contains(digit.characters[i])) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> decodeCharacterForLine(Line line) {
        final List<String> beforeDelimiter = line.beforeDelimiter;
        final Map<String, String> decodedCharacters = new HashMap<>();
        final List<String> characters = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        decodeUniqueCountCharacters(beforeDelimiter, characters, decodedCharacters);
        decodeC(beforeDelimiter, decodedCharacters);
        decodeA(beforeDelimiter, decodedCharacters);
        decodeD(beforeDelimiter, decodedCharacters);
        decodeG(decodedCharacters);
        return decodedCharacters;
    }

    private void decodeG(Map<String, String> decodedCharacters) {
        final List<String> characters = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        for (String s: decodedCharacters.values()) {
            characters.remove(s);
        }
        decodedCharacters.put("g", characters.get(0));
    }

    private void decodeD(List<String> beforeDelimiter, Map<String, String> decodedCharacters) {
        for (String s: beforeDelimiter) {
            if (s.length() == 4) {
                decodedCharacters.put("d", s
                        .replace(decodedCharacters.get("f"), "")
                        .replace(decodedCharacters.get("b"), "")
                        .replace(decodedCharacters.get("c"), ""));
            }
        }
    }

    private void decodeA(final List<String> beforeDelimiter, final Map<String, String> decodedCharacters) {
        for (String s: beforeDelimiter) {
            if (s.length() == 3) {
                decodedCharacters.put("a", s
                        .replace(decodedCharacters.get("f"), "")
                        .replace(decodedCharacters.get("c"), ""));
            }
        }
    }

    private void decodeC(final List<String> beforeDelimiter, final Map<String, String> decodedCharacters) {
        for (String s: beforeDelimiter) {
            if (s.length() == 2) {
                decodedCharacters.put("c", s.replace(decodedCharacters.get("f"), ""));
            }
        }
    }

    private List<Digit> getDigits() {
        return Arrays.asList(
                new Digit(0, new String[]{"a", "b", "c", "e", "f", "g"}),
                new Digit(1, new String[]{"c", "f"}),
                new Digit(2, new String[]{"a", "c", "d", "e", "g"}),
                new Digit(3, new String[]{"a", "c", "d", "f", "g"}),
                new Digit(4, new String[]{"b", "c", "d", "f"}),
                new Digit(5, new String[]{"a", "b", "d", "f", "g"}),
                new Digit(6, new String[]{"a", "b", "d", "e", "f", "g"}),
                new Digit(7, new String[]{"a", "c", "f"}),
                new Digit(8, new String[]{"a", "b", "c", "d", "e", "f", "g"}),
                new Digit(9, new String[]{"a", "b", "c", "d", "f", "g"})
        );
    }

    private void decodeUniqueCountCharacters(List<String> beforeDelimiter, List<String> characters, Map<String, String> decodedCharacters) {
        for (String key : characters) {
            Integer count = countOccurrencesOfChar(key, beforeDelimiter);
            if (count == 4) {
                decodedCharacters.put("e", key);
            } else if (count == 6) {
                decodedCharacters.put("b", key);
            } else if (count == 9) {
                decodedCharacters.put("f", key);
            }
        }
    }

    private Integer countOccurrencesOfChar(final String character, final List<String> decoded) {
        int i = 0;
        for (String d : decoded) {
            if (d.contains(character)) {
                i++;
            }
        }
        return i;
    }

    class Line {
        private List<String> beforeDelimiter;
        private List<String> afterDelimiter;

        public Line(List<String> beforeDelimiter, List<String> afterDelimiter) {
            this.beforeDelimiter = beforeDelimiter;
            this.afterDelimiter = afterDelimiter;
        }
    }

    class Digit {
        private int value;
        private String[] characters;

        public Digit(int value, String[] characters) {
            this.value = value;
            this.characters = characters;
        }
    }
}
