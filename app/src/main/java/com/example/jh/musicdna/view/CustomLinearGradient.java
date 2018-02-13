package com.example.jh.musicdna.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.jh.musicdna.ui.activity.HomeActivity;

/**
 * Created by jinhui on 2018/2/14.
 * Email:1004260403@qq.com
 */

public class CustomLinearGradient extends View {

    Paint paint;
    int startColor, midColor, endColor;
    int alpha;

    public CustomLinearGradient(Context context) {
        super(context);
        init();
    }

    public CustomLinearGradient(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomLinearGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        alpha = 140;
        startColor = Color.argb(alpha, Color.red(HomeActivity.themeColor), Color.green(HomeActivity.themeColor), Color.blue(HomeActivity.themeColor));
        midColor = Color.parseColor("#88111111");
        endColor = Color.parseColor("#FF111111");
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        startColor = Color.argb(alpha, Color.red(HomeActivity.themeColor), Color.green(HomeActivity.themeColor), Color.blue(HomeActivity.themeColor));
//        midColor = Color.parseColor("#88111111");
//        endColor = Color.parseColor("#FF111111");

//        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), new int[]{startColor, midColor, endColor}, new float[]{0.0f, 0.35f, 1.0f}, Shader.TileMode.MIRROR));
        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), startColor, endColor, Shader.TileMode.CLAMP));
        canvas.drawPaint(paint);
    }
}
