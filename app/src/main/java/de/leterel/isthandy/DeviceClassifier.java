package de.leterel.isthandy;

/** Pure, testable heuristic. Android does not expose an authoritative phone/tablet flag. */
public final class DeviceClassifier {
    // Stable android.content.res.Configuration constants. Kept here as values so
    // the platform-feature mapping can be tested without an Android runtime.
    private static final int UI_MODE_TYPE_MASK = 0x0f;
    private static final int UI_MODE_TYPE_TELEVISION = 0x04;
    private static final int UI_MODE_TYPE_WATCH = 0x06;
    public enum Kind { PHONE, TABLET, UNKNOWN }
    public static final class Result {
        public final Kind kind;
        public final String title;
        public final String explanation;
        Result(Kind kind, String title, String explanation) {
            this.kind = kind;
            this.title = title;
            this.explanation = explanation;
        }
    }
    public static Result classifyAndroidFeatures(int shortestDp, boolean touch, boolean telephony,
                                                 int uiMode, boolean leanback, boolean watch,
                                                 boolean automotive, boolean pc) {
        int mode = uiMode & UI_MODE_TYPE_MASK;
        // UI_MODE_TYPE_CAR is also available on ordinary phones placed in a car
        // dock. Only FEATURE_AUTOMOTIVE identifies built-in automotive hardware.
        return classify(shortestDp, touch, telephony,
            mode == UI_MODE_TYPE_TELEVISION || leanback,
            mode == UI_MODE_TYPE_WATCH || watch, automotive, pc);
    }
    public static Result classify(int shortestDp, boolean touch, boolean telephony,
                                  boolean tv, boolean watch, boolean car, boolean pc) {
        if (tv || watch || car || pc) {
            String name = tv ? "Fernseher" : watch ? "Uhr" : car ? "Auto-System" : "Computer";
            return new Result(Kind.UNKNOWN, "Kein normales Handy, Mois.",
                "Android meldet ein " + name + ". Das passt in keine Hosentaschen-Theorie.");
        }
        if (!touch || shortestDp < 200 || shortestDp > 2400) {
            return new Result(Kind.UNKNOWN, "Keine Ahnung, Bruder.",
                "Die Gerätemerkmale reichen nicht für ein ehrliches Urteil.");
        }
        if (shortestDp >= 600) {
            return new Result(Kind.TABLET, "Nee, is Tablet.",
                "Zumindest Tablet-Format: großes Touchdisplay. Ein aufgeklapptes Foldable kann genauso aussehen.");
        }
        if (telephony) {
            return new Result(Kind.PHONE, "Ja, is Handy, Mois.",
                "Kompaktes Touchdisplay + Telefonie-Hardware. Das ist ziemlich handyverdächtig.");
        }
        return new Result(Kind.UNKNOWN, "Handy-Format. Aber ...", 
            "Kleines Touchdisplay, keine gemeldete Telefonie. Könnte auch ein Mini-Tablet sein.");
    }
    private DeviceClassifier() {}
}
