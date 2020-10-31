package mx.jovannypcg.logaggregator;

import mx.jovannypcg.logaggregator.service.LogAggregator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class LogAggregatorApplication implements CommandLineRunner {
    private final LogAggregator logAggregator;

    public LogAggregatorApplication(LogAggregator logAggregator) {
        this.logAggregator = logAggregator;
    }

    public static void main(String[] args) {
        SpringApplication.run(LogAggregatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length == 0)
            throw new IllegalArgumentException("Usage: java -jar log-aggregator.jar <log-dir>");

        var logDirectory = new File(args[0]);

        if (!logDirectory.exists() || !logDirectory.isDirectory())
            throw new IllegalArgumentException("<log-dir> must be a directory containing log files");

        logAggregator.aggregate(logDirectory.listFiles());
    }
}
