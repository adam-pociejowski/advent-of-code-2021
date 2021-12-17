package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class AdventOfCodeDay15 {
    Set<Node> nodes = new HashSet<>();
    Set<Node> settledNodes = new HashSet<>();
    Set<Node> unsettledNodes = new HashSet<>();
    int [][] d;
    int length;
    int height;

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day15-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        parseFile2(txtLines);
        unsettledNodes.add(findNode(0, 0));
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Node node: getNeighbours(currentNode.x, currentNode.y)) {
                Integer edgeWeight = d[node.x][node.y];
                if (!settledNodes.contains(node)) {
                    calculateMinimumDistance(node, edgeWeight, currentNode);
                    unsettledNodes.add(node);
                }
            }
            System.out.println(settledNodes.size()+"/"+length*height);
            settledNodes.add(currentNode);
        }
        Node value = getValue(length - 1, height - 1);
    }

    private void parseFile2(final List<String> txtLines) {
        int originalLength = txtLines.get(0).length();
        int originalHeight = txtLines.size();
        length = originalLength * 5;
        height = originalHeight * 5;
        d = new int[originalLength * 5][originalHeight * 5];
        generateOriginalMatrix(txtLines);
        repeatRowHorizontally(originalLength, originalHeight, 5);
        for (int i = 0; i < 5; i++) {
            int startX = originalLength * i;
            repeatColumnVertically(originalLength, originalHeight, 5, startX);
        }
        findNode(0, 0).distanceTo = 0;
    }

    private void repeatColumnVertically(int originalLength, int originalHeight, int times, int startX) {
        for (int i = 1; i < 5; i++) {
            int startY = originalHeight * i;
            for (int x = startX; x < startX + originalLength; x++) {
                for (int y = startY; y < startY + originalHeight; y++) {
                    d[x][y] = getGeneratedValue(d[x][y -(originalHeight*i)], i);
                    nodes.add(new Node(Integer.MAX_VALUE, x, y));
                }
            }
        }
    }

    private void repeatRowHorizontally(int originalLength, int originalHeight, int times) {
        for (int i = 1; i < 5; i++) {
            int startX = originalLength * i;
            for (int x = startX; x < startX + originalLength; x++) {
                for (int y = 0; y < originalHeight; y++) {
                    d[x][y] = getGeneratedValue(d[x - (originalLength*i)][y] , i);
                    nodes.add(new Node(Integer.MAX_VALUE, x, y));
                }
            }
        }
    }

    int getGeneratedValue(int oldValue, int increment) {
        int newValue = oldValue + increment;
        if (newValue > 9) {
            return newValue - 9;
        }
        return newValue;
    }

    private void generateOriginalMatrix(List<String> txtLines) {
        int y = 0;
        for (String line : txtLines) {
            for (int x = 0; x < line.length(); x++) {
                d[x][y] = Integer.parseInt(String.valueOf(line.charAt(x)));
                nodes.add(new Node(Integer.MAX_VALUE, x, y));
            }
            y++;
        }
    }

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day15-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        parseFile(txtLines);
        unsettledNodes.add(findNode(0, 0));
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Node node: getNeighbours(currentNode.x, currentNode.y)) {
                Integer edgeWeight = d[node.x][node.y];
                if (!settledNodes.contains(node)) {
                    calculateMinimumDistance(node, edgeWeight, currentNode);
                    unsettledNodes.add(node);
                }
            }
            settledNodes.add(currentNode);
        }
        Node value = getValue(txtLines.get(0).length() - 1, txtLines.size() - 1);
    }

    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh,
                                                 Node sourceNode) {
        Integer sourceDistance = sourceNode.distanceTo;
        if (sourceDistance + edgeWeigh < evaluationNode.distanceTo) {
            evaluationNode.distanceTo = sourceDistance + edgeWeigh;
            List<Node> shortestPath = new ArrayList<>(sourceNode.shortestPath);
            shortestPath.add(sourceNode);
            evaluationNode.shortestPath = shortestPath;
        }
    }

    private HashSet<Node> getNeighbours(int x, int y) {
        HashSet<Node> list = new HashSet<>();
        addIfNotNull(list, getValue(x-1, y));
        addIfNotNull(list, getValue(x+1, y));
        addIfNotNull(list, getValue(x, y+1));
        addIfNotNull(list, getValue(x, y-1));
        return list;
    }

    private Node getValue(Set<Node> nodes , int x, int y) {
        for (Node n : nodes) {
            if (n.x == x & n.y == y) {
                return n;
            }
        }
        return null;
    }

    private Node getValue(int x, int y) {
        return getValue(nodes, x, y);
    }

    private void addIfNotNull(HashSet<Node> list, Node n) {
        if (Objects.nonNull(n)) {
            list.add(n);
        }
    }

    private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.distanceTo;
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void parseFile(final List<String> txtLines) {
        d = new int[txtLines.get(0).length()][txtLines.size()];
        int y = 0;
        for (String line : txtLines) {
            for (int x = 0; x < line.length(); x++) {
                d[x][y] = Integer.parseInt(String.valueOf(line.charAt(x)));
                nodes.add(new Node(Integer.MAX_VALUE, x, y));
            }
            y++;
        }
        findNode(0, 0).distanceTo = 0;
    }

    private Node findNode(int x, int y) {
        return nodes
                .stream()
                .filter(n -> n.x ==x && n.y == y)
                .findFirst()
                .get();
    }

    class Node {
        int distanceTo;
        int x;
        int y;
        List<Node> shortestPath = new ArrayList<>();

        public Node(int distanceTo, int x, int y) {
            this.distanceTo = distanceTo;
            this.x = x;
            this.y = y;
        }
    }

}
