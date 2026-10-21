package de.leterel.isthandy;
import de.leterel.ads.AdsHook;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/** Local-only joke device classifier. No permissions, services, storage, analytics or network. */
public final class MainActivity extends Activity {
    private static final int BG = Color.rgb(12, 16, 21), PANEL = Color.rgb(19, 26, 32);
    private static final int LIME = Color.rgb(217, 255, 95), WHITE = Color.rgb(238, 243, 235), MUTED = Color.rgb(146, 159, 168);
    private static final long DURATION = 5800;
    private static final String[] STEPS = {"Existenz von Gerät bestätigen", "Hosentaschen-Kompatibilität prüfen", "Tablet-Ausreden ausschließen", "Offensichtliches wissenschaftlich belegen"};
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView phase, percent, resultTitle, resultBody, verdictLabel, features;
    private final TextView[] steps = new TextView[4];
    private ProgressBar progressBar;
    private ScrollView scroll;
    private ScanView scanner;
    private long start;
    private DeviceClassifier.Result result;
    private LinearLayout resultPanel;
    private AdsHook ads;
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            float amount = Math.min(1f, (SystemClock.elapsedRealtime() - start) / (float) DURATION);
            boolean done = amount >= 1;
            int stage = Math.min(3, (int) (amount * 4));
            phase.setText(done ? "ANALYSE ABGESCHLOSSEN" : "GERÄT WIRD GESCANNT");
            percent.setText(Math.round(amount * 100) + "%");
            progressBar.setProgress(Math.round(amount * 1000));
            scanner.update(amount, result.kind);
            for (int i = 0; i < 4; i++) {
                boolean complete = done || i < stage;
                steps[i].setText((complete ? "✓  " : i == stage ? "›  " : "·  ") + STEPS[i]);
                steps[i].setTextColor(complete ? LIME : i == stage ? WHITE : MUTED);
            }
            if (done) {
                verdictLabel.setText("DAS GROSSE ERGEBNIS");
                resultTitle.setText(result.title);
                resultTitle.setTextColor(LIME);
                resultBody.setText(result.explanation);
                resultPanel.setBackground(rounded(PANEL, 20, Color.rgb(83, 107, 47)));
                scroll.post(() -> scroll.smoothScrollTo(0, Math.max(0, resultPanel.getTop() - dp(20))));
                resultTitle.announceForAccessibility(result.title + " " + result.explanation);
                ads.result(true);
            } else {
                handler.postDelayed(this, 32);
            }
        }
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        if (Build.VERSION.SDK_INT >= 30) getWindow().setDecorFitsSystemWindows(false);
        scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(LinearLayout.VERTICAL);
        column.setPadding(dp(24), dp(20), dp(24), dp(28));
        column.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scroll.addView(column);
        if (Build.VERSION.SDK_INT >= 30) {
            scroll.setOnApplyWindowInsetsListener((view, insets) -> {
                Insets bars = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }
        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView brand = label("IST HANDY?", 16, WHITE, true);
        brand.setLetterSpacing(0.08f);
        top.addView(brand, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView badge = label(AdsHook.ENABLED ? "● TESTWERBUNG" : "●  LOKAL", 10, LIME, true);
        badge.setPadding(dp(10), dp(7), dp(10), dp(7));
        badge.setBackground(rounded(Color.rgb(31, 40, 30), 30, Color.TRANSPARENT));
        top.addView(badge);
        column.addView(top);
        TextView eyebrow = label("INSTITUT FÜR KLARE SACHEN", 10, MUTED, true);
        eyebrow.setLetterSpacing(0.12f); add(column, eyebrow, 28);
        TextView heading = label("Erst mal\nHandy beweisen.", 34, WHITE, true);
        heading.setLineSpacing(dp(1), 1f); add(column, heading, 8);
        add(column, label("Eine extrem unnötige Untersuchung.", 14, MUTED, false), 9);

        LinearLayout scanPanel = new LinearLayout(this);
        scanPanel.setOrientation(LinearLayout.VERTICAL);
        scanPanel.setPadding(dp(18), dp(18), dp(18), dp(18));
        scanPanel.setBackground(rounded(PANEL, 22, Color.rgb(42, 51, 59)));
        add(column, scanPanel, 25);
        LinearLayout scanHeader = new LinearLayout(this);
        scanHeader.setGravity(Gravity.CENTER_VERTICAL);
        phase = label("GERÄT WIRD GESCANNT", 10, MUTED, true);
        phase.setLetterSpacing(0.06f);
        scanHeader.addView(phase, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        percent = label("0%", 16, LIME, true);
        percent.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        scanHeader.addView(percent);
        scanPanel.addView(scanHeader);
        scanner = new ScanView(this);
        scanPanel.addView(scanner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(226)));
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(1000);
        progressBar.setProgressTintList(android.content.res.ColorStateList.valueOf(LIME));
        progressBar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(48, 59, 48)));
        progressBar.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        scanPanel.addView(progressBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(5)));
        for (int i = 0; i < 4; i++) {
            steps[i] = label("·  " + STEPS[i], 12, MUTED, false);
            add(scanPanel, steps[i], i == 0 ? 18 : 9);
        }
        resultPanel = new LinearLayout(this);
        resultPanel.setOrientation(LinearLayout.VERTICAL);
        resultPanel.setPadding(dp(20), dp(19), dp(20), dp(20));
        resultPanel.setBackground(rounded(PANEL, 20, Color.rgb(42, 51, 59)));
        add(column, resultPanel, 16);
        verdictLabel = label("URTEIL STEHT NOCH AUS", 10, MUTED, true);
        verdictLabel.setLetterSpacing(0.10f);
        resultPanel.addView(verdictLabel);
        resultTitle = label("Bitte seriös bleiben.", 27, WHITE, true);
        add(resultPanel, resultTitle, 11);
        resultBody = label("Die Wissenschaft braucht kurz. Wir tun so, als wäre das kompliziert.", 13, MUTED, false);
        resultBody.setLineSpacing(dp(3), 1f); add(resultPanel, resultBody, 9);
        features = label("", 10, MUTED, false);
        features.setTypeface(Typeface.MONOSPACE); add(column, features, 18);
        TextView footer = label(AdsHook.ENABLED ? "GERÄTEMERKMALE ECHT. SCAN IST THEATER.\nTestprofil mit Google-Werbe-SDK und Internetzugriff.\nWerbung erst nach Datenschutzprüfung am Ergebnis." : "GERÄTEMERKMALE ECHT. SCAN IST THEATER.\nKeine Kamera. Kein Internet. Kein Daten-Sammeln.\nBeim nächsten Öffnen beginnt das Drama erneut.", 10, MUTED, false);
        footer.setLineSpacing(dp(4), 1f); add(column, footer, 12);
        ads = new AdsHook(this, column);
        setContentView(scroll);
    }

    @Override protected void onResume() {
        super.onResume();
        ads.result(false);
        ads.resume();
        detectDevice();
        handler.removeCallbacks(tick);
        start = SystemClock.elapsedRealtime();
        verdictLabel.setText("URTEIL STEHT NOCH AUS");
        resultTitle.setText("Bitte seriös bleiben."); resultTitle.setTextColor(WHITE);
        resultBody.setText("Die Wissenschaft braucht kurz. Wir tun so, als wäre das kompliziert.");
        resultPanel.setBackground(rounded(PANEL, 20, Color.rgb(42, 51, 59)));
        scroll.scrollTo(0, 0);
        handler.post(tick);
    }
    @Override protected void onPause() {
        ads.pause();
        handler.removeCallbacks(tick);
        super.onPause();
    }
    @Override protected void onDestroy() { ads.destroy(); handler.removeCallbacks(tick); super.onDestroy(); }
    private void detectDevice() {
        PackageManager pm = getPackageManager();
        int shortDp;
        if (Build.VERSION.SDK_INT >= 30) {
            Rect bounds = getWindowManager().getMaximumWindowMetrics().getBounds();
            shortDp = Math.round(Math.min(bounds.width(), bounds.height()) / getResources().getDisplayMetrics().density);
        } else {
            android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            shortDp = Math.round(Math.min(metrics.widthPixels, metrics.heightPixels) / metrics.density);
        }
        boolean telephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        result = DeviceClassifier.classifyAndroidFeatures(shortDp, pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN), telephony,
            getResources().getConfiguration().uiMode,
            pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK),
            pm.hasSystemFeature(PackageManager.FEATURE_WATCH),
            pm.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE),
            pm.hasSystemFeature("android.hardware.type.pc"));
        features.setText("DISPLAY " + shortDp + " dp  /  TELEFONIE " + (telephony ? "JA" : "NEIN") + (AdsHook.ENABLED ? "  /  TESTWERBUNG" : "  /  OFFLINE"));
    }
    private TextView label(String text, float sp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(text); view.setTextSize(sp); view.setTextColor(color);
        view.setFontFeatureSettings("kern"); view.setIncludeFontPadding(false);
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return view;
    }
    private void add(LinearLayout parent, View child, int marginTop) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(marginTop); parent.addView(child, params);
    }
    private GradientDrawable rounded(int color, int radius, int stroke) {
        GradientDrawable drawable = new GradientDrawable(); drawable.setColor(color);
        drawable.setCornerRadius(dp(radius)); drawable.setStroke(dp(1), stroke); return drawable;
    }
    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
