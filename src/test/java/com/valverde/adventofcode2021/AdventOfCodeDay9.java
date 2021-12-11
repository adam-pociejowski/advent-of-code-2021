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
        final List<BasinPoint> lowPoints = findLowPoints(matrix);
        int sum = 0;
        for (BasinPoint p : lowPoints) {
            sum += (p.value + 1);
        }
        System.out.println(sum);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day9-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final int[][] matrix = loadMatrix(txtLines);
        final List<BasinPoint> lowPoints = findLowPoints(matrix);
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

    private List<Basin> findBasinsForLowPoints(List<BasinPoint> lowPoints, int[][] matrix) {
        final List<Basin> basins = new ArrayList<>();
        int i = 0;
        for (BasinPoint p : lowPoints) {
            List<BasinPoint> basinPoints = new ArrayList<>();
            basinPoints.add(p);
            basinPoints = getAdjactedPointsWithValue(basinPoints, p, p.value + 1, matrix);
            basins.add(new Basin(basinPoints));
            System.out.println("low point "+i+++"/"+lowPoints.size());
        }
        return basins;
    }

    private List<BasinPoint> getAdjactedPointsWithValue(final List<BasinPoint> foundPoints,
                                                        final BasinPoint p,
                                                        final int expectedValue,
                                                        final int[][] matrix) {
        final List<BasinPoint> points = getMatchedAdjactedPoints(p, expectedValue, matrix);
        final List<BasinPoint> newPointsFound = points
                .stream()
                .filter(point -> {
                    for (BasinPoint po : foundPoints) {
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

        for (BasinPoint newPoint : newPointsFound) {
            List<BasinPoint> adjactedPointsWithValue =
                    getAdjactedPointsWithValue(foundPoints, newPoint, newPoint.value + 1, matrix);
            List<BasinPoint> filtered = adjactedPointsWithValue
                    .stream()
                    .filter(point -> {
                        for (BasinPoint po : foundPoints) {
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

    private List<BasinPoint> getMatchedAdjactedPoints(BasinPoint p, int expectedValue, int[][] matrix) {
        final List<BasinPoint> points = new ArrayList<>();
        int value1 = getValue(p.x-1, p.y, matrix);
        int value2 = getValue(p.x+1, p.y, matrix);
        int value3 = getValue(p.x, p.y-1, matrix);
        int value4 = getValue(p.x, p.y+1, matrix);
        if (value1 >= expectedValue && value1 < 9) {
            points.add(new BasinPoint(p.x-1, p.y, value1));
        }
        if (value2 >= expectedValue && value2 < 9) {
            points.add(new BasinPoint(p.x+1, p.y, value2));
        }
        if (value3 >= expectedValue && value3 < 9) {
            points.add(new BasinPoint(p.x, p.y-1, value3));
        }
        if (value4 >= expectedValue && value4 < 9) {
            points.add(new BasinPoint(p.x, p.y+1, value4));
        }
        return points;
    }

    private List<BasinPoint> findLowPoints(int[][] matrix) {
        List<BasinPoint> points = new ArrayList<>();
        for (int x = 0; x< 100; x++) {
            for (int y = 0; y < 100; y++) {
                int value = matrix[x][y];
                boolean case1 = hasHigherPointValue(value, x - 1, y, matrix);
                boolean case2 = hasHigherPointValue(value, x, y - 1, matrix);
                boolean case3 = hasHigherPointValue(value, x + 1, y, matrix);
                boolean case4 = hasHigherPointValue(value, x, y + 1, matrix);
                if (case1 && case2 && case3 && case4) {
                    points.add(new BasinPoint(x, y, value));
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

    class BasinPoint {
        int x;
        int y;
        int value;

        public BasinPoint(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BasinPoint point = (BasinPoint) o;
            return x == point.x && y == point.y && value == point.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, value);
        }
    }

    class Basin {
        List<BasinPoint> points = new ArrayList<>();

        public Basin(List<BasinPoint> points) {
            this.points = points;
        }
    }
}
