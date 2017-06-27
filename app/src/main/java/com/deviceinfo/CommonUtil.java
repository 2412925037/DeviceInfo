package com.deviceinfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shuxiong on 2017/6/22.
 */

public class CommonUtil {


    public  static String INIT_URL = "http://gpup.lettersharing.com/grab/add";
    //post请求
    public static String sendPost(String http, String data) {
        Log.e("requestData", "url:" + http + "\n data:" + data);
        StringBuffer rval = new StringBuffer();
        try {
            URL url = new URL(http);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
//		    conn.setRequestProperty("Content-Encoding", "gzip");
            conn.connect();
            // 传送数据
            if (data != null && !"".equals(data)) {
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter out = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(out);
                bw.write(data);
                bw.flush();
                bw.close();
                out.close();
                os.close();
            }

            // 接收数据
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    rval.append(line).append(System.getProperty("line.separator")); // 添加换行符，屏蔽了 Windows和Linux的区别 ,更保险一些
                }
                br.close();
                isr.close();
                is.close();
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dddd","网络请求失败");
        }
        String result = rval.toString().trim();
        Log.e("responseData", result);
        return result;
    }
}
