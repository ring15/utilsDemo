package com.ring.utilsdemo.utils.keyboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * 类描述：防止软键盘弹出时挡住相关按钮或布局
 * 创建人：huangyaobin
 * 创建时间：2019/6/13
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class PreventKeyboardBlockUtil {

    static PreventKeyboardBlockUtil preventKeyboardBlockUtil;
    static Activity mActivity;
    static View mBtnView;
    static ViewGroup rootView;
    static boolean isMove;
    static int marginBottom = 0;
    static KeyboardHeightProvider keyboardHeightProvider;
    int keyBoardHeight = 0;
    int btnViewY = 0;
    boolean isRegister = false;
    AnimatorSet animSet = new AnimatorSet();

    public static PreventKeyboardBlockUtil getInstance(Activity activity) {
        if (preventKeyboardBlockUtil == null) {
            preventKeyboardBlockUtil = new PreventKeyboardBlockUtil();
        }

        initData(activity);

        return preventKeyboardBlockUtil;
    }

    private static void initData(Activity activity) {
        mActivity = activity;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        rootView = (ViewGroup) ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        isMove = false;
        marginBottom = 0;
        if (keyboardHeightProvider != null) {
            keyboardHeightProvider.recycle();
            keyboardHeightProvider = null;
        }
        keyboardHeightProvider = new KeyboardHeightProvider(mActivity);
    }

    public PreventKeyboardBlockUtil setBtnView(View btnView) {
        mBtnView = btnView;
        return preventKeyboardBlockUtil;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startAnim(msg.arg1);
        }
    };

    void startAnim(int transY) {
        float curTranslationY = rootView.getTranslationY();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(rootView, "translationY", curTranslationY, transY);
        animSet.play(objectAnimator);
        animSet.setDuration(200);
        animSet.start();
    }

    public void register() {

        isRegister = true;

        keyboardHeightProvider.setKeyboardHeightObserver(new KeyboardHeightObserver() {
            @Override
            public void onKeyboardHeightChanged(int height, int orientation) {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    return;
                }
                if (!isRegister) {
                    return;
                }

                if (keyBoardHeight == height) {
                    return;
                } else {
                    keyBoardHeight = height;
                }

                if (keyBoardHeight <= 0) {//键盘收起
                    if (isMove) {

                        sendHandlerMsg(0);

                        isMove = true;
                    }
                } else {//键盘打开

                    int keyBorardTopY = getAppScreenHeight() - keyBoardHeight;
                    if (keyBorardTopY > (btnViewY + mBtnView.getHeight())) {
                        return;
                    }
                    int margin = keyBorardTopY - (btnViewY + mBtnView.getHeight());
                    Log.i("tag", "margin:" + margin);
                    sendHandlerMsg(margin);

                    isMove = true;
                }

            }
        });

        mBtnView.post(new Runnable() {
            @Override
            public void run() {
                btnViewY = getViewLocationYInScreen(mBtnView);
                keyboardHeightProvider.start();
            }
        });

    }

    public static int getAppScreenHeight() {
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public void unRegister() {
        isRegister = false;
        hideSoftInput(mActivity.getWindow());
        keyBoardHeight = 0;
        sendHandlerMsg(0);

        if (keyboardHeightProvider != null) {
            keyboardHeightProvider.setKeyboardHeightObserver(null);
            keyboardHeightProvider.close();
        }
    }

    /**
     * Hide the soft input.
     *
     * @param window The window.
     */
    public static void hideSoftInput(@NonNull final Window window) {
        View view = window.getCurrentFocus();
        if (view == null) {
            View decorView = window.getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(window.getContext());
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(view);
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    public static void hideSoftInput(@NonNull final View view) {
        InputMethodManager imm =
                (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void recycle() {
        mActivity = null;
        if (keyboardHeightProvider != null) {
            keyboardHeightProvider.recycle();
            keyboardHeightProvider = null;
        }

    }

    private void sendHandlerMsg(int i) {
        Message message = new Message();
        message.arg1 = i;
        mHandler.sendMessage(message);
    }

    private int getViewLocationYInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

}
