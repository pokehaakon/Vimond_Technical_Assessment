import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Tools {

    /**
     * Reads one line from the {@code reader} and parses the intervals.
     * @param reader the reader
     * @return A list of the parsed intervals
     * @throws IOException if the reader throws
     */
    static public List<Interval> parseIntervalsFromInput(BufferedReader reader) throws IOException {
        return Arrays.stream(reader.readLine().split(", "))
                .map(Interval::parseInterval)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Takes a list of intervals and combines of all overlapping intervals <br>
     * This is done in O(n*log(n)) (cost of sorting) <br>
     * Mutates {@code intervals} by sorting
     * @param intervals the initial list of intervals
     * @return A list of intervals sorted by the start of the intervals, all starts of intervals are unique!
     */
    static public List<Interval> combineOverlappingIntervals(List<Interval> intervals) {
        // sort by start of interval.
        // In this step we could also combine all intervals
        // with equal starts by choosing the one with the maximal .end
        intervals.sort(Comparator.comparingInt(i -> i.start));
        List<Interval> stack = new ArrayList<>();

        Interval current = intervals.get(0);
        for (Interval next : intervals) {
            if (current.intersects(next) || current.isSequentialWith(next)) {
                current = current.union(next);
            } else {
                stack.add(current);
                current = next;
            }
        }
        //add the last element
        stack.add(current);

        return stack;
    }

    /**
     * Takes a list of intervals to include, and a list of intervals to exclude; <br>
     * and finds the minimal set of intervals needed to cover all the elements in <br>
     * {@code Union(include) - Union(exclude)} <br>
     *
     * Mutates {@code include} and {@code exclude} by sorting
     * @param include List of the included intervals
     * @param exclude List of the excluded intervals
     * @return List of the minimal set of intervals in sorted order
     */
    static public List<Interval> minimalNonExcludedIntervalCovering(List<Interval> include, List<Interval> exclude) {
        include = combineOverlappingIntervals(include);
        exclude = combineOverlappingIntervals(exclude);
        List<Interval> stack = new ArrayList<>();

        int includeIndex = 0, excludeIndex = 0;
        Interval in, ex;
        while (includeIndex < include.size() && excludeIndex < exclude.size()) {
            in = include.get(includeIndex);
            ex = exclude.get(excludeIndex);

            if (!in.intersects(ex)) {
                if (in.endsBefore(ex)) {
                    stack.add(in);
                    includeIndex++;
                } else {
                    excludeIndex++;
                }
                continue;
            }

            if (ex.covers(in)) {
                includeIndex++;
                continue;
            }
            if (ex.leftIntersects(in) || ex.splits(in)) {
                include.set(includeIndex, Interval.of(ex.end + 1, in.end));
                excludeIndex++;
            }
            if (ex.rightIntersects(in) || ex.splits(in)) {
                stack.add(Interval.of(in.start, ex.start - 1));
                includeIndex++;
            }
            if (ex.splits(in)) {
                //the split is done in-place and therefor
                //we need to "de-increment" the index
                includeIndex--;
            }
        }
        stack.addAll(include.subList(includeIndex, include.size()));

        return stack;
    }
}
