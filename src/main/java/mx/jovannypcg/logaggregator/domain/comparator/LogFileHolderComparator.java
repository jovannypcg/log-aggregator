package mx.jovannypcg.logaggregator.domain.comparator;

import mx.jovannypcg.logaggregator.domain.LogFileHolder;

import java.util.Comparator;

/**
 * Comparator of {@link LogFileHolder} instances by the log they hold.
 *
 * The {@link LogFileHolder#log} attribute is grabbed and its timestamp is compared
 * against the log of the other {@link LogFileHolder}.
 *
 * Given that the timestamps are provided in the ISO 8601 format, there is no need
 * to convert them into instants or any other kind of date objects for comparison,
 * as this format provides lexicographical order, which means that they can be
 * compared as String instances.
 */
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
