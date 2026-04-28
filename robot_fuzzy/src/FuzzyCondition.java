public class FuzzyCondition {
    private final String variableName, setName;
    public FuzzyCondition(String variableName, String setName) { this.variableName=variableName; this.setName=setName; }
    public String getVariableName() { return variableName; }
    public String getSetName() { return setName; }
    @Override public String toString() { return variableName+" IS "+setName; }
}
