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
        int[] sizes = {200, 500, 1000, 1500, 2000};
        for (int size : sizes) {
            System.out.println("\nMatrix size: " + size + "x" + size);
            System.out.println("Generating matrices...");
            int[][] A = generateMatrix(size);
            int[][] B = generateMatrix(size);
            System.out.println("Matrices generated.");

            long startTime, endTime;

            System.out.println("Sequential multiplication:");
            startTime = System.nanoTime();
            Result resultSeq = MatrixMultiplier.multiplySequential(A, B);
            endTime = System.nanoTime();
            double timeSeq = (endTime - startTime) / 1e6;
            System.out.println("Time: " + timeSeq + " ms");

            System.out.println("Parallel multiplication:");
            startTime = System.nanoTime();
            Result resultParallel = MatrixMultiplier.multiplyParallel(A, B);
            endTime = System.nanoTime();
            double timeParallel = (endTime - startTime) / 1e6;
            System.out.println("Time: " + timeParallel + " ms");

            double speedup = timeSeq / timeParallel;
            System.out.println("Speedup: " + String.format("%.2f", speedup));
        }
    }
}