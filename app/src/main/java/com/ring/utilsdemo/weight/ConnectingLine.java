package com.ring.utilsdemo.weight;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by ring on 2019/8/20.
 */
class ConnectingLine {

    private int mStarPosition;
    private int mEndPosition;
    private int mCenterPosition;

    private int mCurrentPosition;

    private Paint mUnSelectPaint;
    private Paint mSelectPaint;

    public ConnectingLine(int unSelectLineColor, int selectLineColor, int lineHeight) {

        mUnSelectPaint = new Paint();
        mUnSelectPaint.setStrokeWidth(lineHeight);
        mUnSelectPaint.setStyle(Paint.Style.STROKE);
        mUnSelectPaint.setColor(unSelectLineColor);

        mSelectPaint = new Paint();
        mSelectPaint.setStrokeWidth(lineHeight);
        mSelectPaint.setStyle(Paint.Style.STROKE);
        mSelectPaint.setColor(selectLineColor);
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

    public void draw(Canvas canvas){
        canvas.drawLine(mStarPosition, mCenterPosition, mCurrentPosition, mCenterPosition, mSelectPaint);
        canvas.drawLine(mCurrentPosition, mCenterPosition, mEndPosition, mCenterPosition, mUnSelectPaint);
    }
}
