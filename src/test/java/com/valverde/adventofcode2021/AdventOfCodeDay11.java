package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdventOfCodeDay11 {
    private List<Point> flashed = new ArrayList<>();

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day11-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final Point[][] matrix = loadMatrix(txtLines);
        int flashesCount = 0;
        for (int step = 0; step < 100; step++) {
            flashed = new ArrayList<>();
            flashesCount += simulateStep(matrix);
        }
        System.out.println(flashesCount);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day11-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final Point[][] matrix = loadMatrix(txtLines);
        for (int step = 0; step < 1000; step++) {
            flashed = new ArrayList<>();
            int fleshesInStep = simulateStep(matrix);
            if (fleshesInStep == 100) {
                System.out.println(step+1);
                break;
            }
        }
    }

    private int simulateStep(final Point[][] matrix) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                matrix[x][y].value++;
            }
        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (matrix[x][y].value > 9) {
                    simulateOctopus(matrix, x, y);
                }
            }
        }
        flashed.forEach(f -> f.value = 0);
        return flashed.size();
    }

    private void simulateOctopus(final Point[][] matrix, int x, int y) {
        matrix[x][y].value++;
        if (matrix[x][y].value <= 9) {
            return;
        } else if (flashed.contains(matrix[x][y])) {
            return;
        } else {
            flashed.add(matrix[x][y]);
            for (Point n : getNeighbours(matrix, x, y)) {
                simulateOctopus(matrix, n.x, n.y);
            }
        }
    }

    private List<Point> getNeighbours(final Point[][] matrix, int x, int y) {
        List<Point> list = new ArrayList<>();
        addIfNotNull(list, getValue(x-1, y+1, matrix));
        addIfNotNull(list, getValue(x-1, y-1, matrix));
        addIfNotNull(list, getValue(x-1, y, matrix));
        addIfNotNull(list, getValue(x+1, y, matrix));
        addIfNotNull(list, getValue(x+1, y-1, matrix));
        addIfNotNull(list, getValue(x+1, y+1, matrix));
        addIfNotNull(list, getValue(x, y+1, matrix));
        addIfNotNull(list, getValue(x, y-1, matrix));
        return list;
    }

    private void addIfNotNull(List<Point> list, Point p) {
        if (Objects.nonNull(p)) {
            list.add(p);
        }
    }

    private Point getValue(int x, int y, Point[][] matrix) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            return null;
        }
        return matrix[x][y];
    }

    private Point[][] loadMatrix(final List<String> txtLines) {
        Point[][] matrix = new Point[10][10];
        int y = 0;
        for (String s : txtLines) {
            for (int x = 0; x < s.length(); x++) {
                matrix[x][y] = new Point(x, y, Integer.parseInt(s.substring(x, x + 1)));
            }
            y++;
        }
        return matrix;
    }

    class Point {
        int x;
        int y;
        int value;

        public Point(int x, int y,  int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && value == point.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, value);
        }
    }
}
