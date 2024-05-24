import Interval.IntegerInterval;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InputTest {

    @Test
    public void testParseIntervalsFromInput() throws IOException {
        String testString = "11-11, 3-7, 5-9, 11-15";
        Reader stingReader = new StringReader(testString);
        BufferedReader reader = new BufferedReader(stingReader);

        List<IntegerInterval> intervals = Tools.parseIntervalsFromInput(reader);
        assertEquals(IntegerInterval.of(11, 11), intervals.get(0));
        assertEquals(IntegerInterval.of(3, 7), intervals.get(1));
        assertEquals(IntegerInterval.of(5, 9), intervals.get(2));
        assertEquals(IntegerInterval.of(11, 15), intervals.get(3));
    }
}