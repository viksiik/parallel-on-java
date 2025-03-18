import java.util.Random;

public class Main {
    public static int[][] generateMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        int size = 1000;
        int blockSize = 100;
        int[] threadCounts = {1, 2, 4, 8, 16};

        System.out.println("Generating matrices...");
        int[][] A = generateMatrix(size);
        int[][] B = generateMatrix(size);
        System.out.println("Matrices generated.");

        System.out.println("Sequential Fox multiplication:");
        long startTime = System.nanoTime();
        int[][] resultSeq = FoxMatrixExperiment.foxSequential(A, B, blockSize);
        long endTime = System.nanoTime();
        double timeSeq = (endTime - startTime) / 1e6;
        System.out.println("Time: " + timeSeq + " ms\n");

        for (int threads : threadCounts) {
            System.out.println("Parallel Fox multiplication with " + threads + " threads:");
            startTime = System.nanoTime();
            int[][] resultParallel = FoxMatrixExperiment.foxParallel(A, B, blockSize, threads);
            endTime = System.nanoTime();
            double timeParallel = (endTime - startTime) / 1e6;
            double speedup = timeSeq / timeParallel;
            System.out.println("Time: " + timeParallel + " ms");
            System.out.println("Speedup: " + String.format("%.2f", speedup) + "\n");
        }
    }
}
