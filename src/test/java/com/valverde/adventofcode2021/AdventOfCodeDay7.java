package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AdventOfCodeDay7 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day7-input.txt");
        final List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Integer> positions = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        System.out.println("cost: "+findCheapestCost(positions));
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day7-input.txt");
        final List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Integer> positions = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        System.out.println("cost: "+findCheapestCostTask2(positions));
    }

    private Integer findCheapestCost(final List<Integer> positions) {
        int max = positions
                .stream()
                .mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
        int min = positions
                .stream()
                .mapToInt(v -> v)
                .min().orElseThrow(NoSuchElementException::new);
        int cheapestCost = -1;

        for (int i = min; i <= max; i++) {
            int cost = calcCostForPos(i, positions);
            if (cheapestCost == -1 || cost < cheapestCost) {
                cheapestCost = cost;
            }
        }
        return cheapestCost;
    }

    int calcCostForPos(int i, final List<Integer> positions) {
        int cost = 0;
        for (int pos : positions) {
            cost += Math.abs(pos - i);
        }
        return cost;
    }

    private Integer findCheapestCostTask2(final List<Integer> positions) {
        int max = positions
                .stream()
                .mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
        int min = positions
                .stream()
                .mapToInt(v -> v)
                .min().orElseThrow(NoSuchElementException::new);
        int cheapestCost = -1;

        for (int i = min; i <= max; i++) {
            int cost = calcCostForPosTask2(i, positions);
            if (cheapestCost == -1 || cost < cheapestCost) {
                cheapestCost = cost;
            }
        }
        return cheapestCost;
    }


    int calcCostForPosTask2(int i, final List<Integer> positions) {
        int cost = 0;
        for (int pos : positions) {
            cost += calDistanceCost(Math.abs(pos - i));
        }
        return cost;
    }

    int calDistanceCost(int distance) {
        int sum = 0;
        for (int i = 1; i <= distance; i ++) {
            sum += i;
        }
        return sum;
    }
}
