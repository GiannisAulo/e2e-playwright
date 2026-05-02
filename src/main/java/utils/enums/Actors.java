package utils.enums;

public enum Actors {
    // ── Practice Portal ───────────────────────────────────────
    TraderDeclarant("TraderDeclarant"),
    TraderReadOnly("TraderReadOnly"),
    NCAAdmin("NCAAdmin"),
    NCAReviewer("NCAReviewer"),

    // ── EC Authority (legacy) ─────────────────────────────────
    ECAuthAdmin("EC_OFF");

    public final String label;

    Actors(String label) { this.label = label; }

    public String getLabel() { return label; }
}
