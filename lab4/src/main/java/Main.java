import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String directory = "D:\\kpi\\parallel-on-java\\technologie";

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

            DocumentSearchTask task = new DocumentSearchTask(files);

            Map<String, Set<String>> result = pool.invoke(task);

            long end = System.nanoTime();

            int matchingFiles = (int) result.values().stream().filter(words -> !words.isEmpty()).count();

            System.out.println("Documents and matched IT keywords:");
            result.forEach((file, words) -> {
                if (!words.isEmpty()) {
                    System.out.println(file + " => " + words);
                }
            });

            System.out.println("\nTotal files: " + files.size());
            System.out.println("Files matching keywords: " + matchingFiles);
            System.out.printf("Execution time: %.2f ms\n", (end - start) / 1e6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
