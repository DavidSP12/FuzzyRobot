import java.io.*; import java.nio.file.*; import java.util.*;
public class FuzzySystemLoader {
    public FuzzySystem loadSystem(Path vp, Path rp) throws IOException {
        FuzzySystem s=loadVariables(vp); s.setKnowledgeBase(loadRules(rp)); validateRules(s); return s;
    }
    public FuzzySystem loadVariables(Path vp) throws IOException {
        FuzzySystem s=new FuzzySystem(); LinguisticVariable cur=null;
        try(BufferedReader r=Files.newBufferedReader(vp)) {
            String l; int n=0;
            while((l=r.readLine())!=null) { n++;
                l=clean(l); if(l.isEmpty()) continue;
                String[] p=l.split(";"); String tag=p[0].trim().toUpperCase();
                if(tag.equals("VARIABLE")) { if(p.length!=5) throw new IllegalArgumentException("Linea "+n+": VARIABLE requiere 5 campos");
                    cur=new LinguisticVariable(p[1].trim(),VariableType.fromText(p[2]),Double.parseDouble(p[3].trim()),Double.parseDouble(p[4].trim())); s.addVariable(cur);
                } else if(tag.equals("SET")) { if(cur==null) throw new IllegalArgumentException("Linea "+n+": SET sin VARIABLE previa");
                    cur.addSet(parseSet(p,n));
                } else throw new IllegalArgumentException("Linea "+n+": etiqueta no reconocida: "+tag);
            }
        }
        return s;
    }
    public KnowledgeBase loadRules(Path rp) throws IOException {
        KnowledgeBase kb=new KnowledgeBase();
        try(BufferedReader r=Files.newBufferedReader(rp)) {
            String l; int n=0;
            while((l=r.readLine())!=null) { n++; l=clean(l); if(l.isEmpty()) continue; kb.addRule(parseRule(l,n)); }
        }
        return kb;
    }
    private FuzzySet parseSet(String[] p, int n) {
        String name=p[1].trim(), shape=p[2].trim().toUpperCase();
        if(shape.equals("TRIANGULAR")) { if(p.length!=6) throw new IllegalArgumentException("Linea "+n+": TRIANGULAR requiere a;b;c");
            return new TriangularFuzzySet(name,Double.parseDouble(p[3].trim()),Double.parseDouble(p[4].trim()),Double.parseDouble(p[5].trim()));
        }
        if(shape.equals("TRAPEZOIDAL")) { if(p.length!=7) throw new IllegalArgumentException("Linea "+n+": TRAPEZOIDAL requiere a;b;c;d");
            return new TrapezoidalFuzzySet(name,Double.parseDouble(p[3].trim()),Double.parseDouble(p[4].trim()),Double.parseDouble(p[5].trim()),Double.parseDouble(p[6].trim()));
        }
        throw new IllegalArgumentException("Linea "+n+": tipo no soportado: "+shape);
    }
    private FuzzyRule parseRule(String l, int n) {
        String[] t=l.trim().split("\\s+");
        if(t.length!=12) throw new IllegalArgumentException("Linea "+n+": formato incorrecto (12 tokens esperados)");
        List<FuzzyCondition> ante=new ArrayList<>();
        ante.add(new FuzzyCondition(t[1],t[3])); ante.add(new FuzzyCondition(t[5],t[7]));
        return new FuzzyRule(ante,FuzzyOperator.valueOf(t[4].toUpperCase()),new FuzzyCondition(t[9],t[11]));
    }
    private void validateRules(FuzzySystem s) {
        for(FuzzyRule r:s.getKnowledgeBase().getRules()) {
            for(FuzzyCondition c:r.getAntecedents()) { LinguisticVariable v=s.getVariable(c.getVariableName()); if(!v.isInput()) throw new IllegalArgumentException("Antecedente debe ser INPUT: "+c.getVariableName()); v.getSet(c.getSetName()); }
            FuzzyCondition c=r.getConsequent(); LinguisticVariable o=s.getVariable(c.getVariableName()); if(!o.isOutput()) throw new IllegalArgumentException("Consecuente debe ser OUTPUT: "+c.getVariableName()); o.getSet(c.getSetName());
        }
    }
    private String clean(String l) { int i=l.indexOf('#'); if(i>=0) l=l.substring(0,i); return l.trim(); }
}
