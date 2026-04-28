public enum FuzzyOperator {
    AND, OR;
    public double apply(double a, double b) {
        if (this == AND) return Math.min(a, b);
        return Math.max(a, b);
    }
}
