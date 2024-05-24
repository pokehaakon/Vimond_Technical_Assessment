package Interval;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class IntegerInterval implements Interval<Integer, IntegerInterval> {
    /**
     * Create the union between this and the other interval <br>
     * If the union of the intervals does not form an interval,
     * this throws an {@code Interval.UnionException}
     * @param other the other interval
     */
    @Override
    public IntegerInterval union(IntegerInterval other) {
        if (!this.intersects(other) && !this.isSequentialWith(other)) {
            String msg = "Cannot create a union between: '"
                    + this + "' and '"
                    + other + "', as the union does not form a sequence!";
            throw new UnionException(msg);
        }
        return of(
                Math.min(this.getStart(), other.getStart()),
                Math.max(this.getEnd(), other.getEnd())
        );
    }

    /**
     * Checks if the intervals intersect
     */
    @Override
    public boolean intersects(IntegerInterval other) {
        return this.contains(other.getStart())
                || this.contains(other.getEnd())
                || other.covers(this);
    }

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
    @Override
    public boolean isSequentialWith(IntegerInterval other) {
        return this.getEnd() + 1 == other.getStart()
                || other.getEnd() + 1 == this.getStart();
    }

    /**
     * Checks if the other interval is contained within this interval
     * @param other the other interval
     */
    @Override
    public boolean covers(IntegerInterval other) {
        return this.contains(other.getStart())
                && this.contains(other.getEnd());
    }

    /**
     * Checks if this interval splits the other interval,
     * that is, removing this interval from the other
     * interval would result in two intervals
     *
     * @param other the other interval
     */
    @Override
    public boolean splits(IntegerInterval other) {
        return other.getStart() < this.getStart()
                && this.getEnd()< other.getEnd();
    }

    /**
     * Checks if the start of the other interval is contained in this interval
     * @param other the other interval
     */
    @Override
    public boolean leftIntersects(IntegerInterval other) {
        return this.contains(other.getStart());
    }

    /**
     * Checks if the end of the other interval is contained in this interval
     * @param other the other interval
     */
    @Override
    public boolean rightIntersects(IntegerInterval other) {
        return this.contains(other.getEnd());
    }

    /**
     * Checks if this interval ends before the other and is not sequential.
     * @param other the other interval
     */
    @Override
    public boolean endsBefore(IntegerInterval other) {
        return this.getEnd() < other.getStart();
    }

    /**
     * Checks if a number is contained in the interval
     * @param number the number to check
     */
    @Override
    public boolean contains(Integer number) {
        return this.getStart() <= number && number <= this.getEnd();
    }

    @Override
    public String toString() {
        return getStart() + "-" + getEnd();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IntegerInterval interval) {
            return Objects.equals(this.getStart(), interval.getStart())
                    && Objects.equals(this.getEnd(), interval.getEnd());
        }
        return super.equals(other);
    }

    /**
     * Takes a string on the form 123-234 (regex '-?\d+--?\d+'),
     * and converts it into an interval
     *
     * @param intervalString the string to parse
     * @return an interval created from the string
     */
    static public IntegerInterval parseInterval(String intervalString) {
        String regex = "(-?\\d+)-(-?\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(intervalString.strip());
        if (matcher.matches()) {
            return of(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2))
            );
        } else {
            throw new RuntimeException("Could not parse Interval.Interval from: '" + intervalString + "'");
        }
    }

    /**
     * Create a new interval from the given start and end. <br>
     * If {@code end < start}, it changes the order!
     *
     * @param start the start of the wanted interval
     * @param end the end of the wanted interval
     */
    static public IntegerInterval of(int start, int end) {
        if (start == end) return new UnitInterval(start);
        if (start < end) {
            return new PairInterval(start, end);
        }
        return new PairInterval(end, start);
    }

    static private final class UnitInterval extends IntegerInterval {
        private final int start;
        private UnitInterval(int start) {
            this.start = start;
        }

        @Override
        public Integer getStart() {
            return start;
        }

        @Override
        public Integer getEnd() {
            return start;
        }
    }

    static private final class PairInterval extends IntegerInterval {
        private final int start, end;
        private PairInterval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer getStart() {
            return start;
        }

        @Override
        public Integer getEnd() {
            return end;
        }
    }
}
