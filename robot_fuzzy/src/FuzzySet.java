public abstract class FuzzySet {
    protected final String name;
    protected double lastMembership;
    public FuzzySet(String name) { this.name = name; this.lastMembership = 0.0; }
    public String getName() { return name; }
    public double getLastMembership() { return lastMembership; }
    public void setLastMembership(double v) { this.lastMembership = clamp01(v); }
    public abstract double membership(double x);
    public double fuzzify(double x) { this.lastMembership = clamp01(membership(x)); return this.lastMembership; }
    protected double clamp01(double v) { if(v<0) return 0; if(v>1) return 1; return v; }
    public abstract String describe();
}
