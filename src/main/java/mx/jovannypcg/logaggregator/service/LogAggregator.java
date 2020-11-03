package mx.jovannypcg.logaggregator.service;

import mx.jovannypcg.logaggregator.domain.LogFileHolder;
import mx.jovannypcg.logaggregator.domain.comparator.LogFileHolderComparator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Aggregates a bunch of log files; contains the necessary domain logic
 * to sort the logs by timestamp and provides a single output for all the
 * concerned log files.
 */
@Service
public class LogAggregator {
    private boolean fileExists(File file) {
        return file.isFile() && file.exists();
    }

    /**
     * Returns an instance of {@link TreeSet}, a data structure able to sort its elements given a comparator.
     * The set holds {@link LogFileHolder} instances whose sort method is the timestamp provided by the
     * {@link LogFileHolder#log} attribute.
     *
     * First off, every file passed in as argument is converted into an instance of {@link LogFileHolder}, which
     * internally converts the given file into a {@link java.io.RandomAccessFile} to avoid loading the whole file
     * into memory and keeps track of the read log using a pointer.
     *
     * Then, before adding the {@link LogFileHolder} to the set, each {@link LogFileHolder} reads the first log of
     * their respective file, so that the {@link TreeSet} knows how to sort its items.
     *
     * @param files
     * @return TreeSet instance whose items are {@link LogFileHolder} with a {@link RandomAccessFile} and a
     *         String, representing the first log of the file. The TreeSet instance ensures log(n) time
     *         cost for operations like adding and removing, as its items are sorted.
     */
    TreeSet<LogFileHolder> buildLogFileHolders(File[] files) {
        TreeSet<LogFileHolder> logFileHolders = new TreeSet<>(new LogFileHolderComparator());

        Arrays.stream(files)
                .filter(this::fileExists)
                .map(LogFileHolder::with)
                .peek(LogFileHolder::readLog)
                .filter(LogFileHolder::isLogAvailable)
                .forEach(logFileHolders::add);

        return logFileHolders;
    }

    /**
     * @see #aggregate(File[], PrintStream)
     * @param files Files containing logs to be aggregated.
     */
    public void aggregate(File[] files) {
        aggregate(files, System.out);
    }

    /**
     * Aggregates the given files into a single outcome that is sorted by the timestamp of each log.
     *
     * Each log file is converted into {@link LogFileHolder} and inserted into a {@link TreeSet} to ensure the
     * current logs of each file are sorted based on their timestamp.
     *
     * The first item of the {@link TreeSet} set is grabbed and its log is printed out. Then the next log
     * of the {@link java.io.RandomAccessFile} instance is read and put into the {@code TreeSet} in order to
     * keep the correct order.
     *
     * @param files Files containing logs to be aggregated.
     * @param output Stream printer to set the aggregation outcome.
     */
    void aggregate(File[] files, PrintStream output) {
        TreeSet<LogFileHolder> logFileHolders = buildLogFileHolders(files);

        while (!logFileHolders.isEmpty()) {
            var logFileHolder = logFileHolders.pollFirst();
            var currentLog = logFileHolder.getLog();

            output.println(currentLog);
            var nextLog = logFileHolder.readLog();

            if (!Objects.isNull(nextLog))
                logFileHolders.add(logFileHolder);
            else
                logFileHolder.closeFile();
        }
    }
}
