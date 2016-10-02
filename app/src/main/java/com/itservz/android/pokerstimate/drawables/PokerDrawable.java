package com.itservz.android.pokerstimate.drawables;

import android.content.Context;
import android.graphics.Bitmap;
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
        int bitmapWidth = 0;
        int bitmapHeight = 0;
        int textSize = 0;
        if(fullScreen){
            bitmapHeight = context.getResources().getDimensionPixelSize(R.dimen.card_big_height);
            bitmapWidth = context.getResources().getDimensionPixelSize(R.dimen.card_big_width);
            textSize = context.getResources().getDimensionPixelSize(R.dimen.card_big_text_size);
        } else {
            bitmapHeight = context.getResources().getDimensionPixelSize(R.dimen.card_small_height);
            bitmapWidth = context.getResources().getDimensionPixelSize(R.dimen.card_small_width);
            textSize = context.getResources().getDimensionPixelSize(R.dimen.card_small_text_size);
        }
        BitmapShader shader;
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        paint.setColor(Color.WHITE);

        this.text = text;

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(MyFont.getTypeface());
        //textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.setLetterSpacing(0.5f);
        }
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getBounds().height();
        int width = getBounds().width();
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textHeight = textBounds.height();
        Log.d("PokerDrawable", "text height " + textHeight);
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
