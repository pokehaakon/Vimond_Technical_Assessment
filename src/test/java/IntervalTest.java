import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;


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

    private Interval in0 = Interval.of(-1, -3);
    private Interval in1 = Interval.of(1, 3);
    private Interval in2 = Interval.of(2, 2);
    private Interval in3 = Interval.of(5, 7);
    private Interval in4 = Interval.of(6, 9);
    private Interval in5 = Interval.of(10, 11);

    private Interval[] intervals = {in0, in1, in2, in3, in4, in5, Interval.of(3, -1)};


    @Test
    public void testIntervalConstruction() {
        Interval in1 = Interval.of(-10, 30);
        Interval in2 = Interval.of(30, -10);

        assertEquals(-10, in1.start);
        assertEquals(30, in1.end);
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
        Interval in3_in4 = Interval.of(5, 9);
        assertEquals(in3_in4, in3.union(in4));
        assertEquals(in3_in4, in4.union(in3));

        //Non-overlapping but touching intervals
        Interval in4_in5 = Interval.of(6, 11);
        assertEquals(in4_in5, in4.union(in5));
        assertEquals(in4_in5, in5.union(in4));

        //Non-unionable
        assertThrowsExactly(UnionException.class, () -> in1.union(in3));
    }

    @Test
    public void testIntersection() {
        //binary relations
        testSymmetric(Interval::intersects, intervals);
        testReflexive(Interval::intersects, intervals);

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
        testSymmetric(Interval::isSequentialWith, intervals);
        testIrreflexive(Interval::isSequentialWith, intervals);

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
        testReflexive(Interval::covers, intervals);
        testAntisymmetric(Interval::covers, intervals);

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
        testIrreflexive(Interval::splits, intervals); //redundant
        testAsymmetric(Interval::splits, intervals);

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
        testReflexive(Interval::leftIntersects, intervals);

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
        testReflexive(Interval::rightIntersects, intervals);

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
        testIrreflexive(Interval::endsBefore, intervals); //redundant
        testAsymmetric(Interval::endsBefore, intervals);

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
        testAll(in -> assertEquals(in, Interval.parseInterval(in.toString())), intervals);
        assertEquals(in0, Interval.parseInterval("-1--3 "));
        assertEquals(in0, Interval.parseInterval(" -3--1"));

        assertThrowsExactly(RuntimeException.class, () -> Interval.parseInterval("-1"));
    }

    @Test
    public void testEquals() {
        testReflexive(Interval::equals, intervals);
        testSymmetric(Interval::equals, intervals);
        testTransitive(Interval::equals, intervals);

        Interval otherIn1 = Interval.of(1, 3);

        assertEquals(in1.start, otherIn1.start);
        assertEquals(in1.end, otherIn1.end);
        assertEquals(in1, otherIn1);

        assertNotEquals(in1, Interval.of(1, 2));
        assertNotEquals(in1, Interval.of(2, 3));

        assertNotEquals(in1, "notSameType");
    }

    @Test
    public void testOf() {
        assertEquals(Interval.of(1, 3), Interval.of(3, 1));
        for (int i = -30; i < 20; i += 5) {
            for (int j = -10; j < 35; j += 5) {
                Interval testInterval = Interval.of(i, j);
                assertEquals(testInterval, Interval.of(i, j));
                assertEquals(testInterval, Interval.of(j, i));
            }
        }
    }
}