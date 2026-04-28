public class TrapezoidalFuzzySet extends FuzzySet {
    private final double a, b, c, d;
    public TrapezoidalFuzzySet(String name, double a, double b, double c, double d) {
        super(name);
        if(!(a<=b && b<=c && c<=d)) throw new IllegalArgumentException("trapezoidal requiere a<=b<=c<=d");
        this.a=a; this.b=b; this.c=c; this.d=d;
    }
    @Override public double membership(double x) {
        if(x<a||x>d) return 0;
        if(x>=b && x<=c) return 1;
        if(x>=a && x<b) { if(b==a) return 1; return (x-a)/(b-a); }
        if(x>c && x<=d) { if(d==c) return 1; return (d-x)/(d-c); }
        return 0;
    }
    @Override public String describe() { return String.format("%s TRAPEZOIDAL(%.2f,%.2f,%.2f,%.2f) mu=%.4f",name,a,b,c,d,lastMembership); }
}
