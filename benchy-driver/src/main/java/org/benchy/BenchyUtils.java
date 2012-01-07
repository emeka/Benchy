package org.benchy;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Contains various benchy convenience methods.
 *
 * @author Peter Veentjer.
 */
public class BenchyUtils {

    /**
     * Calculates the number of operations/second.
     *
     * @param operations operation count.
     * @param durationMs the duration in milliseconds
     * @return the calculated operations/second
     */
    public static String operationsPerSecondAsString(long operations, long durationMs) {
        double performance = (1000d * operations) / durationMs;
        return format(performance);
    }

    /**
     * Calculates the number of operations/second/threads.
     *
     * @param operationsPerThread the number of operations per thread
     * @param totalTimeMs the total duration of all threads in milliseconds.
     * @param threads the number of threads.
     * @return the calculated operations/second/thread.
     */
    public static double operationsPerSecondPerThread(long operationsPerThread, long totalTimeMs, int threads) {
        long totalTransactions = operationsPerThread * threads;
        return (1000d * totalTransactions) / totalTimeMs;
    }


    /**
     * Calculates the number of operations/second/thread and formats the result as a String.
     *
     * @param operationsPerThread the number of operations per thread.
     * @param totalDurationMs     the total duration of all threads in milliseconds.
     * @param threads             the number of threads
     * @return the calculated operations/second/thread and formatted as String.
     */
    public static String operationsPerSecondPerThreadAsString(long operationsPerThread, long totalDurationMs, int threads) {
        double transactionsPerSecondPerThread = operationsPerSecondPerThread(operationsPerThread, totalDurationMs, threads);
        return format(transactionsPerSecondPerThread);
    }

    /**
     * Calculates the number of operations/second.
     *
     * @param operationsPerThread the number of operations per thread.
     * @param totalTimeMs the total duration of all threads in milliseconds.
     * @param threads the number of threads.
     * @return the calculated operations/second.
     */
    public static double operationsPerSecond(long operationsPerThread, long totalTimeMs, int threads) {
        return threads * operationsPerSecondPerThread(operationsPerThread, totalTimeMs, threads);
    }

    /**
     * Calculates the number of operations/second and formats the result as a String.
     *
     * @param operationsPerThread the number of operations per thread.
     * @param totalTimeMs the total duration of all threads in milliseconds.
     * @param threads the number of threads.
     * @return the calculated operations/second and formatted as String.
     */
    public static String operationsPerSecondAsString(long operationsPerThread, long totalTimeMs, int threads) {
        return format(operationsPerSecond(operationsPerThread, totalTimeMs, threads));
    }

    /**
     * Formats a double.
     *
     * @param value the double value
     * @return the formatted double value.
     */
    public static String format(double value) {
        return NumberFormat.getInstance(Locale.US).format(value);
    }

    //we don't want instances.
    private BenchyUtils() {
    }
}
