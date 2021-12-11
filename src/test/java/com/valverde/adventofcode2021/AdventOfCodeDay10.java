package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class AdventOfCodeDay10 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day10-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Bracket> syntaxErrors = new ArrayList<>();
        for (String line : txtLines) {
            final List<FoundBracket> foundBrackets = parseLine(line);
            checkSyntax(foundBrackets)
                    .ifPresent(b -> {
                        syntaxErrors.add(b);
                    });
        }

        final Map<Bracket, Integer> countMap = new HashMap<>();
        countMap.put(Bracket.CIRCLE, 0);
        countMap.put(Bracket.SQUARE, 0);
        countMap.put(Bracket.MUSTACHE, 0);
        countMap.put(Bracket.SHARP, 0);
        for (Bracket b : syntaxErrors) {
            countMap.put(b, countMap.get(b) + 1);
        }

        int sum = 0;
        for (Map.Entry<Bracket, Integer> entry : countMap.entrySet()) {
            sum += (entry.getKey().score * entry.getValue());
        }
        System.out.println(sum);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day10-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<List<FoundBracket>> correctLines = new ArrayList<>();
        for (String line : txtLines) {
            final List<FoundBracket> foundBrackets = parseLine(line);
            final Optional<Bracket> bracket = checkSyntax(foundBrackets);
            if (bracket.isEmpty()) {
                correctLines.add(foundBrackets);
            }
        }

        final List<BigInteger> scores = new ArrayList<>();
        for (List<FoundBracket> line : correctLines) {
            final List<Bracket> missingBracketsInOrder = getMissingBracketsInOrder(line);
            BigInteger score = BigInteger.ZERO;
            for (Bracket b : missingBracketsInOrder) {
                score = score.multiply(BigInteger.valueOf(5)).add(BigInteger.valueOf(b.closeScore));
            }
            scores.add(score);
        }

        Collections.sort(scores);
        int size = scores.size();
        System.out.println(scores.get(size / 2));
    }

    private List<Bracket> getMissingBracketsInOrder(final List<FoundBracket> foundBrackets) {
        final Stack<Bracket> expectingCloseOrder = new Stack<>();;
        for (FoundBracket bracket : foundBrackets) {
            if (bracket.type.equals(BracketType.OPEN)) {
                expectingCloseOrder.push(bracket.bracket);
            } else if (bracket.type.equals(BracketType.CLOSE)) {
                expectingCloseOrder.pop();
            }
        }

        List<Bracket> expectedOrder = new ArrayList<>();
        while (!expectingCloseOrder.isEmpty()) {
            expectedOrder.add(expectingCloseOrder.pop());
        }
        return expectedOrder;
    }

    private Optional<Bracket> checkSyntax(final List<FoundBracket> foundBrackets) {
        final Stack<Bracket> expectingCloseOrder = new Stack<>();;
        for (FoundBracket bracket : foundBrackets) {
            if (bracket.type.equals(BracketType.OPEN)) {
                expectingCloseOrder.push(bracket.bracket);
            } else if (bracket.type.equals(BracketType.CLOSE)) {
                Bracket expected = expectingCloseOrder.pop();
                if (!expected.equals(bracket.bracket)) {
                    return Optional.of(bracket.bracket);
                }
            }
        }
        return Optional.empty();
    }

    private List<FoundBracket> parseLine(final String line) {
        final ArrayList<FoundBracket> brackets = new ArrayList<>();
        for (int i = 0; i< line.length(); i++) {
            final String s = String.valueOf(line.charAt(i));
            final FoundBracket foundBracket = classifyBracket(s);
            brackets.add(foundBracket);
        }
        return brackets;
    }

    private FoundBracket classifyBracket(final String s) {
        switch (s) {
            case "(":
                return new FoundBracket(Bracket.CIRCLE, BracketType.OPEN);
            case ")":
                return new FoundBracket(Bracket.CIRCLE, BracketType.CLOSE);
            case "[":
                return new FoundBracket(Bracket.SQUARE, BracketType.OPEN);
            case "]":
                return new FoundBracket(Bracket.SQUARE, BracketType.CLOSE);
            case "{":
                return new FoundBracket(Bracket.MUSTACHE, BracketType.OPEN);
            case "}":
                return new FoundBracket(Bracket.MUSTACHE, BracketType.CLOSE);
            case "<":
                return new FoundBracket(Bracket.SHARP, BracketType.OPEN);
            case ">":
                return new FoundBracket(Bracket.SHARP, BracketType.CLOSE);
            default:
                throw new RuntimeException();
        }
    }

    static class FoundBracket {
        private final Bracket bracket;
        private final BracketType type;

        public FoundBracket(Bracket bracket, BracketType type) {
            this.bracket = bracket;
            this.type = type;
        }
    }
    enum BracketType {
        OPEN,
        CLOSE;
    }

    enum Bracket {
        CIRCLE(3, 1),
        SQUARE(57, 2),
        MUSTACHE(1197, 3),
        SHARP(25137, 4);

        private final int score;
        private final int closeScore;

        Bracket(int score, int closeScore) {
            this.score = score;
            this.closeScore = closeScore;
        }
    }
}
