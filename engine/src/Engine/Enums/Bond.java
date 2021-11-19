package Engine.Enums;

public enum Bond {
    DEPENDS_ON, REQUIRED_FOR;

    @Override
    public String toString() {
        String s = super.toString().substring(0, 1).toUpperCase() +
                super.toString().substring(1).toLowerCase();
        if (s.contains("_"))
            return s.replace('_', ' ');
        return s;
    }
}
