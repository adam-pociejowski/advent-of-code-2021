package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCodeDay4 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day4-input.txt");
        final List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Integer> inputNumbers = readInputValues(lines);
        final List<BingoTable> tables = readBingoTables(lines);
        List<BingoTable> winners;
        Integer inputNumber;
        int index = 0;
        do {
            inputNumber = inputNumbers.get(index++);
            markTablesForInput(inputNumber, tables);
            winners = findWinnerTables(tables);
        } while (winners.isEmpty());

        if (winners.size() == 1) {
            Integer sum = sumAllUnmarked(winners.get(0));
            System.out.println(sum*inputNumber);
        }
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day4-input.txt");
        final List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Integer> inputNumbers = readInputValues(lines);
        final List<BingoTable> tables = readBingoTables(lines);
        List<BingoTable> winners;
        Integer inputNumber;
        int index = 0;
        do {
            inputNumber = inputNumbers.get(index++);
            markTablesForInput(inputNumber, tables);
            winners = findWinnerTables(tables);
            tables.removeAll(winners);
        } while (!tables.isEmpty());

        if (winners.size() == 1) {
            Integer sum = sumAllUnmarked(winners.get(0));
            System.out.println(sum*inputNumber);
        }
    }

    private Integer sumAllUnmarked(final BingoTable table) {
        int sum = 0;
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {
                BingoCell bingoCell = table.cells[row][col];
                if (!bingoCell.marked) {
                    sum += bingoCell.number;
                }
            }
        }
        return sum;
    }

    private List<BingoTable> findWinnerTables(final List<BingoTable> tables) {
        final List<BingoTable> winners = new ArrayList<>();
        tables
                .forEach(t -> {
                    boolean solved = false;
                    for (int row = 0; row < 5; row++) {
                        if (hasMarkedRow(row, t)) {
                            solved = true;
                        }
                    }

                    for (int col = 0; col < 5; col++) {
                        if (hasMarkedCol(col, t)) {
                            solved = true;
                        }
                    }
                    if (solved) {
                        winners.add(t);
                    }
                });
        return winners;
    }

    private boolean hasMarkedCol(final int col, final BingoTable t) {
        for (int row = 0; row < 5; row++) {
            BingoCell cell = t.cells[row][col];
            if (!cell.marked) {
                return false;
            }
        }
        return true;
    }

    private boolean hasMarkedRow(final int row, final BingoTable t) {
        BingoCell[] rowCells = t.cells[row];
        for (BingoCell cell : rowCells) {
            if (!cell.marked) {
                return false;
            }
        }
        return true;
    }

    private void markTablesForInput(final Integer inputNumber, final List<BingoTable> tables) {
        tables
                .forEach(t -> {
                    for (int col = 0; col < 5; col++) {
                        for (int row = 0; row < 5; row++) {
                            BingoCell bingoCell = t.cells[row][col];
                            if (bingoCell.number == inputNumber) {
                                bingoCell.marked = true;
                            }
                        }
                    }
                });
    }

    private List<BingoTable> readBingoTables(final List<String> lines) {
        final List<BingoTable> tables = new ArrayList<>();
        for (int i = 2; i < lines.size(); i += 6) {
            final BingoTable table = new BingoTable();
            table.cells[0] = parseLine(lines.get(i));
            table.cells[1] = parseLine(lines.get(i + 1));
            table.cells[2] = parseLine(lines.get(i + 2));
            table.cells[3] = parseLine(lines.get(i + 3));
            table.cells[4] = parseLine(lines.get(i + 4));
            tables.add(table);
        }
        return tables;
    }

    private BingoCell[] parseLine(final String line) {
        return Arrays
                .stream(line.split(" "))
                .filter(v -> !v.equals(""))
                .map(v -> {
                    int i = Integer.parseInt(v);
                    return new BingoCell(i, false);
                })
                .collect(Collectors.toList())
                .toArray(new BingoCell[5]);
    }

    private List<Integer> readInputValues(final List<String> lines) {
        return Arrays
                .stream(lines.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    class BingoTable {
        private BingoCell[][] cells = new BingoCell[5][5];
    }

    class BingoCell {
        private int number;
        private boolean marked;

        public BingoCell(int number, boolean marked) {
            this.number = number;
            this.marked = marked;
        }
    }
}
