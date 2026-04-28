import java.util.*;
public class FuzzyRule {
    private final List<FuzzyCondition> antecedents;
    private final FuzzyOperator operator;
    private final FuzzyCondition consequent;
    public FuzzyRule(List<FuzzyCondition> antecedents, FuzzyOperator operator, FuzzyCondition consequent) {
        if(antecedents==null||antecedents.size()!=2) throw new IllegalArgumentException("Se requieren exactamente 2 antecedentes");
        this.antecedents=new ArrayList<>(antecedents); this.operator=operator; this.consequent=consequent;
    }
    public List<FuzzyCondition> getAntecedents() { return Collections.unmodifiableList(antecedents); }
    public FuzzyOperator getOperator() { return operator; }
    public FuzzyCondition getConsequent() { return consequent; }
    @Override public String toString() { return "IF "+antecedents.get(0)+" "+operator+" "+antecedents.get(1)+" THEN "+consequent; }
}
