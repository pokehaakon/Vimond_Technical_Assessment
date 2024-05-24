import Interval.IntegerInterval;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

import Interval.UnionException;

public class IntervalTest {

    /**
     * A relation is symmetric if for (a,b), R(a,b) => R(b,a)
     */
    private static <T> void testSymmetric(BiFunction<T, T, Boolean> functionToTest, T[] inputs) {
        for (T left : inputs) {
            for (T right : inputs) {
                assertFalse(
                        functionToTest.apply(left, right)
                        ^ functionToTest.apply(right, left),
                        "failed on: " + left + ", " + right
                );
            }
        }
    }

    /**
     * A relation is asymmetric if for (a,b), R(a,b) => !R(b,a) <br>
     * This is true if and only if the relation is both antisymmetric and irreflexive
     */
    private static <T> void testAsymmetric(BiFunction<T, T, Boolean> functionToTest, T[] inputs) {
        testIrreflexive(functionToTest, inputs);
        testAntisymmetric(functionToTest, inputs);
    }

    /**
     * A relation is antisymmetric if for (a,b), a != b and R(a,b) => !R(b,a)
     */
    private static <T> void testAntisymmetric(BiFunction<T, T, Boolean> functionToTest, T[] inputs) {
        for (T left : inputs) {
            for (T right : inputs) {
                if (left.equals(right)) continue;
                assertFalse(
                        functionToTest.apply(left, right)
                                && functionToTest.apply(right, left),
                        "failed on: " + left + ", " + right
                );
            }
        }
    }

    /**
     * A relation is reflexive if for a, R(a,a) is true
     */
    private static <T> void testReflexive(BiFunction<T, T, Boolean> functionToTest, T[] inputs) {
        for (T inp : inputs) {
            assertTrue(
                    functionToTest.apply(inp, inp),
                    "failed on: " + inp
            );
        }
    }

    /**
     * A relation is irreflexive if for a, R(a,a) is false
     */
    private static <T> void testIrreflexive(BiFunction<T, T, Boolean> functionToTest, T[] inputs) {
        for (T inp : inputs) {
            assertFalse(
                    functionToTest.apply(inp, inp),
                    "failed on: " + inp
            );
        }
    }

    private static <T> void testTransitive(BiFunction<T, T, Boolean> functionToTest, T[] inputs) {
        for (T left : inputs) {
            for (T middle : inputs) {
                if (!functionToTest.apply(left, middle)) continue;
                for (T right : inputs) {
                    if (!functionToTest.apply(middle, right)) continue;
                    assertTrue(
                            functionToTest.apply(left, right),
                            "failed on: " + left + ", " + middle + ", " + right
                    );
                }
            }
        }
    }

    private static <T> void testAll(Consumer<T> test, T[] inputs) {
        for (T input : inputs) {
            test.accept(input);
        }
    }

    final private IntegerInterval in0 = IntegerInterval.of(-1, -3);
    final private IntegerInterval in1 = IntegerInterval.of(1, 3);
    final private IntegerInterval in2 = IntegerInterval.of(2, 2);
    final private IntegerInterval in3 = IntegerInterval.of(5, 7);
    final private IntegerInterval in4 = IntegerInterval.of(6, 9);
    final private IntegerInterval in5 = IntegerInterval.of(10, 11);

    final private IntegerInterval[] intervals = {in0, in1, in2, in3, in4, in5, IntegerInterval.of(3, -1)};


    @Test
    public void testIntervalConstruction() {
        IntegerInterval in1 = IntegerInterval.of(-10, 30);
        IntegerInterval in2 = IntegerInterval.of(30, -10);

        assertEquals(-10, in1.getStart());
        assertEquals(30, in1.getEnd());
        assertEquals(in1, in2);
    }

    @Test
    public void testUnion() {

        //Self unions
        testAll(in -> assertEquals(in, in.union(in)), intervals);

        //One interval contained in the other
        assertEquals(in1, in1.union(in2));
        assertEquals(in1, in2.union(in1));

        //Overlapping intervals
        IntegerInterval in3_in4 = IntegerInterval.of(5, 9);
        assertEquals(in3_in4, in3.union(in4));
        assertEquals(in3_in4, in4.union(in3));

        //Non-overlapping but touching intervals
        IntegerInterval in4_in5 = IntegerInterval.of(6, 11);
        assertEquals(in4_in5, in4.union(in5));
        assertEquals(in4_in5, in5.union(in4));

        //Non-unionable
        assertThrowsExactly(UnionException.class, () -> in1.union(in3));
    }

    @Test
    public void testIntersection() {
        //binary relations
        testSymmetric(IntegerInterval::intersects, intervals);
        testReflexive(IntegerInterval::intersects, intervals);

        //One interval contained in the other
        assertTrue(in1.intersects(in2));

        //Overlapping intervals
        assertTrue(in3.intersects(in4));

        //Non-overlapping but touching intervals
        assertFalse(in4.intersects(in5));
    }

    @Test
    public void testSequentialWith() {
        //binary relations
        testSymmetric(IntegerInterval::isSequentialWith, intervals);
        testIrreflexive(IntegerInterval::isSequentialWith, intervals);

        //One interval contained in the other
        assertFalse(in1.isSequentialWith(in2));

        //Overlapping intervals
        assertFalse(in3.isSequentialWith(in4));

        //Non-overlapping but touching intervals
        assertTrue(in4.isSequentialWith(in5));
    }

    @Test
    public void testCovers() {
        //binary relations
        testReflexive(IntegerInterval::covers, intervals);
        testAntisymmetric(IntegerInterval::covers, intervals);

        //One interval contained in the other
        assertTrue(in1.covers(in2));
        assertFalse(in2.covers(in3));

        //Overlapping intervals
        assertFalse(in3.covers(in4));

        //Non-overlapping but touching intervals
        assertFalse(in4.covers(in5));
    }

    @Test
    public void testSplits() {
        //binary relations
        testIrreflexive(IntegerInterval::splits, intervals); //redundant
        testAsymmetric(IntegerInterval::splits, intervals);

        //One interval contained in the other
        assertFalse(in1.splits(in2));
        assertTrue(in2.splits(in1));

        //Overlapping intervals
        assertFalse(in3.splits(in4));

        //Non-overlapping but touching intervals
        assertFalse(in4.splits(in5));
    }

    @Test
    public void testLeftIntersects() {
        //binary relations
        testReflexive(IntegerInterval::leftIntersects, intervals);

        //One interval contained in the other
        assertTrue(in1.leftIntersects(in2));

        //Overlapping intervals
        assertTrue(in3.leftIntersects(in4));

        //Non-overlapping but touching intervals
        assertFalse(in4.leftIntersects(in5));
    }

    @Test
    public void testRightIntersects() {
        //binary relations
        testReflexive(IntegerInterval::rightIntersects, intervals);

        //One interval contained in the other
        assertFalse(in2.rightIntersects(in1));

        //Overlapping intervals
        assertTrue(in4.rightIntersects(in3));

        //Non-overlapping but touching intervals
        assertFalse(in4.rightIntersects(in5));
    }

    @Test
    public void testEndsBefore() {
        //binary relations
        testIrreflexive(IntegerInterval::endsBefore, intervals); //redundant
        testAsymmetric(IntegerInterval::endsBefore, intervals);

        //One interval contained in the other
        assertFalse(in2.endsBefore(in1));

        //Overlapping intervals
        assertFalse(in4.endsBefore(in3));

        //Non-overlapping but touching intervals
        assertTrue(in4.endsBefore(in5));
    }

    @Test
    public void testContains() {
        assertFalse(in1.contains(0));
        assertTrue(in1.contains(1));
        assertTrue(in1.contains(2));
        assertTrue(in1.contains(3));
        assertFalse(in1.contains(4));

        assertFalse(in2.contains(1));
        assertTrue(in2.contains(2));
        assertFalse(in2.contains(3));
    }

    @Test
    public void testToString() {
        assertEquals("-3--1", in0.toString());
        assertEquals("1-3", in1.toString());
        assertEquals("2-2", in2.toString());
        assertEquals("5-7", in3.toString());
        assertEquals("6-9", in4.toString());
        assertEquals("10-11", in5.toString());
    }

    @Test
    public void testParseInterval() {
        testAll(in -> assertEquals(in, IntegerInterval.parseInterval(in.toString())), intervals);
        assertEquals(in0, IntegerInterval.parseInterval("-1--3 "));
        assertEquals(in0, IntegerInterval.parseInterval(" -3--1"));

        assertThrowsExactly(RuntimeException.class, () -> IntegerInterval.parseInterval("-1"));
    }

    @Test
    public void testEquals() {
        testReflexive(IntegerInterval::equals, intervals);
        testSymmetric(IntegerInterval::equals, intervals);
        testTransitive(IntegerInterval::equals, intervals);

        IntegerInterval otherIn1 = IntegerInterval.of(1, 3);

        assertEquals(in1.getStart(), otherIn1.getStart());
        assertEquals(in1.getEnd(), otherIn1.getEnd());
        assertEquals(in1, otherIn1);

        assertNotEquals(in1, IntegerInterval.of(1, 2));
        assertNotEquals(in1, IntegerInterval.of(2, 3));

        assertNotEquals(in1, "notSameType");
    }

    @Test
    public void testOf() {
        assertEquals(IntegerInterval.of(1, 3), IntegerInterval.of(3, 1));
        for (int i = -30; i < 20; i += 5) {
            for (int j = -10; j < 35; j += 5) {
                IntegerInterval testInterval = IntegerInterval.of(i, j);
                assertEquals(testInterval, IntegerInterval.of(i, j));
                assertEquals(testInterval, IntegerInterval.of(j, i));
            }
        }
    }
}