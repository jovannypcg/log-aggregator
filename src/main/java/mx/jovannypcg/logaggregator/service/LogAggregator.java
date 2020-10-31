package mx.jovannypcg.logaggregator.service;

import mx.jovannypcg.logaggregator.domain.LogFileHolder;
import mx.jovannypcg.logaggregator.domain.comparator.LogFileHolderComparator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

@Service
public class LogAggregator {
    private boolean fileExists(File file) {
        return file.isFile() && file.exists();
    }

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

    void aggregate(File[] files, PrintStream output) {
        TreeSet<LogFileHolder> logFileHolders = buildLogFileHolders(files);

        while (!logFileHolders.isEmpty()) {
            var logFileHolder = logFileHolders.pollFirst();
            var currentLog = logFileHolder.getLog();

            output.println(currentLog);
            var nextLog = logFileHolder.readLog();

            if (!Objects.isNull(nextLog))
                logFileHolders.add(logFileHolder);
        }
    }

    public void aggregate(File[] files) {
        aggregate(files, System.out);
    }
}
