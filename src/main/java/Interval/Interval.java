package Interval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Interval<T, E extends Interval<T, E>> {
    /**
     * @return Start of interval
     */
    T getStart();

    /**
     * @return End of interval
     */
    T getEnd();

    /**
     * Create the union between this and the other interval <br>
     * If the union of the intervals does not form an interval,
     * this throws an {@code Interval.UnionException}
     * @param other the other interval
     */
    E union(E other);

    /**
     * Checks if the intervals intersect
     */
    boolean intersects(E other);

    /**
     * Checks if the intervals are sequential <br><br>
     *
     * An interval is sequential if it either starts
     * one after or ends one before the other interval. <br>
     *
     * Example: <br>
     * {@code (4-5).isSequentialWith(6-7) => true} <br>
     * {@code (1-3).isSequentialWith(1-5) => false}
     *
     * @param other the other interval
     */
    boolean isSequentialWith(E other);

    /**
     * Checks if the other interval is contained within this interval
     * @param other the other interval
     */
    boolean covers(E other);

    /**
     * Checks if this interval splits the other interval,
     * that is, removing this interval from the other
     * interval would result in two intervals
     *
     * @param other the other interval
     */
    boolean splits(E other);

    /**
     * Checks if the start of the other interval is contained in this interval
     * @param other the other interval
     */
    boolean leftIntersects(E other);

    /**
     * Checks if the end of the other interval is contained in this interval
     * @param other the other interval
     */
    boolean rightIntersects(E other);

    /**
     * Checks if this interval ends before the other and is not sequential.
     * @param other the other interval
     */
    boolean endsBefore(E other);

    /**
     * Checks if a number is contained in the interval
     * @param element the number to check
     */
    boolean contains(T element);

    @Override
    String toString();

    @Override
    boolean equals(Object other);

}
