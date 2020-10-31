package mx.jovannypcg.logaggregator.service;

import lombok.SneakyThrows;
import mx.jovannypcg.logaggregator.domain.LogFileHolder;
import mx.jovannypcg.logaggregator.domain.comparator.LogFileHolderComparator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

@Service
public class LogAggregator {
    @SneakyThrows
    private RandomAccessFile toRandomAccessFile(File file) {
        return new RandomAccessFile(file, "r");
    }

    @SneakyThrows
    private LogFileHolder toLogFileHolder(RandomAccessFile randomAccessFile) {
        var log = randomAccessFile.readLine();
        var position = randomAccessFile.getFilePointer();

        return Objects.isNull(log) ? null : new LogFileHolder(randomAccessFile, log, position);
    }

    TreeSet<LogFileHolder> buildLogFileHoldersSet(File[] files) {
        TreeSet<LogFileHolder> logFileHolders = new TreeSet<>(new LogFileHolderComparator());

        Arrays.stream(files)
                .map(this::toRandomAccessFile)
                .map(this::toLogFileHolder)
                .filter(logFileHolder -> !Objects.isNull(logFileHolder))
                .forEach(logFileHolders::add);

        return logFileHolders;
    }

    public void aggregate(File[] files) throws IOException {
        TreeSet<LogFileHolder> logFileHolders = buildLogFileHoldersSet(files);

        while (!logFileHolders.isEmpty()) {
            var logFileHolder = logFileHolders.pollFirst();
            var currentLog = logFileHolder.getLog();
            var currentRandomAccessFile = logFileHolder.getRandomAccessFile();

            System.out.println(currentLog);

            var nextLog = currentRandomAccessFile.readLine();

            if (!Objects.isNull(nextLog)) {
                logFileHolder.setLog(nextLog);
                logFileHolder.setPosition(currentRandomAccessFile.getFilePointer());

                logFileHolders.add(logFileHolder);
            }
        }
    }
}
