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

        List<Interval> intervals = Tools.parseIntervalsFromInput(reader);
        assertEquals(Interval.of(11, 11), intervals.get(0));
        assertEquals(Interval.of(3, 7), intervals.get(1));
        assertEquals(Interval.of(5, 9), intervals.get(2));
        assertEquals(Interval.of(11, 15), intervals.get(3));
    }

    @Test
    public void testParseEmptyIntervals() throws IOException {
        String testString = "\n(none)";
        Reader stingReader = new StringReader(testString);
        BufferedReader reader = new BufferedReader(stingReader);

        //empty input
        List<Interval> intervals = Tools.parseIntervalsFromInput(reader);
        assertTrue(intervals.isEmpty());

        //'(none)' input
        intervals = Tools.parseIntervalsFromInput(reader);
        assertTrue(intervals.isEmpty());
    }
}