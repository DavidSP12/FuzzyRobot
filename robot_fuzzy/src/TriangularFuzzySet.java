public class TriangularFuzzySet extends FuzzySet {
    private final double a, b, c;
    public TriangularFuzzySet(String name, double a, double b, double c) {
        super(name);
        if(!(a<=b && b<=c)) throw new IllegalArgumentException("triangular requiere a<=b<=c");
        this.a=a; this.b=b; this.c=c;
    }
    @Override public double membership(double x) {
        if(x<a||x>c) return 0;
        if(x==b) return 1;
        if(a==b && x>=a && x<=b) return 1;
        if(b==c && x>=b && x<=c) return 1;
        if(x>a && x<b) return (x-a)/(b-a);
        if(x>b && x<c) return (c-x)/(c-b);
        return 0;
    }
    @Override public String describe() { return String.format("%s TRIANGULAR(%.2f,%.2f,%.2f) mu=%.4f",name,a,b,c,lastMembership); }
}
