package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class AdventOfCodeDay2 {

    @Test
    void adventOfCodeDay2Task1() throws IOException {
        final File file = new File("input/day2-input.txt");
        final List<String> commands = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        int x = 0;
        int y = 0;

        for (String command : commands) {
            String[] parts = command.split(" ");
            String direction = parts[0];
            int value = Integer.parseInt(parts[1]);

            switch (direction) {
                case "forward":
                    x += value;
                    break;
                case "down":
                    y += value;
                    break;
                case "up":
                    y -= value;
                    break;
                default:
                    throw new RuntimeException("Not parsed: "+command);
            }
        }
        System.out.println("x: "+x);
        System.out.println("y: "+y);
        System.out.println("xy: "+x*y);
    }

    @Test
    void adventOfCodeDay2Task2() throws IOException {
        final File file = new File("input/day2-input.txt");
        final List<String> commands = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        int x = 0;
        int y = 0;
        int aim = 0;

        for (String command : commands) {
            String[] parts = command.split(" ");
            String direction = parts[0];
            int value = Integer.parseInt(parts[1]);

            switch (direction) {
                case "forward":
                    x += value;
                    y += (aim * value);
                    break;
                case "down":
                    aim += value;
                    break;
                case "up":
                    aim -= value;
                    break;
                default:
                    throw new RuntimeException("Not parsed: "+command);
            }
        }
        System.out.println("x: "+x);
        System.out.println("y: "+y);
        System.out.println("aim: "+aim);
        System.out.println("xy: "+x*y);
    }
}
