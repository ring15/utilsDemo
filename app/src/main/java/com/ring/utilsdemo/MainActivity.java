package com.ring.utilsdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ring.utilsdemo.observe.Consumer;
import com.ring.utilsdemo.observe.Emitter;
import com.ring.utilsdemo.observe.Function;
import com.ring.utilsdemo.observe.Observable;
import com.ring.utilsdemo.observe.ObservableOnSubscribe;
import com.ring.utilsdemo.observe.Observer;
import com.ring.utilsdemo.utils.EquipmentUtil;
import com.ring.utilsdemo.utils.MacUtil;
import com.ring.utilsdemo.utils.NetUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetUtils.init(this);
    }

    private void observe() {

        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(Emitter<String> emitter) throws Exception {
//                        mModel.requestRegister(mobile, mobileCodeKey, emitter);//数据请求
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String user) throws Exception {
//                        user.setUserType(Constants.REQUEST_TYPE_USER);//类型转换
                        return user;
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String user) throws Exception {
//                        mModel.saveUser(user);//缓存操作
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String value) {
//                        if (isViewAttached()) {
//                            mView.startNextActivity();
//                        }//成功的逻辑处理
                    }

                    @Override
                    public void onError(Exception error) {
//                        if (isViewAttached()) {
//                            mView.showErrorMsg(error);
//                        }//异常的逻辑处理
                    }
                });
    }

    private void init() {
        String TAG = "系统参数：";
        Log.i(TAG, "手机厂商：" + EquipmentUtil.getDeviceBrand());
        Log.i(TAG, "手机型号：" + EquipmentUtil.getSystemModel());
        Log.i(TAG, "手机当前系统语言：" + EquipmentUtil.getSystemLanguage());
        Log.i(TAG, "Android系统版本号：" + EquipmentUtil.getSystemVersion());

        Log.i(TAG, "手机设备名：" + EquipmentUtil.getSystemDevice());
        Log.i(TAG, "主板名：" + EquipmentUtil.getDeviceBoand());
        Log.i(TAG, "手机厂商名：" + EquipmentUtil.getDeviceManufacturer());

        Log.i(TAG, "id：" + EquipmentUtil.getID());
        Log.i(TAG, "版本号：" + EquipmentUtil.getDisplay());
        Log.i(TAG, "产品名称：" + EquipmentUtil.getProduct());
        Log.i(TAG, "系统程序的版本号：" + EquipmentUtil.getBootloader());

        Log.i(TAG, "硬件名称：" + EquipmentUtil.getHardware());
        Log.i(TAG, "序列号：" + EquipmentUtil.getSerial(this));
        Log.i(TAG, "设备版本类型：" + EquipmentUtil.getType());
        Log.i(TAG, "设备标签：" + EquipmentUtil.getTags());

        Log.i(TAG, "设备标识：" + EquipmentUtil.getFingerprint());
        Log.i(TAG, "设备主机地址：" + EquipmentUtil.getHost());
        Log.i(TAG, "设备用户名：" + EquipmentUtil.getUser());
        Log.i(TAG, "当前时间：" + EquipmentUtil.getTime());

        Log.i(TAG, "INCREMENTAL（源码控制版本号）：" + EquipmentUtil.getIncremental());
        Log.i(TAG, "SDK版本号：" + EquipmentUtil.getSDK());
        Log.i(TAG, "CODENAME(设备当前的系统开发代号)：" + EquipmentUtil.getCodeName());
        Log.i(TAG, "BASE_OS（目前的开发代号）：" + EquipmentUtil.getBaseOS() + "(23以上版本)");


        Log.i(TAG, "imei：" + EquipmentUtil.getIMEI(this) + "(需要权限READ_PHONE_STATE)");
        Log.i(TAG, "ipv4地址：" + EquipmentUtil.getIPAddress(this) + "(需要权限ACCESS_NETWORK_STATE,  ACCESS_WIFI_STATE, 23及以下还需要INTERNET权限)");
        Log.i(TAG, "mac地址：" + MacUtil.getMac(this) + "(需要权限INTERNET,  ACCESS_WIFI_STATE)");

        Log.i(TAG, "屏幕宽度:" + EquipmentUtil.getWidth(this));
        Log.i(TAG, "屏幕高度:" + EquipmentUtil.getHeight(this));
        Log.i(TAG, "屏幕密度:" + EquipmentUtil.getDensity(this));
        Log.i(TAG, "获取CPU型号:" + EquipmentUtil.getCpuInfo()[0] + ", " + EquipmentUtil.getCpuInfo()[1]);
        Log.i(TAG, "获取运行内存:" + EquipmentUtil.getRAM(this));
        Log.i(TAG, "获取存储内存:" + EquipmentUtil.getROM(this));

        Log.i(TAG, "网络状态:" + NetUtils.getNetworkState());
        Log.i(TAG, "IMSI和运营商:" + EquipmentUtil.getIMSI(this));
        Log.i(TAG, "运营商:" + EquipmentUtil.getCarrier(this));
        Log.i(TAG, "蓝牙地址:" + EquipmentUtil.getBluetoothMacAddress());
        Log.i(TAG, "wifi信息:" + EquipmentUtil.getWifiName(this));
        Log.i(TAG, "ipv6:" + EquipmentUtil.getIpv6Addr());
        Log.i(TAG, "设备ID:" + EquipmentUtil.getAndroidID(this));
        Log.i(TAG, "UserAgent:" + EquipmentUtil.getUserAgent(this));
        Log.i(TAG, "VPN:" + (EquipmentUtil.isVpnUsed() ? "使用VPN" : "未使用VPN"));
        Log.i(TAG, "地区（ISM）:" + EquipmentUtil.getLocation(this));
        Log.i(TAG, "地区（net）:" + EquipmentUtil.getLocationNet(this));
        Log.i(TAG, "时区:" + EquipmentUtil.getTimeZone());

        String message = "系统参数：" + "\n" + "手机厂商：" + EquipmentUtil.getDeviceBrand() + "\n" +
                "手机型号：" + EquipmentUtil.getSystemModel() + "\n" + "手机当前系统语言：" + EquipmentUtil.getSystemLanguage() +
                "\n" + "Android系统版本号：" + EquipmentUtil.getSystemVersion() + "\n" + "手机设备名：" + EquipmentUtil.getSystemDevice() +
                "\n" + "主板名：" + EquipmentUtil.getDeviceBoand() + "\n" + "手机厂商名：" + EquipmentUtil.getDeviceManufacturer() +
                "\n" + "id：" + EquipmentUtil.getID() + "\n" + "版本号：" + EquipmentUtil.getDisplay() + "\n" +
                "产品名称：" + EquipmentUtil.getProduct() + "\n" + "系统程序的版本号：" + EquipmentUtil.getBootloader() + " \n" +
                "硬件名称：" + EquipmentUtil.getHardware() + "\n" + "序列号：" + EquipmentUtil.getSerial(this) + "\n" +
                "设备版本类型：" + EquipmentUtil.getType() + "\n" + "设备标签：" + EquipmentUtil.getTags() + "\n" +
                "设备标识：" + EquipmentUtil.getFingerprint() + "\n" + "设备主机地址：" + EquipmentUtil.getHost() + "\n" +
                "设备用户名：" + EquipmentUtil.getUser() + "\n" + "当前时间：" + EquipmentUtil.getTime() + "\n" +
                "INCREMENTAL（源码控制版本号）：" + EquipmentUtil.getIncremental() + "\n" + "SDK版本号：" + EquipmentUtil.getSDK() + "\n" +
                "CODENAME(设备当前的系统开发代号)：" + EquipmentUtil.getCodeName() + "\n" +
                "BASE_OS（目前的开发代号）：" + EquipmentUtil.getBaseOS() + "(23以上版本)" + "\n" +
                "imei：" + EquipmentUtil.getIMEI(this) + "(需要权限READ_PHONE_STATE)" + "\n" +
                "ipv4地址：" + EquipmentUtil.getIPAddress(this) + "(需要权限ACCESS_NETWORK_STATE,  ACCESS_WIFI_STATE, 23及以下还需要INTERNET权限)" + "\n" +
                "mac地址：" + MacUtil.getMac(this) + "(需要权限INTERNET,  ACCESS_WIFI_STATE)" + "\n" +
                "屏幕宽度:" + EquipmentUtil.getWidth(this) + "\n" +
                "屏幕高度:" + EquipmentUtil.getHeight(this) + "\n" +
                "屏幕密度:" + EquipmentUtil.getDensity(this) + "\n" +
                "获取CPU型号:" + EquipmentUtil.getCpuInfo()[0] + ", " + EquipmentUtil.getCpuInfo()[1] + "\n" +
                "获取运行内存:" + EquipmentUtil.getRAM(this) + "\n" +
                "获取存储内存:" + EquipmentUtil.getROM(this) + "\n" +
                "网络状态:" + NetUtils.getNetworkState() + "\n" +
                "IMSI和运营商:" + EquipmentUtil.getIMSI(this) + "\n" +
                "运营商:" + EquipmentUtil.getCarrier(this) + "\n" +
                "蓝牙地址:" + EquipmentUtil.getBluetoothMacAddress() + "\n" +
                "wifi信息:" + EquipmentUtil.getWifiName(this) + "\n" +
                "ipv6:" + EquipmentUtil.getIpv6Addr() + "\n" +
                "设备ID:" + EquipmentUtil.getAndroidID(this) + "\n" +
                "UserAgent:" + EquipmentUtil.getUserAgent(this) + "\n" +
                "VPN:" + (EquipmentUtil.isVpnUsed() ? "使用VPN" : "未使用VPN") + "\n" +
                "地区（ISM）:" + EquipmentUtil.getLocation(this) + "\n" +
                "地区（net）:" + EquipmentUtil.getLocationNet(this) + "\n" +
                "时区:" + EquipmentUtil.getTimeZone() + "\n";

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label", message);
        clipboardManager.setPrimaryClip(clipData);
    }
}
