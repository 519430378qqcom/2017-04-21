package cn.youmi.framework.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.youmi.framework.main.BaseApplication;

public class SystemUtils {

    /**
     * 获取手机型号
     *
     * @return 型号
     */
    public static String getModel() {
        return URLCoderUtils.URLEncoder(Build.MODEL);
    }

    /**
     * 获取手机Mac地址，并进行MD5加密，用于判断deviceid
     *
     * @return
     */
    public static String getMacMD5() {
        // 获取设备的mac地址
        WifiManager wifi = (WifiManager) BaseApplication.getApplication()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null && info.getMacAddress() != null) {
                return encodeMD5(info.getMacAddress());
            } else {
                return "error";
            }
        } else {
            return "error";
        }

    }

    /**
     * 获取应用版本号
     */
    public static String getVersionName() {
        String version = "0";
        try {
            PackageManager packageManager = BaseApplication.getApplication()
                    .getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    BaseApplication.getApplication().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取应用的versionCode 用更新次数判断大小 是否升级
     */
    public static int getVersionCode() {
        int verCode = -1;
        try {
            PackageManager packageManager = BaseApplication.getApplication()
                    .getPackageManager();
            verCode = packageManager.getPackageInfo(BaseApplication
                    .getApplication().getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    public static String getAppName(String appName) {
        PackageManager pm = BaseApplication.getApplication().getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(appName, 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 获取系统的版本信息
     *
     * @return
     */
    public static String getSystemVersionName() {
        return URLCoderUtils.URLEncoder(Build.VERSION.RELEASE);
    }

    /**
     * 获取系统的cpu
     *
     * @return
     */
    public static String getSystemCpuInfo() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密 将传进来的图片的url地址加密
     *
     * @param s 传进的url
     * @return 加密后的值
     */
    public static String encodeMD5(String s) {
        byte[] input = s.getBytes();
        String output = null;
        // 声明16进制字母
        char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            // 获得一个MD5摘要算法的对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            /*
			 * MD5算法的结果是128位一个整数,在这里javaAPI已经把结果转换成字节数组了
			 */
            byte[] tmp = md.digest();// 获得MD5的摘要结果
            char[] str = new char[32];
            byte b = 0;
            for (int i = 0; i < 16; i++) {
                b = tmp[i];
                str[2 * i] = hexChar[b >>> 4 & 0xf];// 取每一个字节的低四位换成16进制字母
                str[2 * i + 1] = hexChar[b & 0xf];// 取每一个字节的高四位换成16进制字母
            }
            output = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return output;

    }

}
