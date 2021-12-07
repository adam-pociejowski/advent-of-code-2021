package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.valverde.adventofcode2021.AdventOfCodeDay5.LineType.*;

public class AdventOfCodeDay5 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/test.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Line> lines = loadVerticalAndHorizontalLines(txtLines);
        int[][] matrix = prepareMatrix();
        int[][] markedMatrix = markLineOnMatrix(matrix, lines);
        int result = countMarkedMoreThan(2, markedMatrix);
        System.out.println(result);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/test.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<Line> lines = loadVerticalAndHorizontalAndDiagonalLines(txtLines);
        int[][] matrix = prepareMatrix();
        int[][] markedMatrix = markLineOnMatrix(matrix, lines);
        int result = countMarkedMoreThan(2, markedMatrix);
        System.out.println(result);
    }

    private int countMarkedMoreThan(final int number, final int[][] matrix) {
        int cells = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (matrix[i][j] >= number) {
                    cells++;
                }
            }
        }
        return cells;
    }

    private int[][] markLineOnMatrix(final int[][] matrix, final List<Line> lines) {
        lines
                .forEach(line -> {
                    switch (line.type) {
                        case HORIZONTAL:
                            for (int x = line.from.x; x <= line.to.x; x++) {
                                matrix[x][line.from.y]++;
                            }
                            break;
                        case VERTICAL:
                            for (int y = line.from.y; y <= line.to.y; y++) {
                                matrix[line.from.x][y]++;
                            }
                            break;
                        case DIAGONAL:
                            if (line.from.x < line.to.x && line.from.y < line.to.y) {
                                int y = line.from.y;
                                for (int x = line.from.x; x <= line.to.x; x++, y++) {
                                    matrix[x][y]++;
                                }
                            } else if (line.from.x < line.to.x && line.from.y > line.to.y) {
                                int y = line.from.y;
                                for (int x = line.from.x; x <= line.to.x; x++, y--) {
                                    matrix[x][y]++;
                                }
                            } else if (line.from.x > line.to.x && line.from.y > line.to.y) {
                                int y = line.from.y;
                                for (int x = line.from.x; x >= line.to.x; x--, y--) {
                                    matrix[x][y]++;
                                }
                            } else {
                                int y = line.from.y;
                                for (int x = line.from.x; x >= line.to.x; x--, y++) {
                                    matrix[x][y]++;
                                }
                            }
                            break;
                    }
                });
        return matrix;
    }

    private int[][] prepareMatrix() {
        int[][] matrix = new int[1000][1000];
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    private List<Line> loadVerticalAndHorizontalAndDiagonalLines(final List<String> txtLines) {
        final List<Line> lines = new ArrayList<>();
        for (String l : txtLines) {
            final String[] split = l.replaceAll(" ", "").split("->");
            final String[] fromString = split[0].split(",");
            final String[] toString = split[1].split(",");
            final Point first = new Point(Integer.parseInt(fromString[0]), Integer.parseInt(fromString[1]));
            final Point second = new Point(Integer.parseInt(toString[0]), Integer.parseInt(toString[1]));
            final Line line = prepareVerticalOrHorizontalOrDiagonal(first, second);
            if (Objects.nonNull(line)) {
                lines.add(line);
            }
        }
        return lines;
    }

    private List<Line> loadVerticalAndHorizontalLines(final List<String> txtLines) {
        final List<Line> lines = new ArrayList<>();
        for (String l : txtLines) {
            final String[] split = l.replaceAll(" ", "").split("->");
            final String[] fromString = split[0].split(",");
            final String[] toString = split[1].split(",");
            final Point first = new Point(Integer.parseInt(fromString[0]), Integer.parseInt(fromString[1]));
            final Point second = new Point(Integer.parseInt(toString[0]), Integer.parseInt(toString[1]));
            final Line line = prepareVerticalOrHorizontal(first, second);
            if (Objects.nonNull(line)) {
                lines.add(line);
            }
        }
        return lines;
    }

    private Line prepareVerticalOrHorizontalOrDiagonal(Point first, Point second) {
        final Line line = prepareVerticalOrHorizontal(first, second);
        if (Objects.nonNull(line)) {
            return line;
        }

        if (Math.abs(first.x - second.x) == Math.abs(first.y - second.y)) {
            return new Line(first, second, DIAGONAL);
        }
        return null;
    }

    private Line prepareVerticalOrHorizontal(Point first, Point second) {
        if (first.x == second.x) {
            return first.y < second.y ? new Line(first, second, VERTICAL) : new Line(second, first, VERTICAL);
        } else if (first.y == second.y) {
            return first.x < second.x ? new Line(first, second, HORIZONTAL) : new Line(second, first, HORIZONTAL);
        }
        return null;
    }



    class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Line {
        private Point from;
        private Point to;
        private LineType type;

        public Line(Point from, Point to, LineType type) {
            this.from = from;
            this.to = to;
            this.type = type;
            if (to.x > 1000 || to.y > 1000) {
                System.out.println(to);
            }
            if (from.x > 1000 || from.y > 1000) {
                System.out.println(from);
            }
        }
    }

    enum LineType {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL
    }
}
