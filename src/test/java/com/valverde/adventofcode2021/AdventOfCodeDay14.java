package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class AdventOfCodeDay14 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day14-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<String> commands = parseCommandLine(txtLines.get(0));
        final List<PairRule> pairRules = parsePairs(txtLines.subList(2, txtLines.size()));
        List<String> polymer = generatePolymer(5, commands, pairRules);
        Map<String, Integer> occurrences = new HashMap<>();
        for (String s : polymer) {
            if (!occurrences.containsKey(s)) {
                occurrences.put(s, 1);
            } else {
                occurrences.put(s, occurrences.get(s) + 1);
            }
        }

        int maxNumber = 0;
        int minNumber = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            if (entry.getValue() > maxNumber) {
                maxNumber = entry.getValue();
            }
            if (entry.getValue() < minNumber) {
                minNumber = entry.getValue();
            }
        }
        System.out.println(maxNumber - minNumber);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day14-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<String> commands = parseCommandLine(txtLines.get(0));
        final List<PairRule> pairRules = parsePairs(txtLines.subList(2, txtLines.size()));
        Map<PairRule, BigInteger> map = generatePolymer2(40, commands, pairRules);
        Map<String, BigInteger> occurrences = new HashMap<>();
        for (Map.Entry<PairRule, BigInteger> entry : map.entrySet()) {
            String character = String.valueOf(entry.getKey().pair.charAt(0));
            if (!occurrences.containsKey(character)) {
                occurrences.put(character, entry.getValue());
            } else {
                occurrences.put(character, occurrences.get(character).add(entry.getValue()));
            }
        }
        String lastCharacter = commands.get(commands.size() - 1);
        occurrences.put(lastCharacter, occurrences.get(lastCharacter).add(BigInteger.ONE));
        BigInteger maxNumber = BigInteger.ONE;
        BigInteger minNumber = null;
        for (Map.Entry<String, BigInteger> entry : occurrences.entrySet()) {
            if (entry.getValue().compareTo(maxNumber) > 0) {
                maxNumber = entry.getValue();
            }
            if (Objects.isNull(minNumber) || entry.getValue().compareTo(minNumber) < 0) {
                minNumber = entry.getValue();
            }
        }
        System.out.println(maxNumber.subtract(minNumber));
    }

    Map<PairRule, BigInteger> generatePolymer2(final int steps, final List<String> commands, final List<PairRule> pairRules) {
        List<String> currentPolymer = new ArrayList<>(commands);
        Map<PairRule, BigInteger> pairOccurrences = prepareEmptyMap(pairRules);
        List<PairRule> initRules = commandsToPairs(commands, pairRules);
        for (PairRule rule : initRules) {
            pairOccurrences.put(rule, pairOccurrences.get(rule).add(BigInteger.ONE));
        }

        for (int i = 0; i < steps; i++) {
            Map<PairRule, BigInteger> newOccurrences = new HashMap<>(pairOccurrences);
            for (Map.Entry<PairRule, BigInteger> entry : pairOccurrences.entrySet()) {
                PairRule rule = entry.getKey();
                String s0 = String.valueOf(rule.pair.charAt(0));
                String s1 = String.valueOf(rule.pair.charAt(1));
                PairRule pair = findPair(s0 + rule.insert, pairRules);
                PairRule pair2 = findPair(rule.insert + s1, pairRules);
                newOccurrences.put(rule, newOccurrences.get(rule).subtract(entry.getValue()));
                newOccurrences.put(pair, newOccurrences.get(pair).add(entry.getValue()));
                newOccurrences.put(pair2, newOccurrences.get(pair2).add(entry.getValue()));
            }
            pairOccurrences = newOccurrences;
        }
        return pairOccurrences;
    }

    private PairRule findPair(String s, final List<PairRule> pairRules) {
        return pairRules
                .stream()
                .filter(r -> r.pair.equals(s))
                .findFirst()
                .get();
    }

    private Map<PairRule, BigInteger> prepareEmptyMap(List<PairRule> pairRules) {
        Map<PairRule, BigInteger> pairOccurrences = new HashMap<>();
        for (PairRule rule : pairRules) {
            pairOccurrences.put(rule, BigInteger.ZERO);
        }
        return pairOccurrences;
    }

    List<PairRule> commandsToPairs(final List<String> commands, final List<PairRule> pairRules) {
        final List<PairRule> pairs = new ArrayList<>();
        for (int index = 0; index < commands.size() - 1; index++) {
            String pair = commands.get(index) + commands.get(index + 1);
            Optional<PairRule> ruleOptional = pairRules
                    .stream()
                    .filter(r -> r.pair.equals(pair))
                    .findFirst();
            if (ruleOptional.isPresent()) {
                pairs.add(ruleOptional.get());
            } else {
                System.out.println("Pair not found: "+pair);
            }
        }
        return pairs;
    }

    List<String> generatePolymer(final int steps, final List<String> commands, final List<PairRule> pairRules) {
        List<String> currentPolymer = new ArrayList<>(commands);
        for (int i = 0; i <  steps; i++) {
            System.out.println("step: "+i);
            List<String> nextPolymer = new ArrayList<>();
            for (int index = 0; index < currentPolymer.size() - 1; index++) {
                String pair = currentPolymer.get(index) + currentPolymer.get(index + 1);
                Optional<PairRule> ruleOptional = pairRules
                        .stream()
                        .filter(r -> r.pair.equals(pair))
                        .findFirst();
                if (ruleOptional.isPresent()) {
                    nextPolymer.add(currentPolymer.get(index));
                    nextPolymer.add(ruleOptional.get().insert);
                } else {
                    System.out.println("Pair not found: "+pair);
                }
            }
            nextPolymer.add(currentPolymer.get(currentPolymer.size() - 1));
            currentPolymer = new ArrayList<>(nextPolymer);

            String polymer = "";
            for (String s : currentPolymer) {
                polymer += s;
            }
            System.out.println("step "+i+": "+polymer);
        }
        return currentPolymer;
    }

    List<PairRule> parsePairs(final List<String> txtLines) {
        return txtLines
                .stream()
                .map(l -> {
                    String[] s = l.replaceAll(" ", "").split("->");
                    return new PairRule(s[0], s[1]);
                })
                .collect(Collectors.toList());
    }

    List<String> parseCommandLine(final String line) {
        List<String> commands = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            commands.add(String.valueOf(line.charAt(i)));
        }
        return commands;
    }



    class PairRule {
        String pair;
        String insert;

        public PairRule(String pair, String insert) {
            this.pair = pair;
            this.insert = insert;
        }
    }
}
