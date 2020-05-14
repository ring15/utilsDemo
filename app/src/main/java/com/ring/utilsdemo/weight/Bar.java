package com.ring.utilsdemo.weight;


import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by ring on 2019/8/20.
 */
class Bar {

    private int mRangePosition;
    private int mRangeItem;
    private int mRangeHeight;

    private int mStarPosition;
    private int mEndPosition;
    private int mCenterPosition;

    private int mCurrentPosition;

    private Paint mUnSelectedPaint;
    private Paint mSelectedPaint;

    public Bar(int rangePosition, int rangeItem, int rangeUnSelectedColor, int rangeSelectedColor, int rangeHeight, int rangeWidth) {
        mRangePosition = rangePosition;
        mRangeItem = rangeItem;
        mRangeHeight = rangeHeight;
        mUnSelectedPaint = new Paint();
        mUnSelectedPaint.setStrokeWidth(rangeWidth);
        mUnSelectedPaint.setStyle(Paint.Style.STROKE);
        mUnSelectedPaint.setColor(rangeUnSelectedColor);
        mSelectedPaint = new Paint();
        mSelectedPaint.setStrokeWidth(rangeWidth);
        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setColor(rangeSelectedColor);
    }

    public void setStarPosition(int starPosition) {
        mStarPosition = starPosition;
    }

    public void setEndPosition(int endPosition) {
        mEndPosition = endPosition;
    }

    public void setCenterPosition(int centerPosition) {
        mCenterPosition = centerPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < mRangeItem; i++) {
            int length = (mEndPosition - mStarPosition) / (mRangeItem - 1);
            int startX = mStarPosition + i * length;
            if (mRangePosition == 0) {
                canvas.drawLine(startX, mCenterPosition - mRangeHeight / 2,
                        startX, mCenterPosition + mRangeHeight / 2, startX > mCurrentPosition ? mUnSelectedPaint : mSelectedPaint);
            } else if (mRangePosition == 1) {
                canvas.drawLine(startX, mCenterPosition - mRangeHeight,
                        startX, mCenterPosition, startX > mCurrentPosition ? mUnSelectedPaint : mSelectedPaint);
            } else {
                canvas.drawLine(startX, mCenterPosition,
                        startX, mCenterPosition + mRangeHeight, startX > mCurrentPosition ? mUnSelectedPaint : mSelectedPaint);
            }
        }
    }
}
