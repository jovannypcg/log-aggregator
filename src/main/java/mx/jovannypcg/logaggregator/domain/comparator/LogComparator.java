package mx.jovannypcg.logaggregator.domain.comparator;

import java.util.Comparator;

public class LogComparator implements Comparator<String> {
    public static final String LOG_SEPARATOR = ",";

    @Override
    public int compare(String first, String second) {
        var firstTimestamp = first.substring(0, first.indexOf(LOG_SEPARATOR));
        var secondTimestamp = second.substring(0, second.indexOf(LOG_SEPARATOR));

        return firstTimestamp.compareTo(secondTimestamp);
    }
}
