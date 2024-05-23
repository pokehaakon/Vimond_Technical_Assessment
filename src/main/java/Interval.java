public class Interval {
    public final int start, end;

    private Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Interval union(Interval other) {
        return of(
                Math.min(this.start, other.start),
                Math.max(this.end, other.end)
        );
    }

    public boolean intersects(Interval other) {
        return this.start <= other.start && other.start <= this.end
                || this.start <= other.end && other.start <= this.end;
    }

    public boolean isSequentialWith(Interval other) {
        return this.end + 1 == other.start
                || other.end + 1 == this.start;
    }

    public boolean covers(Interval other) {
        return this.start <= other.start
                && other.end <= this.end;
    }

    public boolean splits(Interval other) {
        return other.start < this.start
                && this.end < other.end;
    }

    public boolean leftIntersects(Interval other) {
        return this.start <= other.start
                && other.start <= this.end;
    }

    public boolean rightIntersects(Interval other) {
        return this.start <= other.end
                && other.end <= this.end;
    }

    public boolean endsBefore(Interval other) {
        return this.end < other.start;
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }

    static public Interval parseInterval(String intervalString) {
        String[] ends = intervalString.strip().split("-");
        if (ends.length != 2) {
            throw new RuntimeException("Could not parse Interval from: '" + intervalString + "'");
        }
        return of(
                Integer.parseInt(ends[0]),
                Integer.parseInt(ends[1])
        );
    }

    static public Interval of(int start, int end) {
        if (start < end) {
            return new Interval(start, end);
        }
        return new Interval(end, start);
    }
}
