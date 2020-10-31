package mx.jovannypcg.logaggregator.domain.comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class LogComparatorTests {
    private Comparator<String> logComparator;

    @BeforeEach
    public void init() {
        logComparator = new LogComparator();
    }

    @Test
    public void compareReturnsZeroDueToSameTimestamps() {
        String firstLog = "2016-12-20T19:00:45Z, Server A started.";
        String secondLog = "2016-12-20T19:00:45Z, Server B started.";

        assertThat(logComparator.compare(firstLog, secondLog))
                .isEqualTo(0);
    }

    @Test
    public void compareReturnsPositiveValueDueToFirstStringIsGreaterThanSecond() {
        String firstLog = "2016-12-20T19:00:46Z, Server A started.";
        String secondLog = "2016-12-20T19:00:45Z, Server B started.";

        assertThat(logComparator.compare(firstLog, secondLog))
                .isPositive();
    }

    @Test
    public void compareReturnsNegativeNumberDueToFirstStringIsLessThanSecond() {
        String firstLog = "2016-12-20T19:00:45Z, Server A started.";
        String secondLog = "2016-12-20T19:00:46Z, Server B started.";

        assertThat(logComparator.compare(firstLog, secondLog))
                .isNegative();
    }
}
