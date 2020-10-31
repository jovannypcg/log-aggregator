package mx.jovannypcg.logaggregator.domain.comparator;

import mx.jovannypcg.logaggregator.domain.LogFileHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class LogFileHolderComparatorTests {
    private Comparator<LogFileHolder> comparator;
    private LogFileHolder logFileHolderOne;
    private LogFileHolder logFileHolderTwo;

    private File getResourceFile(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }

    private void initLogFiles() {
        var logFileOne = getResourceFile("fixtures/one.log");
        var logFileTwo = getResourceFile("fixtures/two.log");

        logFileHolderOne = LogFileHolder.with(logFileOne);
        logFileHolderOne.readLog();

        logFileHolderTwo = LogFileHolder.with(logFileTwo);
        logFileHolderTwo.readLog();
    }

    @BeforeEach
    public void init() {
        initLogFiles();

        comparator = new LogFileHolderComparator();
    }

    @Test
    public void compare_shouldReturnZeroDueToSameLogs() {
        var comparison = comparator.compare(logFileHolderOne, logFileHolderOne);

        assertThat(comparison).isZero();
    }

    @Test
    public void compare_shouldReturnPositiveValueDueToLogInFirstHolderIsGreaterThanSecond() {
        var comparison = comparator.compare(logFileHolderOne, logFileHolderTwo);

        assertThat(comparison).isPositive();
    }

    @Test
    public void compare_shouldReturnNegativeValueDueToLogInFirstHolderIsLessThanSecond() {
        var comparison = comparator.compare(logFileHolderTwo, logFileHolderOne);

        assertThat(comparison).isNegative();
    }
}
