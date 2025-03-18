import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MatrixMultiplier {
    public static Result multiplyParallel(int[][] A, int[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;

        Result result = new Result(rowsA, colsB);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < rowsA; i++) {
            final int row = i;
            executor.execute(() -> {
                int[] rowData = new int[colsB];
                for (int j = 0; j < colsB; j++) {
                    for (int k = 0; k < colsA; k++) {
                        rowData[j] += A[row][k] * B[k][j];
                    }
                }
                result.setRow(row, rowData);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Result multiplySequential(int[][] A, int[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;

        Result result = new Result(rowsA, colsB);

        for (int i = 0; i < rowsA; i++) {
            int[] rowData = new int[colsB];
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    rowData[j] += A[i][k] * B[k][j];
                }
            }
            result.setRow(i, rowData);
        }

        return result;
    }
}
