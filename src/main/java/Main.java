import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<Interval> include = Tools.parseIntervalsFromInput(reader);
        System.out.println("Include: " + include);
        include = Tools.combineOverlappingIntervals(include);
        System.out.println("Include: " + include);

        List<Interval> exclude = Tools.parseIntervalsFromInput(reader);
        System.out.println("Exclude: " + exclude);
        exclude = Tools.combineOverlappingIntervals(exclude);
        System.out.println("Exclude: " + exclude);

        System.out.println("Results: " + Tools.doThing(include, exclude));
    }
}
