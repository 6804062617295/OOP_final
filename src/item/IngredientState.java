package item;

// Ingredient states: raw, chopped, cooked, burnt
public enum IngredientState {
    //        name prefix  short tag (used on placeholder boxes)
    RAW("", ""),
    CHOPPED("Chopped", "cut"),
    COOKED("Cooked", "ok"),
    BURNT("Burnt", "X");

    private final String prefix;
    private final String tag;

    IngredientState(String prefix, String tag) {
        this.prefix = prefix;
        this.tag = tag;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTag() {
        return tag;
    }
}
