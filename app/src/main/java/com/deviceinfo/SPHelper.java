package com.deviceinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lidawei on 2017/3/31.
 */

public class SPHelper {

    public static boolean needGetDeviceInfo(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("DEVICEINFO", true);
    }

    public static void setGetDeviceInfoStatus(Context context, boolean status) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("DEVICEINFO", status).commit();
    }
}
