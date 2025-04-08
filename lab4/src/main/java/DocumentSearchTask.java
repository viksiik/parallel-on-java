import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class DocumentSearchTask extends RecursiveTask<Map<String, Set<String>>> {
    private static final Set<String> keywords = Set.of(
            "algorithm", "database", "security", "encryption",
            "java", "cloud", "ai", "machine", "software"
    );

    private final Path file;

    public DocumentSearchTask(Path file) {
        this.file = file;
    }

    @Override
    protected Map<String, Set<String>> compute() {
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
}