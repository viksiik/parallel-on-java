import java.util.Arrays;

class Result {
    private final int[][] matrix;

    public Result(int rows, int cols) {
        this.matrix = new int[rows][cols];
    }

    public void setRow(int rowIndex, int[] rowData) {
        matrix[rowIndex] = Arrays.copyOf(rowData, rowData.length);
    }

    public int[][] getMatrix() {
        return matrix;
    }
}