import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static class WordStats {
        long count;
        long sum;
        long sumSquares;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        Map<Integer, Long> lengthFrequency = new HashMap<>();

        void addWord(String word) {
            int len = word.length();
            count++;
            sum += len;
            sumSquares += len * len;
            min = Math.min(min, len);
            max = Math.max(max, len);
            lengthFrequency.put(len, lengthFrequency.getOrDefault(len, 0L) + 1);
        }

        void combine(WordStats other) {
            this.count += other.count;
            this.sum += other.sum;
            this.sumSquares += other.sumSquares;
            this.min = Math.min(this.min, other.min);
            this.max = Math.max(this.max, other.max);
            other.lengthFrequency.forEach((k, v) ->
                    lengthFrequency.put(k, lengthFrequency.getOrDefault(k, 0L) + v)
            );
        }

        double getMean() {
            return count == 0 ? 0 : (double) sum / count;
        }

        double getVariance() {
            if (count == 0) return 0;
            double mean = getMean();
            return ((double) sumSquares / count) - (mean * mean);
        }

        @Override
        public String toString() {
            return String.format("Words number: %d\nMin: %d\nMax: %d\nMean: %.2f\nVariance: %.2f",
                    count, min, max, getMean(), getVariance());
        }

        public void printHistogram() {
            StringBuilder histogram = new StringBuilder();
            for (int i = min; i <= max; i++) {
                long frequency = lengthFrequency.getOrDefault(i, 0L);
                histogram.append(String.format("[%d]=%d, ", i, frequency));
            }

            System.out.println("Word Length Frequency Histogram:");
            System.out.println(histogram.toString().replaceAll(", $", ""));
        }
    }

    static class WordStatsTask extends RecursiveTask<WordStats> {
        private static final int THRESHOLD = 1000;
        private final List<String> words;
        private final int start, end;

        public WordStatsTask(List<String> words, int start, int end) {
            this.words = words;
            this.start = start;
            this.end = end;
        }

        @Override
        protected WordStats compute() {
            if (end - start <= THRESHOLD) {
                WordStats stats = new WordStats();
                for (int i = start; i < end; i++) {
                    stats.addWord(words.get(i));
                }
                return stats;
            } else {
                int mid = (start + end) / 2;
                WordStatsTask left = new WordStatsTask(words, start, mid);
                WordStatsTask right = new WordStatsTask(words, mid, end);
                left.fork();
                WordStats rightResult = right.compute();
                WordStats leftResult = left.join();
                leftResult.combine(rightResult);
                return leftResult;
            }
        }
    }

    public static void main(String[] args) {
        int threads = 6;

        List<String> filePaths = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            filePaths.add(String.format("D:\\kpi\\parallel-on-java\\lab4\\food\\food_%d.txt", i));
        }

        StringBuilder sb = new StringBuilder();
        for (String path : filePaths) {
            try {
                sb.append(Files.readString(Paths.get(path))).append("\n");
            } catch (IOException e) {
                System.err.println("Error reading file: " + path);
            }
        }
        String text = sb.toString();

        List<String> words = Arrays.stream(text.split("[^\\p{L}]+"))
                .filter(w -> !w.isEmpty())
                .collect(Collectors.toList());

        long startSequential = System.nanoTime();
        WordStats sequentialStats = new WordStats();
        for (String word : words) {
            sequentialStats.addWord(word);
        }
        long endSequential = System.nanoTime();
        long timeSequential = endSequential - startSequential;

        ForkJoinPool pool = new ForkJoinPool(threads);
        WordStats parallelStatsexp = pool.invoke(new WordStatsTask(words, 0, words.size()));

        long startParallel = System.nanoTime();
        WordStats parallelStats = pool.invoke(new WordStatsTask(words, 0, words.size()));
        long endParallel = System.nanoTime();
        long timeParallel = endParallel - startParallel;

        System.out.println("----- Sequential Algorithm -----");
        System.out.println(sequentialStats);
        sequentialStats.printHistogram();
        System.out.printf("Execution time: %.2f ms\n", timeSequential / 1e6);

        System.out.println("\n----- Parallel Algorithm -----");
        System.out.println(parallelStats);
        parallelStats.printHistogram();
        System.out.printf("Execution time: %.2f ms\n", timeParallel / 1e6);

        double speedup = (double) timeSequential / timeParallel;
        System.out.printf("\nSpeedup: %.2f\n", speedup);
        double efficiency = speedup / threads;
        System.out.printf("Efficiency: %.2f\n", efficiency);
    }
}
