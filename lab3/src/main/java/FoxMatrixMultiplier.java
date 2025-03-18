import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class FoxMatrixMultiplier {
    public static int[][] foxSequential(int[][] A, int[][] B, int blockSize) {
        int n = A.length;
        int[][] C = new int[n][n];

        for (int stage = 0; stage < n / blockSize; stage++) {
            for (int i = 0; i < n; i += blockSize) {
                for (int j = 0; j < n; j += blockSize) {
                    for (int k = 0; k < n; k += blockSize) {
                        multiplyBlock(A, B, C, i, j, k, blockSize);
                    }
                }
            }
        }
        return C;
    }

    public static int[][] foxParallel(int[][] A, int[][] B, int blockSize) {
        int n = A.length;
        int[][] C = new int[n][n];
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int stage = 0; stage < n / blockSize; stage++) {
            for (int i = 0; i < n; i += blockSize) {
                for (int j = 0; j < n; j += blockSize) {
                    final int row = i, col = j;
                    executor.execute(() -> {
                        for (int k = 0; k < n; k += blockSize) {
                            multiplyBlock(A, B, C, row, col, k, blockSize);
                        }
                    });
                }
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return C;
    }

    private static void multiplyBlock(int[][] A, int[][] B, int[][] C, int row, int col, int k, int blockSize) {
        for (int i = row; i < row + blockSize; i++) {
            for (int j = col; j < col + blockSize; j++) {
                for (int l = k; l < k + blockSize; l++) {
                    C[i][j] += A[i][l] * B[l][j];
                }
            }
        }
    }
}
