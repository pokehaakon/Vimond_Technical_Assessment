import Interval.IntegerInterval;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToolsTest {

    static public boolean isSortedAndUnique(List<IntegerInterval> intervals) {
        int prev = intervals.get(0).getStart() - 1;
        for (IntegerInterval interval : intervals) {
            if (prev < interval.getStart()) {
                prev = interval.getStart();
            } else {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testCombineOverlappingIntervalsSmall() throws IOException {
        String testString = "11-11, 3-7, 5-9, 11-15\n4-5, 6-7";
        Reader stingReader = new StringReader(testString);
        BufferedReader reader = new BufferedReader(stingReader);
        List<IntegerInterval> include = Tools.parseIntervalsFromInput(reader);
        List<IntegerInterval> exclude = Tools.parseIntervalsFromInput(reader);

        include = Tools.combineOverlappingIntervals(include);
        exclude = Tools.combineOverlappingIntervals(exclude);

        assertEquals(IntegerInterval.of(3, 9), include.get(0));
        assertEquals(IntegerInterval.of(11, 15), include.get(1));

        assertEquals(IntegerInterval.of(4, 7), exclude.get(0));

        assertTrue(isSortedAndUnique(include));
        assertTrue(isSortedAndUnique(exclude));
    }

    @Test
    public void testCombineOverlappingIntervalsLarge() throws IOException {
        String testString = "23-27, 82-89, 61-68, 94-101, 29-36, 45-53, 39-46, 88-97, 4-10, 90-92\n93-95, 71-79, 35-40, 30-31, 18-22";
        Reader stingReader = new StringReader(testString);
        BufferedReader reader = new BufferedReader(stingReader);
        List<IntegerInterval> include = Tools.parseIntervalsFromInput(reader);
        List<IntegerInterval> exclude = Tools.parseIntervalsFromInput(reader);

        include = Tools.combineOverlappingIntervals(include);
        exclude = Tools.combineOverlappingIntervals(exclude);

        List<IntegerInterval> expectedInclude = Tools.parseIntervalsFromInput(new BufferedReader(new StringReader("4-10, 23-27, 29-36, 39-53, 61-68, 82-101")));
        List<IntegerInterval> expectedExclude = Tools.parseIntervalsFromInput(new BufferedReader(new StringReader("18-22, 30-31, 35-40, 71-79, 93-95")));

        assertEquals(expectedInclude, include);
        assertEquals(expectedExclude, exclude);

        assertTrue(isSortedAndUnique(include));
        assertTrue(isSortedAndUnique(exclude));
    }

    @Test
    public void testMinimalNonExcludedIntervalCoveringSmall() throws IOException {
        String testString = "11-11, 3-7, 5-9, 11-15\n4-5, 6-7";
        Reader stingReader = new StringReader(testString);
        BufferedReader reader = new BufferedReader(stingReader);
        List<IntegerInterval> include = Tools.parseIntervalsFromInput(reader);
        List<IntegerInterval> exclude = Tools.parseIntervalsFromInput(reader);

        List<IntegerInterval> results = Tools.minimalNonExcludedIntervalCovering(include, exclude);

        assertEquals(IntegerInterval.of(3, 3), results.get(0));
        assertEquals(IntegerInterval.of(8, 9), results.get(1));
        assertEquals(IntegerInterval.of(11, 15), results.get(2));

        assertTrue(isSortedAndUnique(results));
    }

    @Test
    public void testMinimalNonExcludedIntervalCoveringLarge() throws IOException {
        String testString = "23-27, 82-89, 61-68, 94-101, 29-36, 45-53, 39-46, 88-97, 4-10, 90-92\n93-95, 71-79, 35-40, 30-31, 18-22";
        Reader stingReader = new StringReader(testString);
        BufferedReader reader = new BufferedReader(stingReader);
        List<IntegerInterval> include = Tools.parseIntervalsFromInput(reader);
        List<IntegerInterval> exclude = Tools.parseIntervalsFromInput(reader);

        List<IntegerInterval> results = Tools.minimalNonExcludedIntervalCovering(include, exclude);

        List<IntegerInterval> expectedResult = Tools.parseIntervalsFromInput(new BufferedReader(new StringReader("4-10, 23-27, 29-29, 32-34, 41-53, 61-68, 82-92, 96-101")));
        assertEquals(expectedResult, results);

        assertTrue(isSortedAndUnique(results));
    }
}