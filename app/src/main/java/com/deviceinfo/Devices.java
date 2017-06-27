package com.deviceinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by shuxiong on 2017/6/22.
 *
 * Build类相关
 * 已安装包名，版本号，版本名， 签名（包括系统应用和安装应用）

   Display 屏幕分辨率，高度等信息

   内核版本

 */

public class Devices {



    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */

    public static String getBaseband_Ver(){
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] { String.class,String.class });
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
// System.out.println(">>>>>>><<<<<<<" +(String)result);
            Version = (String)result;
        } catch (Exception e) {
        }
        return Version;
    }
    /**
     * CORE-VER
     * 内核版本
     * return String
     */

    public static String getLinuxCore_Ver() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


// get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);


        String result = "";
        String line;
// get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * INNER-VER
     * 内部版本
     * return String
     */

    public static String getInner_Ver(){
        String ver = "" ;

        if(android.os.Build.DISPLAY .contains(android.os.Build.VERSION.INCREMENTAL)){
            ver = android.os.Build.DISPLAY;
        }else{
            ver = android.os.Build.VERSION.INCREMENTAL;
        }
        return ver;

    }

    /**
     * 获取系统应用
     * @param context
     * @return
     */
    public static List<AppInfo> appInfo_system(Context context){
        List<AppInfo> systemAppList = ApplicationInfoUtil.getAllSystemProgramInfo(context);
        return systemAppList;
    }

    /**
     * 获取用户级应用
     * @param context
     * @return
     */
    public static List<AppInfo> appInfo_user(Context context){
        List<AppInfo> userAppList = ApplicationInfoUtil.getAllNonsystemProgramInfo(context);
        return userAppList;
    }

    /**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static String getDisplays(Context context){
        // 通过Resources获取

        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        //System.out.println("heigth2 : " + dm2.heightPixels);

        //System.out.println("width2 : " + dm2.widthPixels);
        return dm2.widthPixels + "*" + dm2.heightPixels;
    }

    /**
     * 获取应用签名
     * @param context
     * @param pkgname
     * @return
     */
    public static String getSignature(Context context,String pkgname) {
         PackageManager manager = context.getPackageManager();
         PackageInfo packageInfo;
         Signature[] signatures;
         StringBuilder builder = new StringBuilder();
         String str = "";
         boolean isEmpty = TextUtils.isEmpty(pkgname);
        if (isEmpty) {
            Toast.makeText(context, "应用程序的包名不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            try {
                /** 通过包管理器获得指定包名包含签名的包信息 **/
                packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
                /******* 通过返回的包信息获得签名数组 *******/
                signatures = packageInfo.signatures;
                /******* 循环遍历签名数组拼接应用签名 *******/
                for (Signature signature : signatures) {
                    builder.append(signature.toCharsString());
                }
                /************** 得到应用签名 **************/
                str = builder.toString();

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static String getSingInfo(Context act,String pkgname) {
        try {
            PackageInfo packageInfo = act.getPackageManager().getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            byte[] si = sign.toByteArray();
            String base = Base64.encodeToString(si, Base64.DEFAULT);
            //String base = parseSignature(si);
            return base;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            // String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            // System.out.println("pubKey:" + pubKey);
            System.out.println("signNumber:" + signNumber);
            return signNumber;
        } catch (Exception e)

        {
            e.printStackTrace();
        }

        return "";
    }

}
