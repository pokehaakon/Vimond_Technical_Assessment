import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Tools {
    static public List<Interval> parseIntervalsFromInput(BufferedReader reader) throws IOException {
        return Arrays.stream(reader.readLine().split(", "))
                .map(Interval::parseInterval)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static public List<Interval> combineOverlappingIntervals(List<Interval> intervals) {
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
        stack.add(current);

        return stack;
    }

    static public List<Interval> doThing(List<Interval> include, List<Interval> exclude) {
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
