package com.itservz.android.pokerstimate.drawables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.Log;

import com.itservz.android.pokerstimate.R;
import com.itservz.android.pokerstimate.fonts.MyFont;

public class PokerDrawable extends Drawable {

    private final TextPaint textPaint;
    private Paint paint;
    private String text;

    public PokerDrawable(Context context, String text, boolean fullScreen) {
        BitmapShader shader;
        Bitmap bitmap;
        if(fullScreen){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.card14_big);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.card14_small);
        }
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        paint.setColor(Color.WHITE);

        this.text = text;

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(MyFont.getTypeface());
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("PokerDrawable", ""+Build.VERSION.SDK_INT);
            textPaint.setLetterSpacing(-0.1f);
        }*/
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getBounds().height();
        int width = getBounds().width();
        Rect textBounds = new Rect();
        textPaint.setTextSize(height*2/5);
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textHeight = textBounds.height();
        int textWidth = textBounds.width();
        RectF rect = new RectF(0.0f, 0.0f, width, height);
        canvas.drawRoundRect(rect, 30, 30, paint);
        canvas.drawText(text, width/2, height/2 + textHeight/2, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
