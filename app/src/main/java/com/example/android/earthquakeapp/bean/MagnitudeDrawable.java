package com.example.android.earthquakeapp.bean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class MagnitudeDrawable extends Drawable {

    private final Paint circlePaint;
    private final Paint textPaint;

    private final Earthquake earthquake;

    public MagnitudeDrawable(@NonNull final Context context, @NonNull final Earthquake earthquake) {
        this.earthquake = earthquake;

        int magnitudeColor = ContextCompat.getColor(context, earthquake.getMagnitudeColorIdRef());
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(magnitudeColor);
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

/*
        <item name="fontFamily">sans-serif-medium</item>
        <item name="android:gravity">center</item>
        <item name="android:textColor">@android:color/white</item>
        <item name="android:textSize">16sp</item>

* */
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(context.getColor(android.R.color.white));


        textPaint.setTextSize(22f);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        //textPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //textHeight = (int)textPaint.measureText("yY");


    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(0,0,10,circlePaint);
        canvas.drawText("6,6", 0, 0, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        circlePaint.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        circlePaint.setColorFilter(cf);
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
