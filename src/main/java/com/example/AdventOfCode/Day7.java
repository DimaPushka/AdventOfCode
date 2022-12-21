package com.example.AdventOfCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day7 {
    private Directory root;
    private Directory AddDirectory;
    private final List<Directory> directories = new ArrayList<>();

    public int executePart1(Stream<String> input) {
        root = new Directory("/", null);

        directories.add(root);

        input.forEach(this::operationCommand);

        return directories.stream()
                .mapToInt(Directory::getSize)
                .filter(value -> value <= 100000)
                .sum();
    }

    public int executePart2(Stream<String> input) {
        root = new Directory("/", null);

        directories.add(root);

        input.forEach(this::operationCommand);

        int fullSize = root.getSize();

        int fullSpace = 70000000;

        int spaceAvailable = fullSpace - fullSize;

        int spaceNeeded = 30000000;

        int spaceToFree = spaceNeeded - spaceAvailable;

        return directories.stream()
                .mapToInt(Directory::getSize)
                .filter(value -> value >= spaceToFree)
                .min()
                .orElseThrow(() -> new AssertionError("boi!"));
    }

    private void operationCommand(String command) {
        String[] parts = command.split(" ");
        if (parts[0].equals("$")) {
            if (parts[1].equals("cd")) {
                switch (parts[2]) {
                    case "/" -> AddDirectory = root;
                    case ".." -> AddDirectory = AddDirectory.parent;
                    default -> AddDirectory = AddDirectory.children.get(parts[2]);
                }
            }
        } else {
            if (parts[0].equals("dir")) {
                Directory directory = new Directory(parts[1], AddDirectory);
                AddDirectory.children.put(parts[1], directory);
                directories.add(directory);
            } else {
                AddDirectory.children.put(parts[1],
                        new Directory(parts[1], AddDirectory, Integer.parseInt(parts[0])));
            }
        }
    }

    private static class Directory {
        private final String name;
        private final Map<String, Directory> children = new HashMap<>();
        private final Directory parent;
        private int size = 0;

        private Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
        }

        private Directory(String name, Directory parent, int size) {
            this.name = name;
            this.parent = parent;
            this.size = size;
        }

        public int getSize() {
            if (size == 0) {
                for (Directory child : children.values()) {
                    size += child.getSize();
                }
            }
            return size;
        }

        @Override
        public String toString() {
            return "Directory{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }
}
