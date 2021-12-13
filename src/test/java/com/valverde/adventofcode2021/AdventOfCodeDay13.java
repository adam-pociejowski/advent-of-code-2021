package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCodeDay13 {
    List<FoldCommand> commands = new ArrayList<>();
    int width = 0;
    int height = 0;

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day13-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        String[][] cords = parseCoordinates(txtLines);
        cords = foldPaper(cords);
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day13-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        String[][] cords = parseCoordinates(txtLines);
        cords = foldPaper(cords);
        String[] strings = new String[height];
        for (int y = 0; y < height; y++) {
            strings[y] = "";
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                strings[y] += cords[x][y];
            }
        }
        for (int y = 0; y < height; y++) {
            System.out.println(strings[y]);
        }
    }

    private long countDots(String[][] cords) {
        long dots = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cords[x][y].equals("#")) {
                    dots++;
                }
            }
        }
        return dots;
    }


    private String[][] foldPaper(String[][] cords) {
        for (FoldCommand command : commands) {
            if (command.axis.equals("x")) {
                cords = reCalcCords(cords, command.number, height);
                width = command.number;
            } else {
                cords = reCalcCords(cords, width, command.number);
                height = command.number;
            }
            System.out.println(countDots(cords));
        }
        return cords;
    }

    private String[][] reCalcCords(final String[][] oldCords, int newWidth, int newHeight) {
        final String[][] cords = createBlankCords(newWidth, newHeight);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (oldCords[x][y].equals("#")) {
                    int x1 = x;
                    int y1 = y;
                    if (x > newWidth) {
                        x1 = newWidth - (x - newWidth);
                    }
                    if (y > newHeight) {
                        y1 = newHeight - (y - newHeight);
                    }
                    cords[x1][y1] = "#";
                }
            }
        }
        return cords;
    }

    private String[][] parseCoordinates(final List<String> txtLines) {
        commands = txtLines
                .stream()
                .filter(s -> s.startsWith("fold"))
                .map(s -> {
                    String fold = s.replace("fold along ", "");
                    String[] s1 = fold.split("=");
                    return new FoldCommand(s1[0], Integer.parseInt(s1[1]));
                })
                .collect(Collectors.toList());
        FoldCommand x1 = commands
                .stream()
                .filter(c -> c.axis.equals("x"))
                .findFirst().get();
        FoldCommand y1 = commands
                .stream()
                .filter(c -> c.axis.equals("y"))
                .findFirst().get();
        width = x1.number * 2 + 1;
        height = y1.number * 2 + 1;

        final String[][] cords = createBlankCords(width, height);

        int index = 0;
        String line = txtLines.get(index);
        while (!line.equals("")) {
            String[] split = line.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            cords[x][y] = "#";
            line = txtLines.get(++index);
        }
        return cords;
    }

    private String[][] createBlankCords(int widthCords, int heightCords) {
        final String[][] cords = new String[widthCords][heightCords];
        for (int x = 0; x < widthCords; x++) {
            for (int y = 0; y < heightCords; y++) {
                cords[x][y] = ".";
            }
        }
        return cords;
    }

    class FoldCommand {
        private String axis;
        private Integer number;

        public FoldCommand(String axis, Integer number) {
            this.axis = axis;
            this.number = number;
        }
    }
}
