package de.leterel.isthandy;

public final class DeviceClassifierTest {
    private static int assertions;
    private static void expect(DeviceClassifier.Kind expected, int dp, boolean touch, boolean phone, boolean tv, boolean watch, boolean car, boolean pc) {
        DeviceClassifier.Result result = DeviceClassifier.classify(dp, touch, phone, tv, watch, car, pc);
        if (result.kind != expected || result.title.isEmpty() || result.explanation.isEmpty())
            throw new AssertionError("Expected " + expected + " for " + dp + "dp, got " + result.kind);
        assertions++;
    }
    private static void expectAndroid(DeviceClassifier.Kind expected, int dp, boolean telephony,
                                      int uiMode, boolean leanback, boolean watch, boolean automotive, boolean pc) {
        DeviceClassifier.Result result = DeviceClassifier.classifyAndroidFeatures(
            dp, true, telephony, uiMode, leanback, watch, automotive, pc);
        if (result.kind != expected)
            throw new AssertionError("Expected " + expected + " for Android UI mode " + uiMode + ", got " + result.kind);
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
        // Android Configuration: normal=1, desk dock=2, car dock=3, TV=4, watch=6.
        // Night flags (0x10/0x20) must not change the hardware classification.
        for (int night : new int[] {0, 0x10, 0x20}) {
            expectAndroid(DeviceClassifier.Kind.PHONE, 393, true, night | 1, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.PHONE, 393, true, night | 2, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.PHONE, 393, true, night | 3, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.TABLET, 800, true, night | 3, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 393, false, night | 3, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 393, true, night | 3, false, false, true, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 800, true, night | 1, false, false, true, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 800, true, night | 4, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 220, true, night | 6, false, false, false, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 800, true, night | 1, true, false, false, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 220, true, night | 1, false, true, false, false);
            expectAndroid(DeviceClassifier.Kind.UNKNOWN, 800, true, night | 1, false, false, false, true);
        }
        System.out.println("PASS: " + assertions + " classification scenarios; thresholds, ambiguous devices, car/desk dock modes, night flags and hardware categories.");
    }
}
