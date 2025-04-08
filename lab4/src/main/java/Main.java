import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String directory = "D:\\kpi\\parallel-on-java\\lab4\\technologie"; // заміни на свій шлях

        try {
            List<Path> files = Files.list(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .collect(Collectors.toList());

            if (files.isEmpty()) {
                System.out.println("No .txt files found in the directory.");
                return;
            }

            ForkJoinPool pool = new ForkJoinPool();

            long start = System.nanoTime();

            List<DocumentSearchTask> tasks = files.stream()
                    .map(file -> new DocumentSearchTask(file))
                    .collect(Collectors.toList());

            List<Future<Map<String, Set<String>>>> futures = tasks.stream()
                    .map(pool::submit)
                    .collect(Collectors.toList());

            int matchingFiles = 0;

            System.out.println("Documents and matched IT keywords:");
            for (Future<Map<String, Set<String>>> future : futures) {
                Map<String, Set<String>> result = future.get();
                if (!result.isEmpty()) {
                    matchingFiles++;
                    result.forEach((file, words) ->
                            System.out.println(file + " => " + words));
                }
            }

            long end = System.nanoTime();

            System.out.println("\nTotal files: " + files.size());
            System.out.println("Files matching keywords: " + matchingFiles);
            System.out.printf("Execution time: %.2f ms\n", (end - start) / 1e6);

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
