import java.util.*;
public class MamdaniInferenceEngine {
    private final FuzzySystem system; private final int samples;
    public MamdaniInferenceEngine(FuzzySystem s) { this(s,1000); }
    public MamdaniInferenceEngine(FuzzySystem s, int n) { this.system=s; this.samples=n; }
    public InferenceResult infer(Map<String,Double> inputs) {
        StringBuilder trace=new StringBuilder("========== TRAZA MOTOR MAMDANI ==========\n");
        trace.append("\n[FASE 1] Fuzzificacion de entradas\n");
        for(LinguisticVariable v:system.getVariables()) {
            if(v.isInput()) {
                double val=getVal(inputs,v.getName());
                trace.append(String.format("Entrada %s = %.4f%n",v.getName(),val));
                for(Map.Entry<String,Double> e:v.fuzzify(val).entrySet())
                    trace.append(String.format("  mu_%s(%.4f) = %.4f%n",e.getKey(),val,e.getValue()));
            }
        }
        trace.append("\n[FASE 2 y 3] Evaluacion de reglas\n");
        Map<String,Double> agg=new LinkedHashMap<>();
        int rn=1;
        for(FuzzyRule r:system.getKnowledgeBase().getRules()) {
            FuzzyCondition c1=r.getAntecedents().get(0), c2=r.getAntecedents().get(1);
            double mu1=system.getVariable(c1.getVariableName()).membershipOf(c1.getSetName());
            double mu2=system.getVariable(c2.getVariableName()).membershipOf(c2.getSetName());
            double act=r.getOperator().apply(mu1,mu2);
            String outSet=r.getConsequent().getSetName();
            double prev=agg.getOrDefault(outSet,0.0);
            double newVal=Math.max(prev,act);
            agg.put(outSet,newVal);
            trace.append(String.format("R%d: %s%n",rn,r));
            trace.append(String.format("  mu1=%.4f, mu2=%.4f, op=%s, act=%.4f => %s: max(%.4f,%.4f)=%.4f%n",mu1,mu2,r.getOperator(),act,outSet,prev,act,newVal));
            rn++;
        }
        trace.append("\n[FASE 4] Defuzzificacion por centroide\n");
        LinguisticVariable out=system.getOutputVariable();
        double crisp=centroid(out,agg,trace);
        trace.append(String.format("Resultado crisp final para %s = %.4f%n",out.getName(),crisp));
        trace.append("==========================================\n");
        return new InferenceResult(crisp,agg,trace.toString());
    }
    private double centroid(LinguisticVariable v, Map<String,Double> agg, StringBuilder trace) {
        double min=v.getMin(), max=v.getMax(), step=(max-min)/samples;
        double num=0, den=0;
        for(int i=0;i<=samples;i++) {
            double x=min+i*step, mu=0;
            for(Map.Entry<String,Double> e:agg.entrySet())
                mu=Math.max(mu,Math.min(e.getValue(),v.getSet(e.getKey()).membership(x)));
            num+=x*mu; den+=mu;
        }
        trace.append(String.format("  Numerador=%.6f, Denominador=%.6f%n",num,den));
        if(den==0) { trace.append("  ADVERTENCIA: ninguna regla activada.\n"); return Double.NaN; }
        return num/den;
    }
    private double getVal(Map<String,Double> m, String k) {
        for(Map.Entry<String,Double> e:m.entrySet()) if(e.getKey().trim().equalsIgnoreCase(k.trim())) return e.getValue();
        throw new IllegalArgumentException("Falta entrada: "+k);
    }
}
