package mx.jovannypcg.logaggregator.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Objects;

@Getter
public class LogFileHolder {
    /**
     * Reference to the file to read logs from.
     */
    @NonNull
    private RandomAccessFile randomAccessFile;

    /**
     * Current log of the concerned file.
     */
    private String log;

    private LogFileHolder() {}
    private LogFileHolder (RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    @SneakyThrows
    public String readLog() {
        log = randomAccessFile.readLine();
        return log;
    }

    public boolean isLogAvailable() {
        return !Objects.isNull(log);
    }

    @SneakyThrows
    public static LogFileHolder with(File file) {
        var randomAccessFile = new RandomAccessFile(file, "r");
        return new LogFileHolder(randomAccessFile);
    }

    @SneakyThrows
    public void closeFile() {
        randomAccessFile.close();
    }
}
