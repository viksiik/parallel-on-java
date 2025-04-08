import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class DocumentSearchTask extends RecursiveTask<Map<String, Set<String>>> {
    private static final Set<String> keywords = Set.of(
            "algorithm", "database", "security",
            "java", "cloud", "ai", "machine", "software"
    );

    private final List<Path> files;

    public DocumentSearchTask(List<Path> files) {
        this.files = files;
    }

    @Override
    protected Map<String, Set<String>> compute() {
        if (files.size() == 1) {
            return processFile(files.get(0));
        }

        int mid = files.size() / 2;
        DocumentSearchTask leftTask = new DocumentSearchTask(files.subList(0, mid));
        DocumentSearchTask rightTask = new DocumentSearchTask(files.subList(mid, files.size()));

        leftTask.fork();
        Map<String, Set<String>> rightResult = rightTask.compute();
        Map<String, Set<String>> leftResult = leftTask.join();

        return mergeResults(leftResult, rightResult);
    }

    private Map<String, Set<String>> processFile(Path file) {
        Map<String, Set<String>> result = new HashMap<>();
        try {
            String content = Files.readString(file).toLowerCase();
            Set<String> foundWords = Arrays.stream(content.split("[^\\p{L}]+"))
                    .filter(keywords::contains)
                    .collect(Collectors.toSet());
            if (!foundWords.isEmpty()) {
                result.put(file.getFileName().toString(), foundWords);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file);
        }
        return result;
    }

    private Map<String, Set<String>> mergeResults(Map<String, Set<String>> left, Map<String, Set<String>> right) {
        Map<String, Set<String>> merged = new HashMap<>(left);
        right.forEach((file, words) -> merged.merge(file, words, (existing, newWords) -> {
            existing.addAll(newWords);
            return existing;
        }));
        return merged;
    }
}
