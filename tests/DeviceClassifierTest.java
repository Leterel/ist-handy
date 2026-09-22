package de.leterel.isthandy;

public final class DeviceClassifierTest {
    private static int assertions;
    private static void expect(DeviceClassifier.Kind expected, int dp, boolean touch, boolean phone, boolean tv, boolean watch, boolean car, boolean pc) {
        DeviceClassifier.Result result = DeviceClassifier.classify(dp, touch, phone, tv, watch, car, pc);
        if (result.kind != expected || result.title.isEmpty() || result.explanation.isEmpty())
            throw new AssertionError("Expected " + expected + " for " + dp + "dp, got " + result.kind);
        assertions++;
    }
    public static void main(String[] args) {
        expect(DeviceClassifier.Kind.PHONE, 393, true, true, false, false, false, false);
        expect(DeviceClassifier.Kind.PHONE, 599, true, true, false, false, false, false);
        expect(DeviceClassifier.Kind.TABLET, 600, true, false, false, false, false, false);
        expect(DeviceClassifier.Kind.TABLET, 800, true, true, false, false, false, false); // LTE tablet stays tablet
        expect(DeviceClassifier.Kind.UNKNOWN, 393, true, false, false, false, false, false); // small Wi-Fi device
        expect(DeviceClassifier.Kind.UNKNOWN, 960, false, false, true, false, false, false);
        expect(DeviceClassifier.Kind.UNKNOWN, 220, true, true, false, true, false, false);
        expect(DeviceClassifier.Kind.UNKNOWN, 800, true, true, false, false, true, false);
        expect(DeviceClassifier.Kind.UNKNOWN, 800, true, true, false, false, false, true);
        expect(DeviceClassifier.Kind.UNKNOWN, 0, true, true, false, false, false, false);
        expect(DeviceClassifier.Kind.UNKNOWN, 3000, true, true, false, false, false, false);
        expect(DeviceClassifier.Kind.UNKNOWN, 800, false, true, false, false, false, false);
        System.out.println("PASS: " + assertions + " classification scenarios; phone/tablet threshold, cellular tablet, ambiguous devices, other form factors.");
    }
}
