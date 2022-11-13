package soccer.app.entities.formation;

public enum Formations {

    FOUR_FOUR_TWO("4-4-2"),
    FOUR_THREE_THREE("4-3-3"),
    THREE_FOUR_THREE("3-4-3"),
    FOUR_FIVE_ONE("4-5-1");

    final String value;

    Formations(String value) {
        this.value = value;
    }

    public static Formations findByValue(String value) {
        for (Formations f : Formations.values()) {
            if (f.getValue().equals(value)) {
                return f;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
