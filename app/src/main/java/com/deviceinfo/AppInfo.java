package com.deviceinfo;

import android.graphics.drawable.Drawable;

/**
 * Created by shuxiong on 2017/6/22.
 */

public class AppInfo {

    public String appName ; // 应用名
    public String packageName ; // 包名
    public String versionName ; // 版本名
    public int versionCode = 0; // 版本号
    public Drawable appIcon = null; // 应用图标

    @Override
    public String toString() {
        return appName+" , "+packageName+" ,"+versionName+" ,"+versionCode;
    }
}
