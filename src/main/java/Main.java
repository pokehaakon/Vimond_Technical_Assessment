import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<Interval> include = Tools.parseIntervalsFromInput(reader);
        List<Interval> exclude = Tools.parseIntervalsFromInput(reader);
        List<Interval> results = Tools.minimalNonExcludedIntervalCovering(include, exclude);
        System.out.println(results.isEmpty() ? "(none)" : results);
    }
}
