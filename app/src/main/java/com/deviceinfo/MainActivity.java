package com.deviceinfo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File("sdcard/aa.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String str = init();
            FileOutputStream out = new FileOutputStream(file);
            out.write(str.getBytes());
            out.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = init();
                CommonUtil.sendPost(CommonUtil.INIT_URL,str);
            }
        }).start();

    }

    /**
     public static final String BOOTLOADER = "BOOTLOADER";
     public static final String DISPLAY = "DISPLAY";
     public static final String HOST = "HOST";
     public static final String MANUFACTURER = "MANUFACTURER";
     public static final String RADIO = "RADIO";
     public static final String SUPPORTED_32_BIT_ABIS = "SUPPORTED_32_BIT_ABIS";
     public static final String SUPPORTED_64_BIT_ABIS = "SUPPORTED_64_BIT_ABIS";
     public static final String SUPPORTED_ABIS = "SUPPORTED_ABIS";
     public static final String TAGS = "TAGS";
     public static final String TIME = "TIME";
     public static final String UNKNOWN = "UNKNOWN";
     public static final String CODENAME = "CODENAME";
     public static final String INCREMENTAL = "INCREMENTAL";
     public static final String SDK_INT = "SDK_INT";

     public static class VERSION {
     public static final String CODENAME = null;
     public static final String INCREMENTAL = null;
     public static final String RELEASE = null;
    public static final String SDK = null;
    public static final int SDK_INT = 0;

    */

    public String init(){
        String data = "";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(FieldName.BOOTLOADER,Build.BOOTLOADER);
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
            jsonObject.put(FieldName.displayMetrics,Devices.getDisplays(this));
            jsonObject.put(FieldName.linuxVer,Devices.getLinuxCore_Ver());
            jsonObject.put(FieldName.basebandVer,Devices.getBaseband_Ver());
            jsonObject.put(FieldName.innerVer,Devices.getInner_Ver());

            jsonObject.put("appList",getApp());

            Log.e("dddd",jsonObject.toString());
            data = jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public JSONObject getApp(){

        JSONObject object = new JSONObject();
        try {
            //系统应用
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobject = null;

            List<AppInfo> data = Devices.appInfo_system(this);
            for (AppInfo info : data) {
                jsonobject = new JSONObject();
                jsonobject.put(FieldName.pkgName, info.packageName);
                jsonobject.put(FieldName.versionCode,info.versionCode);
                jsonobject.put(FieldName.versionName,info.versionName);
                //jsonobject.put(FieldName.signature,Devices.getSingInfo(this,info.packageName));
                jsonarray.put(jsonobject);
            }
            //用户级应用
            JSONArray jsonarray1 = new JSONArray();
            JSONObject jsonobject1 = null;

            List<AppInfo> data1 = Devices.appInfo_user(this);
            for (AppInfo info : data1) {
                jsonobject1 = new JSONObject();
                jsonobject1.put(FieldName.pkgName, info.packageName);
                jsonobject1.put(FieldName.versionCode,info.versionCode);
                jsonobject1.put(FieldName.versionName,info.versionName);
                //jsonobject1.put(FieldName.signature,Devices.getSingInfo(this,info.packageName));
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
