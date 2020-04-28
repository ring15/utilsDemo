package com.ring.utilsdemo.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by ring on 2020/4/28.
 */
public class CacheUtil {

    private final static String TAG = "CacheUtil";
    private final static String CACHE_NAME = "cache_name";

    private static ACache mACache;

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        mACache = ACache.get(applicationContext);
    }

    public static void putString(String string) {
        if (mACache != null) {
            mACache.put(CACHE_NAME, string);
        } else {
            Log.e(TAG, "mACache is null");
        }
    }

    public static String getString() {
        if (mACache != null) {
            return mACache.getAsString(CACHE_NAME);
        } else {
            Log.e(TAG, "mACache is null");
            return null;
        }
    }
}
