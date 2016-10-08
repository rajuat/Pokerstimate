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
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;

import com.itservz.android.pokerstimate.Preferences;
import com.itservz.android.pokerstimate.R;
import com.itservz.android.pokerstimate.core.CardColor;
import com.itservz.android.pokerstimate.core.ColorsFactory;
import com.itservz.android.pokerstimate.fonts.MyFont;

import java.io.Serializable;

public class PokerDrawable extends Drawable implements Serializable{

    private final TextPaint textPaint;
    private final TextPaint smallTextPaint;
    private final TextPaint logoTextPaint;
    private final CardColor cardColor;
    private final float density;
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
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.card_big);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.card_small);
        }

        height = bitmap.getHeight();
        width = bitmap.getWidth();
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        this.text = text == null ? "" : text;

        cardColor = ColorsFactory.getInstance(context).getRandomPokerColors();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        textPaint = new TextPaint();
        textPaint.setColor(cardColor.getDark());
        textPaint.setTypeface(MyFont.getDinTypeface());
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        smallTextPaint = new TextPaint();
        smallTextPaint.setColor(Color.BLACK);
        smallTextPaint.setTypeface(MyFont.getTrenchTypeface());
        smallTextPaint.setFakeBoldText(true);
        smallTextPaint.setAntiAlias(true);
        smallTextPaint.setTextAlign(Paint.Align.CENTER);
        smallTextPaint.setTextSize(32);

        logoTextPaint = new TextPaint();
        logoTextPaint.setColor(ContextCompat.getColor(context, R.color.brand_red_2));
        logoTextPaint.setTypeface(MyFont.getTrenchTypeface());
        logoTextPaint.setFakeBoldText(true);
        logoTextPaint.setAntiAlias(true);
        logoTextPaint.setTextAlign(Paint.Align.CENTER);
        density = context.getResources().getDisplayMetrics().density;
        logoTextPaint.setTextSize(14 * density);
        Log.d("PokerDrawable", "density "+density);
    }

    @Override
    public void draw(Canvas canvas) {
        float textSize = height * .32f;
        float radius = 4*density;
        if(fullScreen){
            radius = 12*density;
            if(text.length() == 1){
                textSize = height * 0.64f;
            } else if (text.length() == 2){
                textSize = height * 0.56f;
            }
        }
        textPaint.setTextSize(textSize);

        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        float margin = 0.0f;
        RectF rect = new RectF(margin, margin, width - margin, height - margin);
        Path clipPath = new Path();
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        canvas.drawColor(cardColor.getLight());
        canvas.drawRoundRect(rect, radius, radius, paint);

        if(fullScreen) {
            if("☕".equals(text)){
                textPaint.setTextSize(textSize * .35f);
                canvas.drawText(text, width/2, height * .6f, textPaint);
            } else if("∞".equals(text)) {
                canvas.drawText(text, width / 2, height * .68f, textPaint);
            } else {
                canvas.drawText(text, width / 2, height / 2 + textHeight / 2, textPaint);
            }

            /*canvas.drawText(text, width / 7, height / 9 , smallTextPaint);
            canvas.drawText(text, width * 17/20, height * 29/30, smallTextPaint);*/

            String companyName = getCompanyName();
            Rect companyTextBounds = new Rect();
            logoTextPaint.getTextBounds(companyName, 0, companyName.length(), companyTextBounds);
            canvas.drawText(companyName, width/2, height * .05f + companyTextBounds.height(), logoTextPaint );

            String teamName = getTeamName();
            Rect teamTextBounds = new Rect();
            logoTextPaint.getTextBounds(teamName, 0, teamName.length(), teamTextBounds);
            canvas.drawText(teamName, width/2, height * .95f , logoTextPaint );
        } else {
            canvas.drawText(text, width / 2, height / 2 + textHeight / 2, textPaint);
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
        String companyName = sharedPreferences.getString(Preferences.COMPANY_NAME.name(), "ITSERVZ");
        if(companyName == null || companyName.trim().length() == 0){
            companyName = "ITSERVZ";
        }
        return companyName;
    }

    public String getTeamName() {
        String teamName = sharedPreferences.getString(Preferences.TEAM_NAME.name(), "TEAM SCRUM");
        if(teamName == null || teamName.trim().length() == 0){
            teamName = "TEAM SCRUM";
        }
        return teamName;
    }

    public String getText() {
        return text;
    }

}
