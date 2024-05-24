import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interval {
    public final int start, end;

    private Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Create the union between this and the other interval <br>
     * If the union of the intervals does not form an interval,
     * this throws an {@code UnionException}
     * @param other the other interval
     */
    public Interval union(Interval other) {
        if (!this.intersects(other) && !this.isSequentialWith(other)) {
            String msg = "Cannot create a union between: '"
                    + this + "' and '"
                    + other + "', as the union does not form a sequence!";
            throw new UnionException(msg);
        }
        return of(
                Math.min(this.start, other.start),
                Math.max(this.end, other.end)
        );
    }

    /**
     * Checks if the intervals intersect
     */
    public boolean intersects(Interval other) {
        return this.contains(other.start)
                || this.contains(other.end)
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
    public boolean isSequentialWith(Interval other) {
        return this.end + 1 == other.start
                || other.end + 1 == this.start;
    }

    /**
     * Checks if the other interval is contained within this interval
     * @param other the other interval
     */
    public boolean covers(Interval other) {
        return this.contains(other.start)
                && this.contains(other.end);
    }

    /**
     * Checks if this interval splits the other interval,
     * that is, removing this interval from the other
     * interval would result in two intervals
     *
     * @param other the other interval
     */
    public boolean splits(Interval other) {
        return other.start < this.start
                && this.end < other.end;
    }

    /**
     * Checks if the start of the other interval is contained in this interval
     * @param other the other interval
     */
    public boolean leftIntersects(Interval other) {
        return this.contains(other.start);
    }

    /**
     * Checks if the end of the other interval is contained in this interval
     * @param other the other interval
     */
    public boolean rightIntersects(Interval other) {
        return this.contains(other.end);
    }

    /**
     * Checks if this interval ends before the other and is not sequential.
     * @param other the other interval
     */
    public boolean endsBefore(Interval other) {
        return this.end < other.start;
    }

    /**
     * Checks if a number is contained in the interval
     * @param number the number to check
     */
    public boolean contains(int number) {
        return this.start <= number && number <= this.end;
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Interval interval) {
            return this.start == interval.start
                    && this.end == interval.end;
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
    static public Interval parseInterval(String intervalString) {
        String regex = "(-?\\d+)-(-?\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(intervalString.strip());
        if (matcher.matches()) {
            return of(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2))
            );
        } else {
            throw new RuntimeException("Could not parse Interval from: '" + intervalString + "'");
        }
    }

    /**
     * Create a new interval from the given start and end. <br>
     * If {@code end < start}, it changes the order!
     *
     * @param start
     * @param end
     */
    static public Interval of(int start, int end) {
        if (start < end) {
            return new Interval(start, end);
        }
        return new Interval(end, start);
    }
}
