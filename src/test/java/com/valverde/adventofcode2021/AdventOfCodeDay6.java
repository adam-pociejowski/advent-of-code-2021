package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class AdventOfCodeDay6 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day6-input.txt");
        final List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Integer> startData = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        final Map<Integer, BigInteger> map = assignStartValues(startData);
        Map<Integer, BigInteger> result = simulate(map, 80);
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i <= 8; i++) {
            sum = sum.add(result.get(i));
        }
        System.out.println("cycle "+80+": "+sum);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day6-input.txt");
        final List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Integer> startData = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        final Map<Integer, BigInteger> map = assignStartValues(startData);
        Map<Integer, BigInteger> result = simulate(map, 256);
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i <= 8; i++) {
            sum = sum.add(result.get(i));
        }
        System.out.println("cycle "+256+": "+sum);
    }

    private Map<Integer, BigInteger> simulate(final Map<Integer, BigInteger> map, final int cycles) {
        Map<Integer, BigInteger> previousState = map;
        Map<Integer, BigInteger> nextState = new HashMap<>();
        for (int cycle = 1; cycle <= cycles; cycle++) {
            nextState = new HashMap<>();
            final BigInteger fishToSpawn = previousState.get(0);
            for (int number = 1; number <= 8; number++) {
                nextState.put(number - 1, previousState.get(number));
            }
            nextState.put(6, nextState.get(6).add(fishToSpawn));
            nextState.put(8, fishToSpawn);
            previousState = nextState;

        }
        return nextState;
    }

    private Map<Integer, BigInteger> assignStartValues(final List<Integer> startData) {
        final Map<Integer, BigInteger> map = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            map.put(i, BigInteger.valueOf(0));
        }
        for (Integer i : startData) {
            map.put(i, map.get(i).add(BigInteger.valueOf(1)));
        }
        return map;
    }
}
