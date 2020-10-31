package mx.jovannypcg.logaggregator.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * Holds a reference to a log file and a String representing the current log.
 *
 * Internally, {@link RandomAccessFile} keeps track of the pointer to read the
 * file in an efficient way.
 */
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

    /**
     * Reads one line from the {@link RandomAccessFile} instance and sets it
     * to the {@code log} attribute.
     * @return Log read from the file.
     */
    @SneakyThrows
    public String readLog() {
        log = randomAccessFile.readLine();
        return log;
    }

    /**
     * Verifies if the current log is not null;
     * @return true if the current log is not null, false otherwise.
     */
    public boolean isLogAvailable() {
        return !Objects.isNull(log);
    }

    /**
     * Creates an instance of this class by assinging {@code randomAccessFile} an instance
     * based on the given file.
     *
     * @param file File to create the {@link RandomAccessFile} instance of this class.
     * @return Instance of this class with {@code randomAccessFile} created based on the given {@link File}.
     */
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
