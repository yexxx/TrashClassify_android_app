package com.net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.activities.MainActivity;
import com.activities.SearchActivity;
import com.activities.signUpSuccess;
import com.utils.Base64Util;
import com.utils.FileUtil;
import com.utils.HttpUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaiduAI {
    private Context context;
    public BaiduAI(Context context){
        this.context = context;
    }

    public void advancedGeneral(final String filePath,final File picPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 请求url
                String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
                try {
                    // 本地文件路径
                    byte[] imgData = FileUtil.readFileByBytes(filePath);
                    String imgStr = Base64Util.encode(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");

                    String param = "image=" + imgParam;

                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = "24.263f05c71100d1ef02365e9c86bcc233.2592000.1576939477.282335-17275731";

                    String result = HttpUtil.post(url, accessToken, param);
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = result+"^"+ picPath;
                    advancedGeneralHandel.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = 1;
                    advancedGeneralHandel.sendMessage(message);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler advancedGeneralHandel = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    StringBuilder s = new StringBuilder();
                    String[] reg = msg.obj.toString().split("\\{");
                    for(int i = 2; i < reg.length; i++){
                        s.append(reg[i].split(",")[2]);
                    }
                    String[] reg1 = s.toString().split("\"");
                    s = new StringBuilder();
                    for (String value : reg1) {
                        if (value.matches("[\\u4e00-\\u9fa5]*")) {
                            s.append(value);
                            break;
                        }
                    }
                    SharedPreferences sharedPreferences = context.getSharedPreferences("history",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("search_photo_path",sharedPreferences.getString("search_photo_path","")
                            + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date())
                            + msg.obj.toString().split("\\^")[1]+s.toString());
                    editor.apply();
                    System.out.println(sharedPreferences.getString("search_photo_path",""));
                    Intent intent = new Intent(context, SearchActivity.class);
                    intent.putExtra("s",s.toString());
                    context.startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(context,"!!!!!!!!!!!!!!error",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
