package com.ring.utilsdemo.weight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.ring.utilsdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ring.utilsdemo.weight.DrawHelperUtils.drawPartCircle;


/**
 * Created by ring on 2019/8/9.
 */
public class SliderVerifyView extends AppCompatImageView {

    private Context mContext;

    //初始控件宽高
    private int mWidth;
    private int mHeight;
    //验证码滑块的宽高
    private int mSliderWidth;
    private int mSliderHeight;
    //验证的误差允许值
    private float mDeviation;

    //滑块Bitmap
    private Bitmap mMaskBitmap;
    private Paint mMaskPaint;
    private Paint mPaint;

    //用于绘制阴影的Paint
    private Paint mMaskShadowPaint;
    private Bitmap mMaskShadowBitmap;

    //验证滑块随机位置
    private int randomX;
    private int randomY;
    private Random random;
    private List<Boolean> isOuters = new ArrayList<>();

    //设置滑动偏移量
    private int offset;

    //回调
    private OnCheckResult mCheckResult;

    private Path mCaptchaPath;

    private boolean isFailed = false;
    private boolean isShowSuccessAnim = false;
    private Paint mSuccessPaint;//画笔
    private int mSuccessAnimOffset;//动画的offset
    private Path mSuccessPath;//成功动画 平行四边形Path

    private boolean isFirst = true;


    public SliderVerifyView(Context context) {
        this(context, null);
    }

    public SliderVerifyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderVerifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        int defaultSize = dip2px(context, 16);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SliderVerifyView, defStyleAttr, 0);
        mSliderWidth = (int) array.getDimension(R.styleable.SliderVerifyView_sliderWidth, defaultSize);
        mSliderHeight = (int) array.getDimension(R.styleable.SliderVerifyView_sliderHeight, defaultSize);
        mDeviation = (int) array.getDimension(R.styleable.SliderVerifyView_deviation, dip2px(context, 3));
        array.recycle();

        mMaskPaint = new Paint();
        mMaskPaint.setShadowLayer(10, 0, 0, Color.RED);

        mPaint = new Paint();
        mPaint.setShadowLayer(10, 0, 0, Color.RED);
        mPaint.setColor(Color.parseColor("#77000000"));

        random = new Random(System.currentTimeMillis());
        if (isOuters != null) {
            isOuters.clear();
        }
        for (int i = 0; i < 4; i++) {
            isOuters.add(random.nextBoolean());
        }

        mCaptchaPath = new Path();
        mMaskShadowPaint = new Paint();
        mMaskShadowPaint.setStyle(Paint.Style.STROKE);
        mMaskShadowPaint.setStrokeWidth(5);
        mMaskShadowPaint.setColor(Color.parseColor("#33000000"));

    }

    //和下边的onMeasure实现定点效果基本类似
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        mWidth = w;
//        mHeight = h;
//        randomX = random.nextInt(mWidth) % (mWidth - mSliderWidth + 1);
//        randomY = random.nextInt(mHeight) % (mHeight - mSliderHeight + 1);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        randomX = random.nextInt(mWidth) % (mWidth - mSliderWidth + 1);
        randomY = random.nextInt(mHeight) % (mHeight - mSliderHeight + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isFirst) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                canvas.saveLayer(0, 0, mWidth, mHeight, mPaint);
//            } else {
//                canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
//            }
//            createPath(canvas, mPaint);
            createCaptchaPath();
            //生成验证滑块
            mMaskBitmap = createSliderBitmap();
            isFirst = false;
        }
        canvas.drawPath(mCaptchaPath, mPaint);
        canvas.drawPath(mCaptchaPath, mMaskShadowPaint);
        if (!isFailed) {
            canvas.drawBitmap(mMaskBitmap, offset - randomX, 0, null);
        }
        if (isShowSuccessAnim) {
            canvas.translate(mSuccessAnimOffset, 0);
            canvas.drawPath(mSuccessPath, mSuccessPaint);
        }

    }

    private Bitmap createSliderBitmap() {
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
//        createPath(canvas, mMaskPaint);
        canvas.drawPath(mCaptchaPath, mMaskPaint);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, getImageMatrix(), mMaskPaint);
        mMaskPaint.setXfermode(null);
        canvas.drawPath(mCaptchaPath, mMaskShadowPaint);
        return tempBitmap;
    }

    private void createPath(Canvas canvas, Paint paint) {
        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        int circleRadius = (mSliderHeight - mSliderWidth) > 0 ? mSliderWidth / 6 : mSliderHeight / 6;
        Rect rect = new Rect(randomX + circleRadius, randomY + circleRadius, randomX + mSliderWidth - circleRadius, randomY + mSliderHeight);
        canvas.drawRect(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawCircle(randomX + mSliderWidth / 2, randomY + circleRadius, circleRadius, paint);
        canvas.drawCircle(randomX + circleRadius, randomY + (mSliderHeight + circleRadius) / 2, circleRadius, paint);
        canvas.drawCircle(randomX + mSliderWidth - circleRadius, randomY + (mSliderHeight + circleRadius) / 2, circleRadius, paint);
        paint.setColor(Color.parseColor("#000000"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawCircle(randomX + mSliderWidth / 2, randomY + mSliderHeight, circleRadius, paint);
        paint.setXfermode(null);
        mPaint.setColor(Color.parseColor("#77000000"));
    }


    //生成验证码Path
    private void createCaptchaPath() {
        //原本打算随机生成gap，后来发现 宽度/3 效果比较好，
        int gap;
        gap = mSliderWidth / 3;

        mCaptchaPath.reset();
        mCaptchaPath.lineTo(0, 0);


        //从左上角开始 绘制一个不规则的阴影
        mCaptchaPath.moveTo(randomX, randomY);//左上角


/*        mCaptchaPath.lineTo(randomX + gap, randomY);
        //画出凹凸 由于是多段Path 无法闭合，简直阿西吧
        int r = mSliderWidth / 2 - gap;
        RectF oval = new RectF(randomX + gap, randomY - (r), randomX + gap + r * 2, randomY + (r));
        mCaptchaPath.arcTo(oval, 180, 180);*/

        mCaptchaPath.lineTo(randomX + gap, randomY);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(randomX + gap, randomY),
                new PointF(randomX + gap * 2, randomY),
                mCaptchaPath, isOuters.get(0));


        mCaptchaPath.lineTo(randomX + mSliderWidth, randomY);//右上角
        mCaptchaPath.lineTo(randomX + mSliderWidth, randomY + gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(randomX + mSliderWidth, randomY + gap),
                new PointF(randomX + mSliderWidth, randomY + gap * 2),
                mCaptchaPath, isOuters.get(1));


        mCaptchaPath.lineTo(randomX + mSliderWidth, randomY + mSliderHeight);//右下角
        mCaptchaPath.lineTo(randomX + mSliderWidth - gap, randomY + mSliderHeight);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(randomX + mSliderWidth - gap, randomY + mSliderHeight),
                new PointF(randomX + mSliderWidth - gap * 2, randomY + mSliderHeight),
                mCaptchaPath, isOuters.get(2));


        mCaptchaPath.lineTo(randomX, randomY + mSliderHeight);//左下角
        mCaptchaPath.lineTo(randomX, randomY + mSliderHeight - gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(randomX, randomY + mSliderHeight - gap),
                new PointF(randomX, randomY + mSliderHeight - gap * 2),
                mCaptchaPath, isOuters.get(3));


        mCaptchaPath.close();

/*        RectF oval = new RectF(randomX + gap, randomY - (r), randomX + gap + r * 2, randomY + (r));
        mCaptchaPath.addArc(oval, 180,180);
        mCaptchaPath.lineTo(randomX + mSliderWidth, randomY);
        //凹的话，麻烦一点，要利用多次move
        mCaptchaPath.lineTo(randomX + mSliderWidth, randomY + gap);
        oval = new RectF(randomX + mSliderWidth - r, randomY + gap, randomX + mSliderWidth + r, randomY + gap + r * 2);
        mCaptchaPath.addArc(oval, 90, 180);
        mCaptchaPath.moveTo(randomX + mSliderWidth, randomY + gap + r * 2);*//*
        mCaptchaPath.lineTo(randomX + mSliderWidth, randomY + mSliderHeight);
        mCaptchaPath.lineTo(randomX, randomY + mSliderHeight);
        mCaptchaPath.close();*/
    }

    public void setOffset(int progress) {
        //按百分比计算的话，会出现一点点偏差，导致两个不能完全重合
//        offset = ((mWidth - mSliderWidth) * progress) / 100;
        offset = progress;
        invalidate();
    }

    public int maxWidth() {
        return mWidth - mSliderHeight;
    }

    public void check() {
        if ((offset - randomX) > -mDeviation && (offset - randomX) < mDeviation) {

            int width = dip2px(mContext, 100);

            mSuccessPaint = new Paint();
            mSuccessPaint.setShader(new LinearGradient(0, 0, width / 2 * 3, mHeight, new int[]{
                    0x00ffffff, 0x88ffffff}, new float[]{0, 0.5f},
                    Shader.TileMode.MIRROR));
            //模仿斗鱼 是一个平行四边形滚动过去
            mSuccessPath = new Path();
            mSuccessPath.moveTo(0, 0);
            mSuccessPath.rLineTo(width, 0);
            mSuccessPath.rLineTo(width / 2, mHeight);
            mSuccessPath.rLineTo(-width, 0);
            mSuccessPath.close();
            ValueAnimator valueAnimator = ValueAnimator.ofInt(mWidth + width, 0);
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new FastOutLinearInInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSuccessAnimOffset = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isShowSuccessAnim = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isShowSuccessAnim = false;
                    mCheckResult.onSuccess();
                }
            });
            valueAnimator.start();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(200).setRepeatCount(4);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCheckResult.onFailed();
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedValue = (float) valueAnimator.getAnimatedValue();
                    if (animatedValue < 0.5) {
                        isFailed = true;
                    } else {
                        isFailed = false;
                    }
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }

    public void setCheckResultListen(OnCheckResult checkResult) {
        mCheckResult = checkResult;
    }

    public interface OnCheckResult {
        void onSuccess();

        void onFailed();
    }

    public void resetSlider() {
        random = new Random(System.currentTimeMillis());
        randomX = random.nextInt(mWidth) % (mWidth - mSliderWidth + 1);
        randomY = random.nextInt(mHeight) % (mHeight - mSliderHeight + 1);
        if (isOuters != null) {
            isOuters.clear();
        }
        for (int i = 0; i < 4; i++) {
            isOuters.add(random.nextBoolean());
        }
        offset = 0;
        isFirst = true;
        invalidate();
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
