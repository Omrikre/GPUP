package Engine.Enums;

public enum Location {
    LEAF, MIDDLE, ROOT, INDEPENDENT;

    @Override
    public String toString() {
        return super.toString().substring(0, 1).toUpperCase() +
                super.toString().substring(1).toLowerCase();
    }

}
