package com.valverde.adventofcode2021;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class AdventOfCodeDay12 {
    List<List<PathNode>> allPaths = new ArrayList<>();

    @Test
    public void task1() throws IOException {
        final File file = new File("input/day12-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<PathNode> pathNodes = parsePathNodes(txtLines);
        allPaths = new ArrayList<>();
        findAllPaths(pathNodes);
        List<List<PathNode>> collect = allPaths
                .stream()
                .filter(path -> {
                    for (PathNode p : path) {
                        if (p.from.isSmallCave && (!p.from.name.equals("start") && !p.from.name.equals("end"))) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
        System.out.println(collect.size());
    }

    @Test
    public void task2() throws IOException {
        final File file = new File("input/day12-input.txt");
        final List<String> txtLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final List<PathNode> pathNodes = parsePathNodes(txtLines);
        allPaths = new ArrayList<>();
        findAllPaths2(pathNodes);
        for (List<PathNode> path : allPaths) {
            StringBuilder sb = new StringBuilder("start");
            for (PathNode pathNode : path) {
                sb.append("-").append(pathNode.to.name);
            }
            System.out.println(sb);
        }
        System.out.println(allPaths.size());

    }

    private void findAllPaths2(final List<PathNode> allNodes) {
        final List<PathNode> startNodes = findStartPaths(allNodes);
        for (final PathNode startNode : startNodes) {
            findPath2(Collections.singletonList(startNode), allNodes);
        }
    }

    private void findPath2(final List<PathNode> currentPath, final List<PathNode> allNodes) {
        final PathNode lastPathNode = currentPath.get(currentPath.size() - 1);
        final List<PathNode> possibleNextNodes = getPossibleNextPathNodes2(allNodes, lastPathNode.to, currentPath);
        if (possibleNextNodes.isEmpty()) {
            return;
        }
        for (final PathNode nextNode : possibleNextNodes) {
            final ArrayList<PathNode> pathNodes = new ArrayList<>(currentPath);
            pathNodes.add(nextNode);
            if (nextNode.to.name.equals("end")) {
                allPaths.add(pathNodes);
            } else {
                findPath2(pathNodes, allNodes);
            }
        }
    }

    private List<PathNode> getPossibleNextPathNodes2(final List<PathNode> allNodes,
                                                     final Node from,
                                                     final List<PathNode> currentPath) {
        List<PathNode> allAvailablePaths = findAllAvailablePaths(from, allNodes);
        boolean finalHasDouble = hasDoubleSmallCave(currentPath);
        return allAvailablePaths
                .stream()
                .filter(a -> {
                    if (a.to.name.equals("start") || a.to.name.equals("end")) {
                        for (PathNode pn : currentPath) {
                            if (pn.from.name.equals(a.to.name)) {
                                return false;
                            }
                        }
                        return true;
                    } else if (a.to.isSmallCave) {
                        if (finalHasDouble) {
                            for (PathNode pn : currentPath) {
                                if (pn.from.name.equals(a.to.name)) {
                                    return false;
                                }
                            }
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private boolean hasDoubleSmallCave(List<PathNode> currentPath) {
        Map<String, Integer> occurrences = new HashMap<>();
        for (PathNode node : currentPath) {
            if (node.to.isSmallCave) {
                if (!occurrences.containsKey(node.to.name)) {
                    occurrences.put(node.to.name, 1);
                } else {
                    occurrences.put(node.to.name, occurrences.get(node.to.name) + 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            if (entry.getValue() >= 2) {
                return true;
            }
        }
        return false;
    }


    private void findAllPaths(final List<PathNode> allNodes) {
        final List<PathNode> startNodes = findStartPaths(allNodes);
        for (final PathNode startNode : startNodes) {
            findPath(Collections.singletonList(startNode), allNodes);
        }
    }

    private void findPath(final List<PathNode> currentPath, final List<PathNode> allNodes) {
        final PathNode lastPathNode = currentPath.get(currentPath.size() - 1);
        final List<PathNode> possibleNextNodes = getPossibleNextPathNodes(allNodes, lastPathNode.to, currentPath);
        if (possibleNextNodes.isEmpty()) {
            return;
        }
        for (final PathNode nextNode : possibleNextNodes) {
            final ArrayList<PathNode> pathNodes = new ArrayList<>(currentPath);
            pathNodes.add(nextNode);
            if (nextNode.to.name.equals("end")) {
                allPaths.add(pathNodes);
            } else {
                findPath(pathNodes, allNodes);
            }
        }
    }

    private List<PathNode> getPossibleNextPathNodes(final List<PathNode> allNodes,
                                                    final Node from,
                                                    final List<PathNode> currentPath) {
        List<PathNode> allAvailablePaths = findAllAvailablePaths(from, allNodes);
        List<PathNode> possiblePaths = allAvailablePaths
                .stream()
                .filter(a -> {
                    if (a.to.isSmallCave) {
                        for (PathNode pn : currentPath) {
                            if (pn.from.name.equals(a.to.name)) {
                                return false;
                            }
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        return possiblePaths;
    }

    private List<PathNode> findAllAvailablePaths(final Node from, final List<PathNode> pathNodes) {
        return pathNodes
                .stream()
                .filter(n -> n.from.name.equals(from.name))
                .collect(Collectors.toList());
    }

    private List<PathNode> findStartPaths(final List<PathNode> pathNodes) {
        return pathNodes
                .stream()
                .filter(p -> p.from.name.equals("start"))
                .collect(Collectors.toList());
    }

    private List<PathNode> parsePathNodes(final List<String> lines) {
        final List<PathNode> pathNodes = new ArrayList<>();
        for (String l : lines) {
            String[] split = l.split("-");
            Node from = new Node(split[0], Character.isLowerCase(split[0].charAt(0)));
            Node to = new Node(split[1], Character.isLowerCase(split[1].charAt(0)));
            pathNodes.add(new PathNode(from, to));
            pathNodes.add(new PathNode(to, from));
        }
        return pathNodes;
    }

    class PathNode {
        Node from;
        Node to;

        public PathNode(Node from, Node to) {
            this.from = from;
            this.to = to;
        }
    }

    class Node {
        String name;
        boolean isSmallCave;

        public Node(String name, boolean isSmallCave) {
            this.name = name;
            this.isSmallCave = isSmallCave;
        }
    }
}
