package de.pfaffenrodt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Dimitri on 20.08.16.
 */
public class TextSceneObject extends Plane {
    private String mText;
    private int mWidth;
    private int mHeight;

    private Paint mTextPaint;
    private RectF mRectF;



    /**
     *
     * @param context
     * @param width of bitmap
     * @param height of bitmap
     */
    public TextSceneObject(Context context, int width, int height) {
        super(context, getSceneObjectWidth(), getSceneObjectHeight(width, height));
        mWidth = width;
        mHeight = height;

        mTextPaint = new Paint();
        mTextPaint.setTextSize(height/2);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setARGB(0xff, 0x00, 0x00, 0x00);
        mRectF = new RectF(0, 0, width, height);
    }

    private static float getSceneObjectWidth() {
        return 1f;
    }

    private static float getSceneObjectHeight(float width, float height) {
        return height / width;
    }

    public void setTextSize(int textSize){
        mTextPaint.setTextSize(textSize);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        invalidateTexture();
    }

    private void invalidateTexture() {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        onDraw(canvas);
        loadTexture(bitmap);
    }

    private void onDraw(Canvas canvas) {
        Paint.Align align = mTextPaint.getTextAlign();
        float x;
        float y;
        //x
        if (align == Paint.Align.LEFT) {
            x = mRectF.centerX() - mTextPaint.measureText(mText) / 2;
        } else if (align == Paint.Align.CENTER) {
            x = mRectF.centerX();
        } else {
            x = mRectF.centerX() + mTextPaint.measureText(mText) / 2;
        }
        //y
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float acent = Math.abs(metrics.ascent);
        float descent = Math.abs(metrics.descent);
        y = mRectF.centerY() + (acent - descent) / 2f;
        canvas.drawText(mText, x, y, mTextPaint);
    }
}
