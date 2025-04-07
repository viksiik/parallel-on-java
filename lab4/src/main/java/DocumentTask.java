import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class DocumentTask extends RecursiveTask<Map<String, Set<String>>> {
    private final Path filePath;

    public DocumentTask(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    protected Map<String, Set<String>> compute() {
        Map<String, Set<String>> wordMap = new HashMap<>();
        try {
            String content = Files.readString(filePath);
            String fileName = filePath.getFileName().toString();
            Arrays.stream(content.toLowerCase().split("[^\\p{L}]+"))
                    .filter(s -> !s.isEmpty())
                    .forEach(word -> {
                        wordMap.computeIfAbsent(word, k -> new HashSet<>()).add(fileName);
                    });
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return wordMap;
    }
}
