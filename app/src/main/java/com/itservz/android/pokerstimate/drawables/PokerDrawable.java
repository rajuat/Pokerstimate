package com.itservz.android.pokerstimate.drawables;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.util.Log;

import com.itservz.android.pokerstimate.Preferences;
import com.itservz.android.pokerstimate.R;
import com.itservz.android.pokerstimate.fonts.MyFont;

import java.io.Serializable;

public class PokerDrawable extends Drawable implements Serializable{

    private final TextPaint textPaint;
    private final TextPaint smallTextPaint;
    private final TextPaint logoTextPaint;
    private Paint paint;

    private String text;
    private boolean fullScreen = false;
    private int height, width;
    private SharedPreferences sharedPreferences;

    public PokerDrawable(Context context, String text, boolean fullScreen) {
        this.fullScreen = fullScreen;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Bitmap bitmap = null;
        if(fullScreen){
            bitmap = BitmapFactory.decodeResource(context.getResources(), CardsFactory.getRandomBigCard());
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), CardsFactory.getRandomSmallCard());
        }
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        this.text = text == null ? "" : text;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        paint.setColor(Color.WHITE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(MyFont.getTypeface());
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        smallTextPaint = new TextPaint();
        smallTextPaint.setColor(Color.BLACK);
        smallTextPaint.setTypeface(MyFont.getTypeface());
        smallTextPaint.setFakeBoldText(true);
        smallTextPaint.setAntiAlias(true);
        smallTextPaint.setTextAlign(Paint.Align.CENTER);
        smallTextPaint.setTextSize(32);

        logoTextPaint = new TextPaint();
        logoTextPaint.setColor(Color.BLACK);
        logoTextPaint.setTypeface(MyFont.getTypeface());
        logoTextPaint.setFakeBoldText(true);
        logoTextPaint.setAntiAlias(true);
        logoTextPaint.setTextAlign(Paint.Align.CENTER);
        logoTextPaint.setTextSize(32);
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getBounds().height();
        int width = getBounds().width();
        Rect textBounds = new Rect();
        int textSize = height*2/5;
        if(fullScreen){
            if(text.length() == 1){
                textSize = height*4/5;
            } else if (text.length() == 2){
                textSize = height*3/5;
            }
        }
        textPaint.setTextSize(textSize);
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textHeight = textBounds.height();
        int textWidth = textBounds.width();
        RectF rect = new RectF(0.0f, 0.0f, width, height);
        canvas.drawRoundRect(rect, 30, 30, paint);
        canvas.drawText(text, width/2, height/2 + textHeight/2, textPaint);
        if(fullScreen) {
            canvas.drawText("100", width / 7, height / 9 , smallTextPaint);
            canvas.drawText("100", width * 17/20, height * 29/30, smallTextPaint);

            String companyName = getCompanyName();
            Rect companyTextBounds = new Rect();
            logoTextPaint.getTextBounds(companyName, 0, text.length(), companyTextBounds);
            canvas.drawText(companyName, width/2, companyTextBounds.height() /2, logoTextPaint );
            String teamName = getTeamName();
            Rect teamTextBounds = new Rect();
            logoTextPaint.getTextBounds(teamName, 0, text.length(), companyTextBounds);
            canvas.drawText(teamName, width/2, height - companyTextBounds.height() /2, logoTextPaint );
        }
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

    public String getCompanyName() {
        return sharedPreferences.getString(Preferences.COMPANY_NAME.name(), "ITSERVZ");
    }

    public String getTeamName() {
        return sharedPreferences.getString(Preferences.TEAM_NAME.name(), "TEAM SCRUM");
    }

    public String getText() {
        return text;
    }

}
