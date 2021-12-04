package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCodeDay1 {

    @Test
    void advent_of_code__task1() throws IOException {
        final File file = new File("input/day1-input.txt");
        final List<Integer> values = Files
                .readAllLines(file.toPath(), Charset.defaultCharset())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        countNumberOfIncreasedValues(values);
    }

    @Test
    void advent_of_code__task2() throws IOException {
        final File file = new File("input/day1-input.txt");
        final List<Integer> values = Files
                .readAllLines(file.toPath(), Charset.defaultCharset())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final List<Integer> windows = new ArrayList<>();
        for (int i = 0; i < values.size() - 2; i++) {
            windows.add(values.get(i) + values.get(i + 1) + values.get(i + 2));
        }
        countNumberOfIncreasedValues(windows);
    }

    private void countNumberOfIncreasedValues(List<Integer> values) {
        int increaseCounter = 0;
        for (int i = 1; i < values.size(); i++) {
            int previousValue = values.get(i-1);
            int currentValue = values.get(i);
            if (currentValue > previousValue) {
                increaseCounter++;
            }
        }
        System.out.println(increaseCounter);
    }
}

