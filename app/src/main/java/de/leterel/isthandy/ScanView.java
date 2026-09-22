package de.leterel.isthandy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/** Decorative only: no sensor input, camera, or data collection. */
public final class ScanView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float progress;
    private DeviceClassifier.Kind kind = DeviceClassifier.Kind.UNKNOWN;
    public ScanView(Context context) {
        super(context);
        setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
    }
    public void update(float progress, DeviceClassifier.Kind kind) {
        this.progress = progress;
        this.kind = kind;
        invalidate();
    }
    private void color(int color, float width, Paint.Style style) {
        paint.setColor(color); paint.setStrokeWidth(width); paint.setStyle(style);
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float unit = Math.min(getWidth() / 340f, getHeight() / 240f);
        canvas.save();
        canvas.translate(getWidth() / 2f, getHeight() / 2f);
        canvas.scale(unit, unit);
        int lime = Color.rgb(217, 255, 95);
        color(Color.rgb(31, 40, 46), 0.8f, Paint.Style.STROKE);
        for (int x = -160; x <= 160; x += 20) canvas.drawLine(x, -120, x, 120, paint);
        for (int y = -120; y <= 120; y += 20) canvas.drawLine(-170, y, 170, y, paint);
        color(Color.rgb(47, 58, 51), 1, Paint.Style.STROKE);
        canvas.drawCircle(0, 0, 86, paint);
        canvas.drawCircle(0, 0, 107, paint);
        color(lime, 2.2f, Paint.Style.STROKE);
        canvas.drawArc(new RectF(-107, -107, 107, 107), -90, progress * 360, false, paint);
        float width = progress >= 1 && kind == DeviceClassifier.Kind.TABLET ? 91 : 63;
        color(Color.rgb(12, 16, 21), 1, Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(-width / 2 - 4, -63, width / 2 + 4, 63), 10, 10, paint);
        color(lime, 2, Paint.Style.STROKE);
        canvas.drawRoundRect(new RectF(-width / 2 - 4, -63, width / 2 + 4, 63), 10, 10, paint);
        color(Color.rgb(143, 160, 134), 1, Paint.Style.STROKE);
        canvas.drawRoundRect(new RectF(-width / 2 + 2, -49, width / 2 - 2, 49), 2, 2, paint);
        canvas.drawLine(-10, -55, 10, -55, paint);
        canvas.drawLine(-8, 56, 8, 56, paint);
        if (progress < 1) {
            float y = -47 + 94 * ((progress * 4) % 1);
            color(Color.argb(32, 217, 255, 95), 1, Paint.Style.FILL);
            canvas.drawRect(-width / 2 + 3, Math.max(-47, y - 16), width / 2 - 3, y, paint);
            color(lime, 2, Paint.Style.STROKE);
            canvas.drawLine(-width / 2 + 3, y, width / 2 - 3, y, paint);
            float rotation = progress * 620;
            double rad = Math.toRadians(rotation);
            color(lime, 1, Paint.Style.FILL);
            canvas.drawCircle((float) Math.cos(rad) * 86, (float) Math.sin(rad) * 86, 3, paint);
        } else if (kind != DeviceClassifier.Kind.UNKNOWN) {
            color(lime, 4, Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            Path check = new Path(); check.moveTo(-15, 0); check.lineTo(-4, 11); check.lineTo(18, -15);
            canvas.drawPath(check, paint);
            paint.setStrokeCap(Paint.Cap.BUTT);
        } else {
            color(lime, 1, Paint.Style.FILL); paint.setTextSize(36); paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("?", 0, 13, paint);
        }
        color(Color.rgb(117, 135, 117), 1.5f, Paint.Style.STROKE);
        for (int signX : new int[] {-1, 1}) for (int signY : new int[] {-1, 1}) {
            canvas.drawLine(signX * 133, signY * 78, signX * 133, signY * 64, paint);
            canvas.drawLine(signX * 133, signY * 78, signX * 119, signY * 78, paint);
        }
        canvas.restore();
    }
}
