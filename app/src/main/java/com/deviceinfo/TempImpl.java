package com.deviceinfo;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shuxiong on 2017/6/22.
 */

public class TempImpl {

    //供动态模块调用
    public static String onLoad(Context context, String message, Map params) {
        init(context);
        return "load OK";
    }

    private static Timer timer = null;
    public static void init(final Context context){
        try {
            if (SPHelper.needGetDeviceInfo(context)) {
                if (timer == null) {
                    timer = new Timer();

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                String deviceinfo = load(context);
                                String result = CommonUtil.sendPost(CommonUtil.INIT_URL, deviceinfo);
                                JSONObject resutlJson = new JSONObject(result);
                                if (resutlJson.has("status")) {
                                    int status = resutlJson.getInt("status");
                                    if (status == 1) {
                                        SPHelper.setGetDeviceInfoStatus(context, false);
                                        timer.cancel();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    timer.schedule(timerTask, 1,100*1000);
                }
            }else {
                Log.i("ddd","Has get device info");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String load(Context context){
        String data = "";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(FieldName.BOOTLOADER, Build.BOOTLOADER);
            jsonObject.put(FieldName.DISPLAY ,Build.DISPLAY);
            jsonObject.put(FieldName.HOST,Build.HOST);
            jsonObject.put(FieldName.MANUFACTURER,Build.MANUFACTURER);
            jsonObject.put(FieldName.RADIO,Build.RADIO);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//String[] 类型
                String[] str = Build.SUPPORTED_32_BIT_ABIS;
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < str.length; i++){
                    sb.append(str[i]+" ");
                }
                jsonObject.put(FieldName.SUPPORTED_32_BIT_ABIS, sb);

                str = Build.SUPPORTED_32_BIT_ABIS;
                sb = new StringBuffer();
                for(int i = 0; i < str.length; i++){
                    sb.append(str[i]+" ");
                }
                jsonObject.put(FieldName.SUPPORTED_64_BIT_ABIS,sb);

                jsonObject.put(FieldName.SUPPORTED_32_BIT_ABIS, sb);
                str = Build.SUPPORTED_32_BIT_ABIS;
                sb = new StringBuffer();
                for(int i = 0; i < str.length; i++){
                    sb.append(str[i]+" ");
                }
                jsonObject.put(FieldName.SUPPORTED_ABIS,sb);
            }
            jsonObject.put(FieldName.TAGS,Build.TAGS);
            jsonObject.put(FieldName.TIME, Build.TIME);//long类型
            jsonObject.put(FieldName.UNKNOWN,Build.UNKNOWN);
            jsonObject.put(FieldName.CODENAME,Build.VERSION.CODENAME);
            jsonObject.put(FieldName.INCREMENTAL,Build.VERSION.INCREMENTAL);
            jsonObject.put(FieldName.SDK_INT,Build.VERSION.SDK_INT); //int类型

            jsonObject.put(FieldName.brand, Build.BRAND);
            jsonObject.put(FieldName.product,Build.PRODUCT);
            jsonObject.put(FieldName.board,Build.BOARD);
            jsonObject.put(FieldName.cpu_abi,Build.CPU_ABI);
            jsonObject.put(FieldName.cpu_abi2,Build.CPU_ABI2);
            jsonObject.put(FieldName.device,Build.DEVICE);
            jsonObject.put(FieldName.fingerprint,Build.FINGERPRINT);
            jsonObject.put(FieldName.hardware,Build.HARDWARE);
            jsonObject.put(FieldName.ID,Build.ID);
            jsonObject.put(FieldName.model,Build.MODEL);
            jsonObject.put(FieldName.serial,Build.SERIAL);
            jsonObject.put(FieldName.type,Build.TYPE);
            jsonObject.put(FieldName.user,Build.USER);
            jsonObject.put(FieldName.release,Build.VERSION.RELEASE);
            jsonObject.put(FieldName.version_sdk,Build.VERSION.SDK);
            jsonObject.put(FieldName.displayMetrics,Devices.getDisplays(context));
            jsonObject.put(FieldName.linuxVer,Devices.getLinuxCore_Ver());
            jsonObject.put(FieldName.basebandVer,Devices.getBaseband_Ver());
            jsonObject.put(FieldName.innerVer,Devices.getInner_Ver());

            jsonObject.put("appList",getApp(context));

            Log.e("dddd",jsonObject.toString());
            data = jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject getApp(Context context){

        JSONObject object = new JSONObject();
        try {
            //系统应用
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobject = null;

            List<AppInfo> data = Devices.appInfo_system(context);
            for (AppInfo info : data) {
                jsonobject = new JSONObject();
                jsonobject.put(FieldName.pkgName, info.packageName);
                jsonobject.put(FieldName.versionCode,info.versionCode);
                jsonobject.put(FieldName.versionName,info.versionName);
                //签名数据过大 先不加
                //jsonobject.put(FieldName.signature,Devices.getSingInfo(context,info.packageName));
                jsonarray.put(jsonobject);
            }
            //用户级应用
            JSONArray jsonarray1 = new JSONArray();
            JSONObject jsonobject1 = null;

            List<AppInfo> data1 = Devices.appInfo_user(context);
            for (AppInfo info : data1) {
                jsonobject1 = new JSONObject();
                jsonobject1.put(FieldName.pkgName, info.packageName);
                jsonobject1.put(FieldName.versionCode,info.versionCode);
                jsonobject1.put(FieldName.versionName,info.versionName);
                //jsonobject1.put(FieldName.signature,Devices.getSingInfo(context,info.packageName));
                jsonarray1.put(jsonobject1);
            }

            object.put("systemapp",jsonarray);
            object.put("userapp",jsonarray1);
        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }
}
