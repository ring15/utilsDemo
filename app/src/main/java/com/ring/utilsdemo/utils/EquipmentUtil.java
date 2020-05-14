package com.ring.utilsdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by ring on 2019/7/22.
 */
public class EquipmentUtil {


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机设备名
     *
     * @return 手机设备名
     */
    public static String getSystemDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机主板名
     *
     * @return 主板名
     */
    public static String getDeviceBoand() {
        return Build.BOARD;
    }


    /**
     * 获取手机厂商名（硬件制造商）
     *
     * @return 手机厂商名
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取id（修订版本列表）
     *
     * @return id
     */
    public static String getID() {
        return Build.ID;
    }

    /**
     * 获取手机版本号(显示屏参数)
     *
     * @return 版本号
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 获取手机产品名称
     *
     * @return 产品名称
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取手机系统程序的版本号
     *
     * @return 系统程序的版本号
     */
    public static String getBootloader() {
        return Build.BOOTLOADER;
    }

    /**
     * 获取手机硬件名称
     *
     * @return 硬件名称
     */
    public static String getHardware() {
        return Build.HARDWARE;
    }

    /**
     * 获取手机序列号
     *
     * @return 序列号
     */
    public static String getSerial(Context context) {
        String serial = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 9.0 +
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) { // 8.0 +
                serial = Build.SERIAL;
            } else { // 8.0 -
                Class c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 获取手机设备版本类型(Builder类型)
     *
     * @return 设备版本类型
     */
    public static String getType() {
        return Build.TYPE;
    }

    /**
     * 获取手机设备标签
     *
     * @return 设备标签
     */
    public static String getTags() {
        return Build.TAGS;
    }

    /**
     * 获取手机设备标识(唯一编号)
     *
     * @return 设备标识
     */
    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取手机设备主机地址
     *
     * @return HOST
     */
    public static String getHost() {
        return Build.HOST;
    }

    /**
     * 获取手机设备用户名
     *
     * @return USER
     */
    public static String getUser() {
        return Build.USER;
    }

    /**
     * 获取编译时间
     *
     * @return TIME
     */
    public static long getTime() {
        return Build.TIME;
    }

    /**
     * 获取手机INCREMENTAL（小米上返回MIUI版本）（源码控制版本号）
     *
     * @return INCREMENTAL
     */
    public static String getIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * 获取手机SDK_INT（SDK版本号）
     *
     * @return SDK_INT
     */
    public static int getSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机CODENAME(设备当前的系统开发代号)
     *
     * @return CODENAME
     */
    public static String getCodeName() {
        return Build.VERSION.CODENAME;
    }

    /**
     * 获取手机BASE_OS（目前的开发代号）
     *
     * @return BASE_OS
     */
    public static String getBaseOS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Build.VERSION.BASE_OS;
        } else {
            return null;
        }
    }

    /**
     * 获取手机androidID
     *
     * @param context 上下文
     * @return androidID
     */
    public static String getAndroidID(Context context) {
        String androidID = "";
        try {
            androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidID;
    }


    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取手机IMSI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMSI
     */
    public static String getIMSI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String IMSI = tm.getSubscriberId();
        String ProvidersName = "";
        //460是国家代码，后面是运营商代码
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007") || IMSI.startsWith("46008")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006") || IMSI.startsWith("46009")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005") || IMSI.startsWith("46011")) {
                ProvidersName = "中国电信";
            } else if (IMSI.startsWith("46004")) {
                ProvidersName = "中国卫通";
            } else if (IMSI.startsWith("46020")) {
                ProvidersName = "中国铁通";
            } else {
                ProvidersName = "其他";
            }
        }
        return "imsi:" + IMSI + ", 运营商:" + ProvidersName;
    }


    /**
     * 获取IP地址，需要权限ACCESS_NETWORK_STATE和ACCESS_WIFI_STATE
     *
     * @param context 上下文
     * @return ipv4地址
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取蓝牙地址
     */
    public static String getBluetoothMacAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (bluetoothAdapter == null) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;
    }

    /**
     * 获取CPU型号
     *
     * @return cpu信息
     */
    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};// 0-cpu型号 //1-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    /**
     * 获取运行内存信息
     *
     * @param context 上下文
     * @return 运行内存信息
     */
    public static String getRAM(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return "可用RAM：" + Formatter.formatFileSize(context, info.availMem) + ", 总RAM:"
                + Formatter.formatFileSize(context, info.totalMem);
    }

    /**
     * 获取存储内存
     *
     * @return 存储内存信息
     */
    public static String getROM(Context context) {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long totalCounts = statFs.getBlockCountLong();
        long availableCounts = statFs.getAvailableBlocksLong();
        long size = statFs.getBlockSizeLong();
        long availROMSize = availableCounts * size;
        long totalROMSize = totalCounts * size;
        return "可用ROM：" + Formatter.formatFileSize(context, availROMSize) + ", 总ROM:"
                + Formatter.formatFileSize(context, totalROMSize);
    }

    /**
     * 获取WiFi名称
     *
     * @param context 上下文
     * @return wifi名，不是wifi连接返回<unknown ssid>
     */
    public static String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            int networkId = wifiInfo.getNetworkId();
            List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
            if (configurations != null && configurations.size() > 0) {
                for (WifiConfiguration wifiConfiguration : configurations) {
                    if (wifiConfiguration.networkId == networkId) {
                        ssid = wifiConfiguration.SSID;
                        break;
                    }
                }
            }
            return ssid;
        }
        return null;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity 上下文，只能是activity
     * @return 屏幕宽度
     */
    public static int getWidth(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity 上下文，只能是activity
     * @return 屏幕高度
     */
    public static int getHeight(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取屏幕密度
     *
     * @param activity 上下文，只能是activity
     * @return 屏幕密度
     */
    public static float getDensity(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

    /**
     * 方法名：getIpv6Addr(String command)
     * * 参数：  shell下需要执行的命令，例如： /system/bin/ip -6 addr show
     * * 功能：  1、获取Ipv6地址，并统计地址的个数
     *
     * @return ipv6地址
     */
    public static String getIpv6Addr() {
        String command = "/system/bin/ip -6 addr show "; // 默认获取Ipv6地址的shell命令
        int IPV6LEN_LEFT_BIT = 6; // 默认，截取的Ipv6字符串左起始位
        int IPV6LEN_RIGHT_BIT = 1; // 默认，截取的Ipv6字符串右结束位
        String ipv6addrTemp = "";
        List<String> ipv6s = new ArrayList<>();

        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(command);
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if ((line.contains("inet6")) && (line.contains("scope"))) {
                    ipv6addrTemp = line.substring(line.indexOf("inet6") + IPV6LEN_LEFT_BIT, line.lastIndexOf("scope") - IPV6LEN_RIGHT_BIT);
                    ipv6s.add(ipv6addrTemp);
                }
            }
        } catch (IOException e) {

        }
        String ipv6AddrString = "";
        if (ipv6s.size() > 0) {
            for (int i = 0; i < ipv6s.size(); i++) {
                ipv6AddrString = ipv6AddrString + "\n" + i + "、" + ipv6s.get(i);
            }
        }
        return ipv6AddrString;
    }

    /**
     * 获取运营商信息
     *
     * @param context 上下文
     * @return 运营商信息
     */
    public static String getCarrier(Context context) {
        final Map<String, String> carrierMap = new HashMap<String, String>() {
            {
                put("46000", "中国移动");
                put("46002", "中国移动");
                put("46007", "中国移动");
                put("46008", "中国移动");

                put("46001", "中国联通");
                put("46006", "中国联通");
                put("46009", "中国联通");

                put("46003", "中国电信");
                put("46005", "中国电信");
                put("46011", "中国电信");

                put("46004", "中国卫通");

                put("46020", "中国铁通");

            }
        };

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simOperator = tm.getSimOperator();
            if (!TextUtils.isEmpty(simOperator) && carrierMap.containsKey(simOperator)) {
                return carrierMap.get(simOperator);
            }

            String simOperatorName = tm.getSimOperatorName();
            if (!TextUtils.isEmpty(simOperatorName)) {
                return simOperatorName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getLocation(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimCountryIso();
    }

    public static String getLocationNet(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkCountryIso();
    }

    public static String getTimeZone() {
        Calendar mDummyDate;
        mDummyDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        mDummyDate.setTimeZone(now.getTimeZone());
        mDummyDate.set(now.get(Calendar.YEAR), 11, 31, 13, 0, 0);
        return getTimeZoneText(now.getTimeZone(), true);
    }

    public static String getTimeZoneText(TimeZone tz, boolean includeName) {
        Date now = new Date();

        SimpleDateFormat gmtFormatter = new SimpleDateFormat("ZZZZ");
        gmtFormatter.setTimeZone(tz);
        String gmtString = gmtFormatter.format(now);
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        Locale l = Locale.getDefault();
        boolean isRtl = TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL;
        gmtString = bidiFormatter.unicodeWrap(gmtString,
                isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);

        if (!includeName) {
            return gmtString;
        }

        return gmtString;
    }
}
