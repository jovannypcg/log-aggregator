package mx.jovannypcg.logaggregator.domain.comparator;

import mx.jovannypcg.logaggregator.domain.LogFileHolder;

import java.util.Comparator;

public class LogFileHolderComparator implements Comparator<LogFileHolder> {
    public static final String LOG_SEPARATOR = ",";

    private String getTimestamp(String log) {
        return log.substring(0, log.indexOf(LOG_SEPARATOR));
    }

    @Override
    public int compare(LogFileHolder firstHolder, LogFileHolder secondHolder) {
        var firstTimestamp = getTimestamp(firstHolder.getLog());
        var secondTimestamp = getTimestamp(secondHolder.getLog());

        return firstTimestamp.compareTo(secondTimestamp);
    }
}
