package com.umiwi.video.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

public class NetworkManager extends BroadcastReceiver {


    /**
     * TelephonyManager.getNetworkClass()
     */
    public enum NetWorkCodes {
        NETWORK_CLASS_NO, NETWORK_CLASS_UNKNOWN, NETWORK_CLASS_2_G, NETWORK_CLASS_3_G, NETWORK_CLASS_4_G, NETWORK_CLASS_WIFI
    }


    private NetworkManager(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static ArrayList<NetworkStatusListener> statusList = new ArrayList<>();

//    public void registerNetworkListener(Context context) {
//        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        context.registerReceiver(ls, intentFilter);
//    }

    public interface NetworkStatusListener {

        void networkStatus(NetWorkCodes code);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (NetworkStatusListener s : statusList) {
            s.networkStatus(getNetWorkType(context));
        }
    }

    private NetWorkCodes getNetWorkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null && info.isConnectedOrConnecting()) {
            return NetWorkCodes.NETWORK_CLASS_NO;
        }
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI: {
                return NetWorkCodes.NETWORK_CLASS_WIFI;
            }
            case ConnectivityManager.TYPE_MOBILE: {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
//                    case 16: //TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetWorkCodes.NETWORK_CLASS_2_G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
//                    case 17: //TelephonyManager.NETWORK_TYPE_TD_SCDMA://17
                        return NetWorkCodes.NETWORK_CLASS_3_G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
//                    case 18: //TelephonyManager.NETWORK_TYPE_IWLAN:
                        return NetWorkCodes.NETWORK_CLASS_4_G;
                }
                break;
            }
            default: {
                return NetWorkCodes.NETWORK_CLASS_UNKNOWN;
            }
        }
        return NetWorkCodes.NETWORK_CLASS_UNKNOWN;
    }
}
