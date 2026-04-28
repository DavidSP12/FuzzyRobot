import java.util.*;
public class FuzzySystem {
    private final Map<String,LinguisticVariable> vars = new LinkedHashMap<>();
    private KnowledgeBase kb = new KnowledgeBase();
    public void addVariable(LinguisticVariable v) {
        String k=v.getName().trim().toLowerCase();
        if(vars.containsKey(k)) throw new IllegalArgumentException("Variable repetida: "+v.getName());
        vars.put(k,v);
    }
    public LinguisticVariable getVariable(String n) {
        LinguisticVariable v=vars.get(n.trim().toLowerCase());
        if(v==null) throw new IllegalArgumentException("No existe variable '"+n+"'");
        return v;
    }
    public Collection<LinguisticVariable> getVariables() { return vars.values(); }
    public KnowledgeBase getKnowledgeBase() { return kb; }
    public void setKnowledgeBase(KnowledgeBase k) { kb=k; }
    public LinguisticVariable getOutputVariable() {
        for(LinguisticVariable v:vars.values()) if(v.isOutput()) return v;
        throw new IllegalStateException("No hay variable de salida");
    }
    public String describeVariables() {
        StringBuilder sb=new StringBuilder("Variables linguisticas cargadas:\n");
        for(LinguisticVariable v:vars.values()) sb.append(v.describe());
        return sb.toString();
    }
}
