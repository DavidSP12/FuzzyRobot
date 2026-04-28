import java.util.*;
public class LinguisticVariable {
    private final String name; private final VariableType type;
    private final double min, max;
    private final Map<String,FuzzySet> sets = new LinkedHashMap<>();
    public LinguisticVariable(String name,VariableType type,double min,double max) {
        if(min>=max) throw new IllegalArgumentException("min debe ser < max");
        this.name=name; this.type=type; this.min=min; this.max=max;
    }
    public String getName() { return name; }
    public VariableType getType() { return type; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public void addSet(FuzzySet s) { sets.put(s.getName().trim().toLowerCase(), s); }
    public FuzzySet getSet(String n) {
        FuzzySet s = sets.get(n.trim().toLowerCase());
        if(s==null) throw new IllegalArgumentException("No existe conjunto '"+n+"' en '"+name+"'");
        return s;
    }
    public List<FuzzySet> getSets() { return Collections.unmodifiableList(new ArrayList<>(sets.values())); }
    public Map<String,Double> fuzzify(double v) {
        Map<String,Double> r=new LinkedHashMap<>();
        for(FuzzySet s:sets.values()) r.put(s.getName(), s.fuzzify(v));
        return r;
    }
    public double membershipOf(String n) { return getSet(n).getLastMembership(); }
    public boolean isInput() { return type==VariableType.INPUT; }
    public boolean isOutput() { return type==VariableType.OUTPUT; }
    public String describe() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Variable %s [%s] universo=[%.2f, %.2f]%n",name,type,min,max));
        for(FuzzySet s:sets.values()) sb.append("  - ").append(s.describe()).append(System.lineSeparator());
        return sb.toString();
    }
}
