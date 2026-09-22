package de.leterel.isthandy;

/** Pure, testable heuristic. Android does not expose an authoritative phone/tablet flag. */
public final class DeviceClassifier {
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
