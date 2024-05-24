import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
}