package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdventOfCodeDay9 {

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day9-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final int[][] matrix = loadMatrix(txtLines);
        final List<Point> lowPoints = findLowPoints(matrix);
        int sum = 0;
        for (Point p : lowPoints) {
            sum += (p.value + 1);
        }
        System.out.println(sum);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day9-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final int[][] matrix = loadMatrix(txtLines);
        final List<Point> lowPoints = findLowPoints(matrix);
        final List<Basin> basins = findBasinsForLowPoints(lowPoints, matrix);
        Basin n1 = findHighestCountBasin(basins);
        basins.remove(n1);
        Basin n2 = findHighestCountBasin(basins);
        basins.remove(n2);
        Basin n3 = findHighestCountBasin(basins);
        int res = n1.points.size() * n2.points.size() * n3.points.size();
        System.out.println(res);

    }

    private Basin findHighestCountBasin(List<Basin> basins) {
        Basin basin = null;
        for (Basin b : basins) {
            if (Objects.isNull(basin) || b.points.size() > basin.points.size()) {
                basin = b;
            }
        }
        return basin;
    }

    private List<Basin> findBasinsForLowPoints(List<Point> lowPoints, int[][] matrix) {
        final List<Basin> basins = new ArrayList<>();
        int i = 0;
        for (Point p : lowPoints) {
            List<Point> basinPoints = new ArrayList<>();
            basinPoints.add(p);
            basinPoints = getAdjactedPointsWithValue(basinPoints, p, p.value + 1, matrix);
            basins.add(new Basin(basinPoints));
            System.out.println("low point "+i+++"/"+lowPoints.size());
        }
        return basins;
    }

    private List<Point> getAdjactedPointsWithValue(final List<Point> foundPoints,
                                                   final Point p,
                                                   final int expectedValue,
                                                   final int[][] matrix) {
        final List<Point> points = getMatchedAdjactedPoints(p, expectedValue, matrix);
        final List<Point> newPointsFound = points
                .stream()
                .filter(point -> {
                    for (Point po : foundPoints) {
                        if (po.x == point.x && po.y == point.y) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        foundPoints.addAll(newPointsFound);
        if (newPointsFound.isEmpty()) {
            return foundPoints;
        }

        for (Point newPoint : newPointsFound) {
            List<Point> adjactedPointsWithValue =
                    getAdjactedPointsWithValue(foundPoints, newPoint, newPoint.value + 1, matrix);
            List<Point> filtered = adjactedPointsWithValue
                    .stream()
                    .filter(point -> {
                        for (Point po : foundPoints) {
                            if (po.x == point.x && po.y == point.y) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());


            foundPoints.addAll(filtered);
        }
        return foundPoints;
    }

    private List<Point> getMatchedAdjactedPoints(Point p, int expectedValue, int[][] matrix) {
        final List<Point> points = new ArrayList<>();
        int value1 = getValue(p.x-1, p.y, matrix);
        int value2 = getValue(p.x+1, p.y, matrix);
        int value3 = getValue(p.x-1, p.y-1, matrix);
        int value4 = getValue(p.x-1, p.y+1, matrix);
        if (value1 >= expectedValue && value1 < 9) {
            points.add(new Point(p.x-1, p.y, value1));
        }
        if (value2 >= expectedValue && value2 < 9) {
            points.add(new Point(p.x+1, p.y, value2));
        }
        if (value3 >= expectedValue && value3 < 9) {
            points.add(new Point(p.x, p.y-1, value3));
        }
        if (value4 >= expectedValue && value4 < 9) {
            points.add(new Point(p.x, p.y+1, value4));
        }
        return points;
    }

    private List<Point> findLowPoints(int[][] matrix) {
        List<Point> points = new ArrayList<>();
        for (int x = 0; x< 100; x++) {
            for (int y = 0; y < 100; y++) {
                int value = matrix[x][y];
                boolean case1 = hasHigherPointValue(value, x - 1, y, matrix);
                boolean case2 = hasHigherPointValue(value, x, y - 1, matrix);
                boolean case3 = hasHigherPointValue(value, x + 1, y, matrix);
                boolean case4 = hasHigherPointValue(value, x, y + 1, matrix);
                if (case1 && case2 && case3 && case4) {
                    points.add(new Point(x, y, value));
                }
            }
        }
        return points;
    }

    private int getValue(int x, int y, int[][] matrix) {
        if (x < 0 || x >= 100 || y < 0 || y >= 100) {
            return -1;
        }
        return matrix[x][y];
    }

    private boolean hasHigherPointValue(int value, int x, int y, int[][] matrix) {
        if (x < 0 || x >= 100 || y < 0 || y >= 100) {
            return true;
        }
        return value < matrix[x][y];
    }

    private int[][] loadMatrix(final List<String> txtLines) {
        int[][] matrix = new int[100][100];
        int y = 0;
        for (String s : txtLines) {
            for (int x = 0; x < s.length(); x++) {
                matrix[x][y] = Integer.parseInt(s.substring(x, x + 1));
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

    class Basin {
        List<Point> points = new ArrayList<>();

        public Basin(List<Point> points) {
            this.points = points;
        }
    }
}
