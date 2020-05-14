package com.ring.utilsdemo.weight;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by ring on 2019/8/20.
 */
public class Thumb {

    private int mImgType;
    private Drawable mDisableImg;
    private Drawable mNormalImg;
    private Drawable mPressedImg;
    private int mRadio;
    private int mDisableColor;
    private int mNormalColor;
    private int mPressedColor;

    private int mCenterPosition;
    private int mCurrentPosition;
    private int mState;

    private Bitmap mBitmap;
    private Matrix mMatrix;

    private Paint mBitmapPaint;
    private Paint mCirclePaint;

    private Shader mShader = null;

    public Thumb(int imgType, Drawable disableImg, Drawable normalImg, Drawable pressedImg, int radio) {
        mImgType = imgType;
        mDisableImg = disableImg;
        mNormalImg = normalImg;
        mPressedImg = pressedImg;
        mRadio = radio;
        mBitmapPaint = new Paint();
        mMatrix = new Matrix();
    }

    public Thumb(int imgType, int radio, int disableColor, int normalColor, int pressedColor) {
        mImgType = imgType;
        mRadio = radio;
        mDisableColor = disableColor;
        mNormalColor = normalColor;
        mPressedColor = pressedColor;
        mCirclePaint = new Paint();
    }

    public void setCenterPosition(int centerPosition) {
        mCenterPosition = centerPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    public void setState(int state) {
        mState = state;
    }

    public void draw(Canvas canvas) {
        if (mImgType == 0) {
            if (mState == 0) {
                mBitmap = ((BitmapDrawable) mDisableImg).getBitmap();
            } else if (mState == 1) {
                mBitmap = ((BitmapDrawable) mNormalImg).getBitmap();
            } else {
                mBitmap = ((BitmapDrawable) mPressedImg).getBitmap();
            }
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            float scaleX = (float) mRadio * 2 / (float) width;
            float scaleY = (float) mRadio * 2 / (float) height;
            float scale = scaleX > scaleY ? scaleY : scaleX;
            float[] value = new float[9];
            mMatrix.getValues(value);
            value[Matrix.MSCALE_X] = scale;
            value[Matrix.MSCALE_Y] = scale;
            value[Matrix.MTRANS_X] = mCurrentPosition - (width * scale) / 2;
            value[Matrix.MTRANS_Y] = mCenterPosition - (height * scale) / 2;
            mMatrix.setValues(value);
            canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
        } else {
            if (mState == 0) {
                mShader = new RadialGradient(mCurrentPosition, mCenterPosition, mRadio, mDisableColor, Color.TRANSPARENT, Shader.TileMode.REPEAT);
                mCirclePaint.setShader(mShader);
                mCirclePaint.setColor(mDisableColor);
            } else if (mState == 1) {
                mShader = new RadialGradient(mCurrentPosition, mCenterPosition, mRadio, mNormalColor, Color.TRANSPARENT, Shader.TileMode.REPEAT);
                mCirclePaint.setShader(mShader);
                mCirclePaint.setColor(mNormalColor);
            } else {
                mShader = new RadialGradient(mCurrentPosition, mCenterPosition, mRadio, mPressedColor, Color.TRANSPARENT, Shader.TileMode.REPEAT);
                mCirclePaint.setShader(mShader);
                mCirclePaint.setColor(mPressedColor);
            }
            canvas.drawCircle(mCurrentPosition, mCenterPosition, mRadio, mCirclePaint);
        }
    }
}
