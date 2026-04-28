public enum VariableType {
    INPUT, OUTPUT;
    public static VariableType fromText(String text) {
        return VariableType.valueOf(text.trim().toUpperCase());
    }
}
