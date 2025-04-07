import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class CommonWordsTask extends RecursiveTask<Map<String, Set<String>>> {
    private final List<Path> files;

    public CommonWordsTask(List<Path> files) {
        this.files = files;
    }

    @Override
    protected Map<String, Set<String>> compute() {
        if (files.size() == 1) {
            return new DocumentTask(files.get(0)).fork().join();
        } else if (files.size() == 2) {
            DocumentTask task1 = new DocumentTask(files.get(0));
            DocumentTask task2 = new DocumentTask(files.get(1));
            task1.fork();
            Map<String, Set<String>> map2 = task2.compute();
            Map<String, Set<String>> map1 = task1.join();
            return mergeMaps(map1, map2);
        } else {
            int mid = files.size() / 2;
            CommonWordsTask left = new CommonWordsTask(files.subList(0, mid));
            CommonWordsTask right = new CommonWordsTask(files.subList(mid, files.size()));
            left.fork();
            Map<String, Set<String>> rightMap = right.compute();
            Map<String, Set<String>> leftMap = left.join();
            return mergeMaps(leftMap, rightMap);
        }
    }

    private Map<String, Set<String>> mergeMaps(Map<String, Set<String>> map1, Map<String, Set<String>> map2) {
        for (Map.Entry<String, Set<String>> entry : map2.entrySet()) {
            map1.merge(entry.getKey(), entry.getValue(), (set1, set2) -> {
                set1.addAll(set2);
                return set1;
            });
        }
        return map1;
    }
}
