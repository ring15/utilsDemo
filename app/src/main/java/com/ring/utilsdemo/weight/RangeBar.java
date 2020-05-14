package com.ring.utilsdemo.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ring.utilsdemo.R;


/**
 * Created by ring on 2019/8/20.
 */
public class RangeBar extends View {

    private Context mContext;

    private int mMode;
    private int mImgType;
    private int mRangePosition;
    private Drawable mDisableImg;
    private Drawable mNormalImg;
    private Drawable mPressedImg;
    private int mRadio;
    private int mDisableColor;
    private int mNormalColor;
    private int mPressedColor;
    private int mUnSelectLineColor;
    private int mSelectLineColor;
    private int mLineHeight;
    private int mRangeItem;
    private int mRangeUnSelectedColor;
    private int mRangeSelectedColor;
    private int mRangeHeight;
    private int mRangeWidth;

    private int mWidth;
    private int mHeight;
    private int mStarPosition;
    private int mEndPosition;

    private Bar mBar;
    private Thumb mThumb;
    private ConnectingLine mConnectingLine;

    public RangeBar(Context context) {
        this(context, null);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRangeBae(context, attrs, defStyleAttr);
    }

    private void initRangeBae(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        mDisableImg = getResources().getDrawable(R.drawable.seek_thumb_disabled);
        mNormalImg = getResources().getDrawable(R.drawable.seek_thumb_normal);
        mPressedImg = getResources().getDrawable(R.drawable.seek_thumb_pressed);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RangeBar, defStyleAttr, 0);
        mMode = array.getInteger(R.styleable.RangeBar_mode, 0);
        mImgType = array.getInteger(R.styleable.RangeBar_imgType, 0);
        mRangePosition = array.getInteger(R.styleable.RangeBar_rangePosition, 0);
        if (null != array.getDrawable(R.styleable.RangeBar_disableImg)) {
            mDisableImg = array.getDrawable(R.styleable.RangeBar_disableImg);
        }
        if (null != array.getDrawable(R.styleable.RangeBar_normalImg)) {
            mNormalImg = array.getDrawable(R.styleable.RangeBar_normalImg);
        }
        if (null != array.getDrawable(R.styleable.RangeBar_pressedImg)) {
            mPressedImg = array.getDrawable(R.styleable.RangeBar_pressedImg);
        }
        mRadio = (int) array.getDimension(R.styleable.RangeBar_imgRadio, dip2px(context, 10));
        mDisableColor = array.getColor(R.styleable.RangeBar_disableColor, Color.GRAY);
        mNormalColor = array.getColor(R.styleable.RangeBar_normalColor, Color.GRAY);
        mPressedColor = array.getColor(R.styleable.RangeBar_pressedColor, Color.GRAY);
        mUnSelectLineColor = array.getColor(R.styleable.RangeBar_unSelectLineColor, Color.GRAY);
        mSelectLineColor = array.getColor(R.styleable.RangeBar_selectLineColor, Color.GRAY);
        mLineHeight = (int) array.getDimension(R.styleable.RangeBar_lineHeight, dip2px(context, 1));
        mRangeItem = array.getInteger(R.styleable.RangeBar_rangeItem, 5);
        mRangeUnSelectedColor = array.getColor(R.styleable.RangeBar_rangeUnSelectedColor, Color.GRAY);
        mRangeSelectedColor = array.getColor(R.styleable.RangeBar_rangeSelectedColor, Color.GRAY);
        mRangeHeight = (int) array.getDimension(R.styleable.RangeBar_rangeHeight, dip2px(context, 10));
        mRangeWidth = (int) array.getDimension(R.styleable.RangeBar_rangeWidth, dip2px(context, 1));
        array.recycle();

        if (mMode == 1){
            initBar();
        }
        initThumb();
        initLine();

    }

    private void initLine() {
        mConnectingLine = new ConnectingLine(mUnSelectLineColor, mSelectLineColor, mLineHeight);
    }

    private void initThumb() {
        if (mImgType == 0) {
            mThumb = new Thumb(mImgType, mDisableImg, mNormalImg, mPressedImg, mRadio);
        } else {
            mThumb = new Thumb(mImgType, mRadio, mDisableColor, mNormalColor, mPressedColor);
        }
    }

    private void initBar() {
        mBar = new Bar(mRangePosition, mRangeItem, mRangeUnSelectedColor, mRangeSelectedColor, mRangeHeight, mRangeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        mStarPosition = dip2px(mContext, 5) + mRadio;
        mEndPosition = mWidth - (dip2px(mContext, 5) + mRadio);
        if (mMode == 1 && mBar != null) {
            mBar.setStarPosition(mStarPosition);
            mBar.setEndPosition(mEndPosition);
            mBar.setCenterPosition(mHeight / 2);
            mBar.setCurrentPosition(mStarPosition);
        }
        if (mConnectingLine != null) {
            mConnectingLine.setStarPosition(mStarPosition);
            mConnectingLine.setEndPosition(mEndPosition);
            mConnectingLine.setCenterPosition(mHeight / 2);
            mConnectingLine.setCurrentPosition(mStarPosition);
        }
        if (mThumb != null) {
            mThumb.setCenterPosition(mHeight / 2);
            mThumb.setCurrentPosition(mStarPosition);
            mThumb.setState(0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMode == 1 &&mBar != null) {
            mBar.draw(canvas);
        }
        if (mConnectingLine != null) {
            mConnectingLine.draw(canvas);
        }
        if (mThumb != null) {
            mThumb.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
        }
        return true;
    }

    private void actionMove(MotionEvent event) {
        int x = (int) event.getX();
        if (x < mStarPosition ){
            setCurrentPosition(mStarPosition);
        }else if (x > mEndPosition){
            setCurrentPosition(mEndPosition);
        }else {
            setCurrentPosition(x);
        }
    }

    private void setCurrentPosition(int currentPosition){
        if (mMode == 1 &&mBar != null) {
            mBar.setCurrentPosition(currentPosition);
        }
        if (mConnectingLine != null) {
            mConnectingLine.setCurrentPosition(currentPosition);
        }
        if (mThumb != null) {
            mThumb.setCurrentPosition(currentPosition);
        }
        invalidate();
    }

    private void actionUp(MotionEvent event) {
        int x = (int) event.getX();
        if (mMode == 0){
            if (x < mStarPosition ){
                setCurrentPosition(mStarPosition);
            }else if (x > mEndPosition){
                setCurrentPosition(mEndPosition);
            }else {
                setCurrentPosition(x);
            }
            if (mThumb != null) {
                if (x > mStarPosition){
                    mThumb.setState(2);
                }else {
                    mThumb.setState(0);
                }
            }
        }else {
            int currentRage = 0;
            int length = (mEndPosition - mStarPosition) / (mRangeItem - 1);
            for (int i = 0; i < mRangeItem; i++){
                if (x < mStarPosition + length * i + length /2 && x > mStarPosition + length * i - length /2){
                    currentRage = i;
                    break;
                }
            }
            setCurrentPosition(currentRage * length + mStarPosition);
            if (mThumb != null) {
                if (currentRage > 0){
                    mThumb.setState(2);
                }else {
                    mThumb.setState(0);
                }
            }
        }
    }

    private void actionDown(MotionEvent event) {
        if (mThumb != null) {
            mThumb.setState(1);
            invalidate();
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
