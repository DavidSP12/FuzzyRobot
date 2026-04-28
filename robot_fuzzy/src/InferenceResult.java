import java.util.*;
public class InferenceResult {
    private final double crispOutput;
    private final Map<String,Double> aggregatedOutputDegrees;
    private final String trace;
    public InferenceResult(double c, Map<String,Double> a, String t) { crispOutput=c; aggregatedOutputDegrees=new LinkedHashMap<>(a); trace=t; }
    public double getCrispOutput() { return crispOutput; }
    public Map<String,Double> getAggregatedOutputDegrees() { return aggregatedOutputDegrees; }
    public String getTrace() { return trace; }
}
