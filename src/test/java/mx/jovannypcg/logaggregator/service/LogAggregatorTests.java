package mx.jovannypcg.logaggregator.service;

import mx.jovannypcg.logaggregator.domain.LogFileHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LogAggregatorTests {
    private File[] logFiles;
    private PrintStream printStream;
    private OutputStream output;

    private LogAggregator logAggregator;

    private File getResourceFile(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }

    @BeforeEach
    public void init() {
        logFiles = new File[]{
                getResourceFile("fixtures/one.log"),
                getResourceFile("fixtures/two.log"),
                getResourceFile("fixtures/three.log"),
                getResourceFile("fixtures/empty.log")};

        output = new ByteArrayOutputStream();
        printStream = new PrintStream(output);

        logAggregator = new LogAggregator();
    }

    @Test
    public void buildLogFileHolders_shouldReturnFileHoldersSortedByLogTimestamp() {
        var expectedLogs = List.of("2016-12-20T18:51:16Z, Server C started.",
                "2016-12-20T19:00:45Z, Server A started.",
                "2016-12-20T19:01:16Z, Server B started.");

        var logFileHolders = logAggregator.buildLogFileHolders(logFiles);
        var logs = logFileHolders
                .stream()
                .map(LogFileHolder::getLog)
                .collect(Collectors.toList());

        assertThat(logs)
                .isEqualTo(expectedLogs);
    }

    @Test
    public void aggregate_shouldPrintTheLogsSortedByTimestamp() {
        var expectedOutput =
                "2016-12-20T18:51:16Z, Server C started.\n" +
                "2016-12-20T19:00:45Z, Server A started.\n" +
                "2016-12-20T19:01:16Z, Server B started.\n" +
                "2016-12-20T19:01:25Z, Server A completed job.\n" +
                "2016-12-20T19:02:02Z, Server C completed job.\n" +
                "2016-12-20T19:02:48Z, Server A terminated.\n" +
                "2016-12-20T19:03:25Z, Server B completed job.\n" +
                "2016-12-20T19:04:50Z, Server B terminated.\n" +
                "2016-12-20T19:05:00Z, Server C terminated.\n" +
                "2016-12-20T19:59:15Z, Server D started.\n" +
                "2016-12-20T20:22:40Z, Server E started.\n" +
                "2016-12-20T20:25:11Z, Server D completed job.\n" +
                "2016-12-20T20:25:11Z, Server D terminated.\n" +
                "2016-12-20T20:26:02Z, Server E completed job.\n" +
                "2016-12-20T20:26:03Z, Server E terminated.\n";

        logAggregator.aggregate(logFiles, printStream);

        assertThat(output.toString())
                .isEqualTo(expectedOutput);
    }
}
