package soccer.models.playingfield;

public enum FieldSite {
    LEFT("LEFT_TEAM"),
    RIGHT("RIGHT_TEAM");
    private final String value;

    FieldSite(String string) {
        this.value = string;
    }

    @Override
    public String toString() {
        return value;
    }

}
