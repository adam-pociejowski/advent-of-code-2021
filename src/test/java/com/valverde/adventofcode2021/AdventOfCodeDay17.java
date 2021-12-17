package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AdventOfCodeDay17 {
    int minX;
    int maxX;
    int minY;
    int maxY;

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day17-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        parseLine(txtLines);
        int maxHeight = 0;
        for (int veloX = 1; veloX < 100; veloX++) {
            for (int veloY = -10; veloY < 100; veloY++) {
                List<Point> trajectory = simulateTrajectory(veloX, veloY);
                if (Objects.nonNull(trajectory)) {
                    int maxCurrentHeight = 0;
                    for (Point p : trajectory) {
                        if (p.y > maxCurrentHeight) {
                            maxCurrentHeight = p.y;
                        }
                    }
                    if (maxCurrentHeight > maxHeight) {
                        maxHeight = maxCurrentHeight;
                    }
                }
            }
        }
        System.out.println(maxHeight);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day17-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        parseLine(txtLines);
        int counter = 0;
        for (int veloX = -1000; veloX < 1000; veloX++) {
            for (int veloY = -1000; veloY < 1000; veloY++) {
                List<Point> trajectory = simulateTrajectory(veloX, veloY);
                if (Objects.nonNull(trajectory)) {
                    counter++;
                }
            }
        }
        System.out.println(counter);
    }

    private List<Point> simulateTrajectory(int velocityX, int velocityY) {
        int x = 0;
        int y = 0;
        int currentVelocityX = velocityX;
        int currentVelocityY = velocityY;
        List<Point> trajectory = new LinkedList<>();
        trajectory.add(new Point(x , y));
        while (!reachedGoal(x , y) && !overshotGoal(x, y)) {
            x += currentVelocityX;
            y += currentVelocityY;
            trajectory.add(new Point(x , y));
            if (currentVelocityX > 0) {
                currentVelocityX--;
            } else if (currentVelocityX < 0) {
                currentVelocityX++;
            }
            currentVelocityY--;
        }
        if (overshotGoal(x, y)) {
            return null;
        }

        return trajectory;
    }

    private boolean reachedGoal(int x, int y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    private boolean overshotGoal(int x, int y) {
        return x > maxX || y < minY;
    }

    private void parseLine(final List<String> txtLines) {
        String line = txtLines.get(0);
        String[] split = line.replace("target area: ", "").split(",");
        String[] splitX = split[0].replace("x=", "").split("\\.\\.");
        String[] splitY = split[1].replace(" y=", "").split("\\.\\.");
        minX = Integer.parseInt(splitX[0]);
        maxX = Integer.parseInt(splitX[1]);
        minY = Integer.parseInt(splitY[0]);
        maxY = Integer.parseInt(splitY[1]);
    }

    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
