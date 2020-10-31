package mx.jovannypcg.logaggregator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.RandomAccessFile;

@Data
@AllArgsConstructor
public class LogFileHolder {
    /**
     * Reference to the file to read logs from.
     */
    private RandomAccessFile randomAccessFile;

    /**
     * Current log of the concerned file.
     */
    private String log;

    /**
     * Position of the next log in the file.
     */
    private long position;
}
