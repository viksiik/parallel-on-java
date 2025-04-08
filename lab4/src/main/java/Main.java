import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String directory = "D:\\kpi\\parallel-on-java\\lab4\\food";
        try {
            List<Path> files = Files.list(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .collect(Collectors.toList());

            if (files.isEmpty()) {
                System.out.println("No .txt files found in the directory.");
                return;
            }

            ForkJoinPool pool = new ForkJoinPool(6);

            long start = System.nanoTime();
            CommonWordsTask task = new CommonWordsTask(files);
            Map<String, Set<String>> wordToFiles = pool.invoke(task);
            long end = System.nanoTime();

            System.out.println("\nSimilar words in two or more files:");
            wordToFiles.entrySet().stream()
                    .filter(e -> e.getValue().size() >= 2)
                    .forEach(e -> {
                        System.out.println(e.getKey() + " -> " + e.getValue());
                    });

            long totalFiles = files.size();
            System.out.println("\nSimilar words in all files:");
            wordToFiles.entrySet().stream()
                    .filter(e -> e.getValue().size() == totalFiles)
                    .map(Map.Entry::getKey)
                    .forEach(System.out::println);

            System.out.printf("Execution time: %.2f ms\n", (end - start) / 1e6);
        } catch (IOException e) {
            System.err.println("Error reading files from directory: " + e.getMessage());
        }
    }
}
