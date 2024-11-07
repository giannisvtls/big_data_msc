
package oss1;

import java.util.Random;


public class MatrixVectorMultiplication extends Thread {
    private int lo, hi;
    private int[][] matrix;
    private int[] vector;
    private int[] result;

    public MatrixVectorMultiplication(int[][] matrix, int[] vector, int[] result, int lo, int hi) {
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

        // Measure the start time
        long startTime = System.nanoTime();

        // Create and start threads
        MatrixVectorMultiplication[] threads = new MatrixVectorMultiplication[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int lo = (i * n) / numThreads;
            int hi = ((i + 1) * n) / numThreads;
            threads[i] = new MatrixVectorMultiplication(matrix, vector, result, lo, hi);
            threads[i].start();
        }

        // Wait for the threads to finish
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
            //System.out.println("thread"+threads[i]+"joined");
        }

        // Measuring the endtime
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        double milliSeconds = totalTime / 1_000_000.0;

        System.out.println("Time taken with " + numThreads + " threads: " + milliSeconds + "ms");

        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 512;  // Number of rows, always 2^x
        int m = 3000;   // Number of columns
        int[] threadCounts = {1, 2, 4, 8};  // Different thread counts to test

        int[][] matrix = new int[n][m];
        int[] vector = new int[m];

        // Initialize matrix and vector with random values between 0 and 10
        initializeMatrix(matrix);
        initializeVector(vector);

        // Print matrix and vector for reference
        System.out.println("Matrix A:");
        //printMatrix(matrix); //commented for large matrices
        System.out.println("Vector v:");
        //printVector(vector);

        // Test matrix-vector multiplication with different numbers of threads
        for (int numThreads : threadCounts) {
            System.out.println("\nRunning with " + numThreads + " threads:");
            int[] result = multiplyMatrixVector(matrix, vector, numThreads);

            // Print the result
            //System.out.println("Result of A * v:");
            //printVector(result);
        }
    }

    // Method to initialize matrix with random values between 0 and 10
    private static void initializeMatrix(int[][] matrix) {
        Random rand = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = rand.nextInt(11);
            }
        }
    }

    // Method to initialize vector with random values between 0 and 10
    private static void initializeVector(int[] vector) {
        Random rand = new Random();
        for (int i = 0; i < vector.length; i++) {
            vector[i] = rand.nextInt(11);
        }
    }

    // print the initialized matrix
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    // Print the initialized vector
    private static void printVector(int[] vector) {
        for (int value : vector) {
            System.out.print(value + "\t");
        }
        System.out.println();
    }
}

