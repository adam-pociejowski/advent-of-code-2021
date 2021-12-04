package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCodeDay3 {

    @Test
    void adventOfCodeDay3_task1() throws IOException {
        File file = new File("input/day3-input.txt");
        List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        int[] occurrences = findBitsOccurrences(lines);
        StringBuilder gamma = new StringBuilder();
        StringBuilder epsilon = new StringBuilder();
        for (int o : occurrences) {
            if (o > 0) {
                gamma.append("1");
                epsilon.append("0");
            } else {
                gamma.append("0");
                epsilon.append("1");
            }
        }

        int gammaDecimal = binaryToBase10(gamma.toString());
        int epsilonDecimal = binaryToBase10(epsilon.toString());
        System.out.println(gammaDecimal*epsilonDecimal);
    }

    @Test
    void adventOfCodeDay3_task2() throws IOException {
        File file = new File("input/day3-input.txt");
        List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        String oxygen = findLineWithCriteria(lines, true);
        String co2 = findLineWithCriteria(lines, false);
        int oxygenDecimal = binaryToBase10(oxygen);
        int co2Decimal = binaryToBase10(co2);
        System.out.println(oxygenDecimal*co2Decimal);
    }

    private String findLineWithCriteria(final List<String> inputLines,
                                        final boolean mostCommon) {
        List<String> lines = new ArrayList<>(inputLines);
        final StringBuilder mostCommonStartString = new StringBuilder();
        final StringBuilder leastCommonStartString = new StringBuilder();
        int lineIndex = 0;
        do {
            int[] occurrences = findBitsOccurrences(lines);
            int occurrencesInLine = occurrences[lineIndex++];
            if (occurrencesInLine > 0) {
                mostCommonStartString.append("1");
                leastCommonStartString.append("0");
            } else if (occurrencesInLine == 0) {
                mostCommonStartString.append("1");
                leastCommonStartString.append("0");
            } else {
                mostCommonStartString.append("0");
                leastCommonStartString.append("1");
            }
            lines = filterStartedWith(
                    lines,
                    mostCommon ? mostCommonStartString.toString() : leastCommonStartString.toString());
        } while (lines.size() > 1);
        return lines.get(0);
    }

    private List<String> filterStartedWith(final List<String> lines, final String start) {
        return lines
                .stream()
                .filter(l -> l.startsWith(start))
                .collect(Collectors.toList());
    }

    private int[] findBitsOccurrences(List<String> lines) {
        int[] occurrences = {0,0,0,0,0,0,0,0,0,0,0,0};
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '1') {
                    occurrences[i]++;
                } else if (c == '0') {
                    occurrences[i]--;
                } else {
                    throw new RuntimeException("Char not found");
                }
            }
        }
        return occurrences;
    }

    private static int binaryToBase10(String binaryString) {
        int val = 0;
        for (char c : binaryString.toCharArray()) {
            val <<= 1;
            val += c-'0';
        }
        return val;
    }
}
