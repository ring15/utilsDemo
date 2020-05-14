package com.ring.utilsdemo.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.ring.utilsdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/8/13.
 */
public class StepView extends View {

    private Context mContext;

    private int mCompletedLineColor;
    private int mUnCompletedLineColor;
    private int mCompletedTextColor;
    private int mUnCompletedTextColor;

    private Drawable mCompletedIcon;
    private Drawable mUnCompletedIcon;
    private Drawable mCompletingIcon;

    private int mTextSize;
    private int mLineSize;
    private int mType;

    private TextPaint mCompletedTextPaint;
    private TextPaint mUnCompletedTextPaint;
    private Paint mCompletedLinePaint;
    private Paint mUnCompletedLinePaint;

    private Paint mDrawablePaint;

    private List<String> mText;
    private int mCompletingNum;

    private Path mLinePath;

    private int horizonStart;

    private int offsetX;
    private int offsetY;
    private int lastX = 0;
    private int lastY = 0;
    private int tempX;
    private int tempY;


    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StepView, defStyleAttr, 0);
        mCompletedLineColor = array.getColor(R.styleable.StepView_completedLineColor, Color.WHITE);
        mUnCompletedLineColor = array.getColor(R.styleable.StepView_unCompletedLineColor, Color.WHITE);
        mCompletedTextColor = array.getColor(R.styleable.StepView_completedTextColor, Color.WHITE);
        mUnCompletedTextColor = array.getColor(R.styleable.StepView_unCompletedTextColor, Color.WHITE);
        mCompletedIcon = array.getDrawable(R.styleable.StepView_completedIcon);
        mUnCompletedIcon = array.getDrawable(R.styleable.StepView_unCompletedIcon);
        mCompletingIcon = array.getDrawable(R.styleable.StepView_completingIcon);
        mLineSize = (int) array.getDimension(R.styleable.StepView_lineSize, dip2px(context, 14));
        mTextSize = (int) array.getDimension(R.styleable.StepView_textSize, dip2px(context, 20));
        mType = array.getInteger(R.styleable.StepView_oriental, 1);
        array.recycle();

        mCompletedTextPaint = new TextPaint();
        mCompletedTextPaint.setColor(mCompletedTextColor);
        mCompletedTextPaint.setTextSize(mTextSize);
        mCompletedTextPaint.setAntiAlias(true);

        mUnCompletedTextPaint = new TextPaint();
        mUnCompletedTextPaint.setColor(mUnCompletedTextColor);
        mUnCompletedTextPaint.setTextSize(mTextSize);
        mUnCompletedTextPaint.setAntiAlias(true);

        mCompletedLinePaint = new Paint();
        mCompletedLinePaint.setColor(mCompletedLineColor);
        mCompletedLinePaint.setStrokeWidth(dip2px(context, 1));

        mUnCompletedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnCompletedLinePaint.setColor(mUnCompletedLineColor);
        //若不加下边的style设置，什么也画不出来
        mUnCompletedLinePaint.setStyle(Paint.Style.STROKE);
        mUnCompletedLinePaint.setStrokeWidth(dip2px(context, 1));
        mUnCompletedLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        mLinePath = new Path();

        mDrawablePaint = new Paint();

        mText = new ArrayList<>();
    }

    public void setText(List<String> text, int completingNum) {
        mText = text;
        mCompletingNum = completingNum;
        if (mType == 1) {
            int remain = (getWidth() - mText.size() * dip2px(mContext, 20) - (mText.size() - 1) * mLineSize);
            horizonStart = remain > 0 ? remain / 2 : 0;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //单纯的设置paint实现虚线效果，必须关闭硬件加速，会降低性能
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        if (mText != null && mText.size() > 0 && mCompletingNum <= mText.size()) {
            drawText(canvas);
            drawLine(canvas);
            drawDrawable(canvas);
        }
    }

    private void drawDrawable(Canvas canvas) {
        if (mCompletingNum == mText.size()) {
            if (mType == 1) {
                for (int i = 0; i < mText.size(); i++) {
                    Rect rect = new Rect(offsetX + horizonStart + i * (dip2px(mContext, 20) + mLineSize), dip2px(mContext, 20),
                            offsetX + horizonStart + i * (dip2px(mContext, 20) + mLineSize) + dip2px(mContext, 20), dip2px(mContext, 40));
                    mCompletedIcon.setBounds(rect);
                    mCompletedIcon.draw(canvas);
                }
            } else if (mType == 2) {
                for (int i = 0; i < mText.size(); i++) {
                    Rect rect = new Rect(dip2px(mContext, 20), offsetY + i * (dip2px(mContext, 20) + mLineSize),
                            dip2px(mContext, 40), offsetY + i * (dip2px(mContext, 20) + mLineSize) + dip2px(mContext, 20));
                    mCompletedIcon.setBounds(rect);
                    mCompletedIcon.draw(canvas);
                }
            } else {
                for (int i = 0; i < mText.size(); i++) {
                    Rect rect = new Rect(dip2px(mContext, 20), offsetY + i * (dip2px(mContext, 20) + mLineSize),
                            dip2px(mContext, 40), offsetY + i * (dip2px(mContext, 20) + mLineSize) + dip2px(mContext, 20));
                    mCompletedIcon.setBounds(rect);
                    mCompletedIcon.draw(canvas);
                }
            }
        } else {
            if (mType == 1) {
                for (int i = 0; i < mText.size(); i++) {
                    Rect rect = new Rect(offsetX + horizonStart + i * (dip2px(mContext, 20) + mLineSize), dip2px(mContext, 20),
                            offsetX + horizonStart + i * (dip2px(mContext, 20) + mLineSize) + dip2px(mContext, 20), dip2px(mContext, 40));
                    if (i < mCompletingNum) {
                        mCompletedIcon.setBounds(rect);
                        mCompletedIcon.draw(canvas);
                    } else if (i == mCompletingNum) {
                        mCompletingIcon.setBounds(rect);
                        mCompletingIcon.draw(canvas);
                    } else {
                        mUnCompletedIcon.setBounds(rect);
                        mUnCompletedIcon.draw(canvas);
                    }
                }
            } else if (mType == 2) {
                for (int i = 0; i < mText.size(); i++) {
                    Rect rect = new Rect(dip2px(mContext, 20), offsetY + i * (dip2px(mContext, 20) + mLineSize),
                            dip2px(mContext, 40), offsetY + i * (dip2px(mContext, 20) + mLineSize) + dip2px(mContext, 20));
                    if (i < mCompletingNum) {
                        mCompletedIcon.setBounds(rect);
                        mCompletedIcon.draw(canvas);
                    } else if (i == mCompletingNum) {
                        mCompletingIcon.setBounds(rect);
                        mCompletingIcon.draw(canvas);
                    } else {
                        mUnCompletedIcon.setBounds(rect);
                        mUnCompletedIcon.draw(canvas);
                    }
                }
            } else {
                for (int i = 0; i < mText.size(); i++) {
                    int j = mText.size() - 1 - i;
                    Rect rect = new Rect(dip2px(mContext, 20), offsetY + j * (dip2px(mContext, 20) + mLineSize),
                            dip2px(mContext, 40), offsetY + j * (dip2px(mContext, 20) + mLineSize) + dip2px(mContext, 20));
                    if (i < mCompletingNum) {
                        mCompletedIcon.setBounds(rect);
                        mCompletedIcon.draw(canvas);
                    } else if (i == mCompletingNum) {
                        mCompletingIcon.setBounds(rect);
                        mCompletingIcon.draw(canvas);
                    } else {
                        mUnCompletedIcon.setBounds(rect);
                        mUnCompletedIcon.draw(canvas);
                    }
                }
            }
        }
    }

    private void drawLine(Canvas canvas) {
        if (mType == 1) {
            for (int i = 0; i < mText.size() - 1; i++) {
                if (i < mCompletingNum) {
                    canvas.drawLine(offsetX + horizonStart + dip2px(mContext, 20) * (i + 1) + mLineSize * i, dip2px(mContext, 30),
                            offsetX + horizonStart + (dip2px(mContext, 20) + mLineSize) * (i + 1), dip2px(mContext, 30), mCompletedLinePaint);
                } else {
                    mLinePath.reset();
                    mLinePath.moveTo(offsetX + horizonStart + dip2px(mContext, 20) * (i + 1) + mLineSize * i, dip2px(mContext, 30));
                    mLinePath.lineTo(offsetX + horizonStart + (dip2px(mContext, 20) + mLineSize) * (i + 1), dip2px(mContext, 30));
                    canvas.drawPath(mLinePath, mUnCompletedLinePaint);
                }
            }

        } else if (mType == 2) {
            for (int i = 0; i < mText.size() - 1; i++) {
                if (i < mCompletingNum) {
                    canvas.drawLine(dip2px(mContext, 30), offsetY + dip2px(mContext, 20) * (i + 1) + mLineSize * i,
                            dip2px(mContext, 30), offsetY + (dip2px(mContext, 20) + mLineSize) * (i + 1), mCompletedLinePaint);
                } else {
                    //改用路径，支持硬件加速
                    mLinePath.reset();
                    mLinePath.moveTo(dip2px(mContext, 30), offsetY + dip2px(mContext, 20) * (i + 1) + mLineSize * i);
                    mLinePath.lineTo(dip2px(mContext, 30), offsetY + (dip2px(mContext, 20) + mLineSize) * (i + 1));
                    canvas.drawPath(mLinePath, mUnCompletedLinePaint);
//                    canvas.drawLine(dip2px(mContext, 30), dip2px(mContext, 20) * (i + 1) + mLineSize * i,
//                            dip2px(mContext, 30), (dip2px(mContext, 20) + mLineSize) * (i + 1), mUnCompletedLinePaint);
                }
            }
        } else {
            for (int i = 0; i < mText.size() - 1; i++) {
                int j = mText.size() - 1 - i;
                if (i < mCompletingNum) {
                    canvas.drawLine(dip2px(mContext, 30), offsetY + dip2px(mContext, 20) * j + mLineSize * (j - 1),
                            dip2px(mContext, 30), offsetY + (dip2px(mContext, 20) + mLineSize) * j, mCompletedLinePaint);
                } else {
                    mLinePath.reset();
                    mLinePath.moveTo(dip2px(mContext, 30), offsetY + dip2px(mContext, 20) * j + mLineSize * (j - 1));
                    mLinePath.lineTo(dip2px(mContext, 30), offsetY + (dip2px(mContext, 20) + mLineSize) * j);
                    canvas.drawPath(mLinePath, mUnCompletedLinePaint);
                }
            }

        }
    }

    private void drawText(Canvas canvas) {
        if (mType == 1) {
            for (int i = 0; i < mText.size(); i++) {
                canvas.save();
                if (i <= mCompletingNum) {
                    canvas.translate(offsetX + horizonStart + i * (dip2px(mContext, 20) + mLineSize), dip2px(mContext, 60));
                    StaticLayout myStaticLayout = new StaticLayout(mText.get(i), mCompletedTextPaint, mLineSize + dip2px(mContext, 20), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                } else {
                    canvas.translate(offsetX + horizonStart + i * (dip2px(mContext, 20) + mLineSize), dip2px(mContext, 60));
                    StaticLayout myStaticLayout = new StaticLayout(mText.get(i), mUnCompletedTextPaint, mLineSize + dip2px(mContext, 20), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                }
                canvas.restore();
            }

        } else if (mType == 2) {
            for (int i = 0; i < mText.size(); i++) {
                canvas.save();
                if (i <= mCompletingNum) {
                    canvas.translate(dip2px(mContext, 60), offsetY + i * (dip2px(mContext, 20) + mLineSize));
                    StaticLayout myStaticLayout = new StaticLayout(mText.get(i), mCompletedTextPaint, canvas.getWidth() - dip2px(mContext, 60), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                } else {
                    canvas.translate(dip2px(mContext, 60), offsetY + i * (dip2px(mContext, 20) + mLineSize));
                    StaticLayout myStaticLayout = new StaticLayout(mText.get(i), mUnCompletedTextPaint, canvas.getWidth() - dip2px(mContext, 60), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                }
                canvas.restore();
            }
        } else {
            for (int i = 0; i < mText.size(); i++) {
                int j = mText.size() - 1 - i;
                canvas.save();
                if (i <= mCompletingNum) {
                    canvas.translate(dip2px(mContext, 60), offsetY + j * (dip2px(mContext, 20) + mLineSize));
                    StaticLayout myStaticLayout = new StaticLayout(mText.get(i), mCompletedTextPaint, canvas.getWidth() - dip2px(mContext, 60), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                } else {
                    canvas.translate(dip2px(mContext, 60), offsetY + j * (dip2px(mContext, 20) + mLineSize));
                    StaticLayout myStaticLayout = new StaticLayout(mText.get(i), mUnCompletedTextPaint, canvas.getWidth() - dip2px(mContext, 60), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                }
                canvas.restore();
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mType == 1) {
            if ((getWidth() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize) > 0) {
                return false;
            }
        } else {
            if ((getHeight() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize) > 0) {
                return true;
            }
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mType == 1) {
                    offsetX = x - lastX + tempX;
                    if (offsetX > (getWidth() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize) && offsetX <= 0) {
                        invalidate();
                    }
                } else {
                    offsetY = y - lastY + tempY;
                    if (offsetY > (getHeight() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize) && offsetY <= 0) {
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mType == 1) {
                    tempX += x - lastX;
                    if (tempX > 0) {
                        tempX = 0;
                        offsetX = 0;
                        invalidate();
                    } else if (tempX < (getWidth() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize)) {
                        tempX = (getWidth() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize);
                        offsetX = (getWidth() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize);
                        invalidate();
                    }
                } else {
                    tempY += y - lastY;
                    if (tempY > 0) {
                        tempY = 0;
                        offsetY = 0;
                        invalidate();
                    } else if (tempY < (getHeight() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize)) {
                        tempY = (getHeight() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize);
                        offsetY = (getHeight() - mText.size() * dip2px(mContext, 20) - mText.size() * mLineSize);
                        invalidate();
                    }
                }
                break;
        }
        return true;
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
