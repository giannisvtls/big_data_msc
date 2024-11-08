import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MatrixVectorMultiplicationHandler extends Thread {
    private static final Logger logger = Logger.getLogger(MatrixVectorMultiplicationHandler.class.getName());

    private final int lo, hi;
    private final int[][] matrix;
    private final int[] vector;
    private final int[] result;

    // Helper function that sets default values to env vars that are not configured
    private static int getEnvVarOrDefault(String envVar, int defaultValue) {
        String value = System.getenv(envVar);
        if (value != null && Objects.equals(envVar, "ROWS") && !isPowerOfTwo(Integer.parseInt(value))) {
            logger.log(Level.WARNING,"Environment variable \"" + envVar + "\" is not a power of 2, using default value: " + defaultValue);
            return defaultValue;
        }
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING,"Environment variable \"" + envVar + "\" not set, using default value: " + defaultValue);
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    private static boolean isPowerOfTwo(int number) {
        return (number > 0) && ((number & (number - 1)) == 0);
    }

    private static final int n = getEnvVarOrDefault("ROWS", 1024);         // Max number of new cases
    private static final int m = getEnvVarOrDefault("COLUMNS", 4000);         //

    public MatrixVectorMultiplicationHandler(int[][] matrix, int[] vector, int[] result, int lo, int hi) {
        this.matrix = matrix;
        this.vector = vector;
        this.result = result;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    public void run() {
        for (int row = lo; row < hi; row++) {
            result[row] = multiplyRowByVector(matrix[row], vector);
        }
    }

    // Helper method to calculate the dot product of a matrix row and a vector
    private int multiplyRowByVector(int[] row, int[] vector) {
        int sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += row[i] * vector[i];
        }
        return sum;
    }



    public static int[] multiplyMatrixVector(int[][] matrix, int[] vector, int numThreads) throws InterruptedException {
        int n = matrix.length;
        int[] result = new int[n];

        // Check if matrix and vector dimensions are compatible
        if (matrix[0].length != vector.length) {
            throw new IllegalArgumentException("Matrix columns must match vector rows.");
        }

        // Measure the start time
        long startTime = System.nanoTime();

        // Create and start threads
        MatrixVectorMultiplicationHandler[] threads = new MatrixVectorMultiplicationHandler[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int lo = (i * n) / numThreads;
            int hi = ((i + 1) * n) / numThreads;
            threads[i] = new MatrixVectorMultiplicationHandler(matrix, vector, result, lo, hi);
            threads[i].start();
        }

        // Wait for the threads to finish
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        // Measuring the endtime
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        double milliSeconds = totalTime / 1_000_000.0;

        System.out.println("Time taken with " + numThreads + " threads: " + milliSeconds + "ms");

        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        int[] threadCounts = {1, 2, 4, 8};  // Different thread counts to test

        int[][] matrix = new int[n][m];
        int[] vector = new int[m];

        // Initialize matrix and vector with random values between 0 and 10
        MatrixVectorMultiplicationHandler.initializeMatrix(matrix);
        MatrixVectorMultiplicationHandler.initializeVector(vector);

        // Test matrix-vector multiplication with different numbers of threads
        for (int numThreads : threadCounts) {
            System.out.println("\nRunning with " + numThreads + " threads:");
            int[] result = MatrixVectorMultiplicationHandler.multiplyMatrixVector(matrix, vector, numThreads);

            System.out.println("Result of A * v:");
            MatrixVectorMultiplicationHandler.printVector(result);
        }
    }

    // Method to initialize matrix with random values between 0 and 10
    static void initializeMatrix(int[][] matrix) {
        Random rand = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = rand.nextInt(11);
            }
        }
    }

    // Method to initialize vector with random values between 0 and 10
    static void initializeVector(int[] vector) {
        Random rand = new Random();
        for (int i = 0; i < vector.length; i++) {
            vector[i] = rand.nextInt(11);
        }
    }

    // Print the initialized vector
    static void printVector(int[] vector) {
        for (int value : vector) {
            System.out.print(value + "\t");
        }
        System.out.println();
    }
}
