package com.example.android.earthquakeapp.bean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.android.earthquakeapp.activity.UiUtils;

public class MagnitudeDrawable extends Drawable {

    private final Paint circlePaint;
    private final Paint textPaint;

    private final Earthquake earthquake;
    private final float mRadius;

    public MagnitudeDrawable(@NonNull final Context context, @NonNull final Earthquake earthquake) {
        this.earthquake = earthquake;

        int magnitudeColor = ContextCompat.getColor(context, earthquake.getMagnitudeColorIdRef());
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(magnitudeColor);
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mRadius = dipToPixels(context,18);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(context.getColor(android.R.color.white));


        textPaint.setTypeface(Typeface.SANS_SERIF);
        textPaint.setTextSize(sipToPixels(context, 16));
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public static float dipToPixels(Context context, float dipValue){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  dipValue, metrics);
    }

    public static float sipToPixels(Context context, float sipValue){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,  sipValue, metrics);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG,"drawCanvas");
        float radius = mRadius;
        canvas.drawCircle(radius,radius,radius,circlePaint);
        canvas.drawText(UiUtils.DECIMAL_FORMAT.format(earthquake.getMagnitude()), radius, radius * 1.3f, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        Log.d(TAG,"setAlpha " + alpha);
        circlePaint.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        Log.d(TAG,"setColorFilter " + cf);
        circlePaint.setColorFilter(cf);
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public int getIntrinsicWidth(){
        return (int)(mRadius * 2);
    }

    public int getIntrinsicHeight(){
        return getIntrinsicWidth();
    }

    private static final String TAG = MagnitudeDrawable.class.getSimpleName();
}
