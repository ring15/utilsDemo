package com.ring.utilsdemo.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ring on 2019/7/5.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler mCrashHandler = new CrashHandler();
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    private Context mContext;

    /**
     * 在application中初始化即可
     * @return
     */
    public static CrashHandler getInstance() {
        return mCrashHandler;
    }

    /**
     * 设置为线程默认异常处理器
     * @param context
     */
    public void init(Context context) {
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 关键方法，通过此方法获取crash信息，并进行相应的操作，如下是将crash信息保存到本地文件，还可以选择上传等
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpExceptionToSDCard(e);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        e.printStackTrace();
        if (mExceptionHandler != null) {
            mExceptionHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * 以时间为文件名，将crash信息保存到本地文件
     * @param exception
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpExceptionToSDCard(Throwable exception) throws PackageManager.NameNotFoundException {
        try {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "CrashInfo");
                if (dir.exists() || dir.mkdir()) {
                    long current = System.currentTimeMillis();
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(current));
                    File file = new File(dir.getPath() + "/" + "crash" + time + ".trace");
                    PrintWriter printWriter = null;
                    printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    printWriter.println(time);
                    dumpPhoneInfo(printWriter);
                    printWriter.println();
                    exception.printStackTrace(printWriter);
                    printWriter.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一下设备的信息
     * @param printWriter
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter printWriter) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        printWriter.print("App Version: ");
        printWriter.print(packageInfo.versionName);

        printWriter.print("OS Version: ");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print("_");
        printWriter.println(Build.VERSION.SDK_INT);

        printWriter.print("Model: ");
        printWriter.println(Build.MODEL);

        printWriter.print("CPU ABI: ");
        printWriter.println(Build.CPU_ABI);
    }
}
