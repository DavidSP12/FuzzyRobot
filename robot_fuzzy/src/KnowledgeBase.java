import java.util.*;
public class KnowledgeBase {
    private final List<FuzzyRule> rules = new ArrayList<>();
    public void addRule(FuzzyRule r) { rules.add(r); }
    public List<FuzzyRule> getRules() { return Collections.unmodifiableList(rules); }
    public String describe() {
        StringBuilder sb = new StringBuilder("Base de conocimiento - Reglas cargadas:\n");
        for(int i=0;i<rules.size();i++) sb.append(String.format("  R%d: %s%n",i+1,rules.get(i)));
        return sb.toString();
    }
}
