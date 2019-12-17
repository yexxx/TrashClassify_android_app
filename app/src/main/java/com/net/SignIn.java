package com.net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.activities.MainActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignIn {
    private Context context;
    private Activity activity;
    public SignIn(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }

    public void sendSignInInfo(TextView userName, TextView password, CheckBox checkBox1, CheckBox checkBox2){
        final String baseUrl = "http://your_ip_here/signin/";
        final String post = signInInputCheck(userName, password, checkBox1, checkBox2);
        if(!post.equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(baseUrl);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setDoOutput(true);
                        urlConn.setDoInput(true);
                        urlConn.setRequestMethod("POST");
                        urlConn.connect();

                        DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                        dos.writeBytes(post);
                        dos.flush();
                        dos.close();

                        InputStream in = urlConn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) sb.append(line);
                        System.out.println(sb.toString());
                        if (sb.toString().matches("success[\\s\\S]*")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if(post.matches("id[\\s\\S]*")){
                                String[] Reg1 = post.split("&");
                                String[] Reg2 = Reg1[0].split("=");
                                editor.putInt("id",Integer.parseInt(Reg2[1]));
                                String[] Reg3 = sb.toString().split("\\+");
                                editor.putString("email",Reg3[1]);
                            }else{
                                String[] Reg1 = post.split("&");
                                String[] Reg2 = Reg1[0].split("=");
                                editor.putString("email",Reg2[1]);
                                String[] Reg3 = sb.toString().split("\\+");
                                editor.putString("id",Reg3[1]);
                            }
                            editor.apply();
                            Message message = Message.obtain();
                            message.what = 0;
                            signInHandler.sendMessage(message);
                        } else if (sb.toString().equals("password error")) {
                            Message message = Message.obtain();
                            message.what = 1;
                            signInHandler.sendMessage(message);
                        } else if (sb.toString().equals("no such user")) {
                            Message message = Message.obtain();
                            message.what = 2;
                            signInHandler.sendMessage(message);
                        } else {
                            Message message = Message.obtain();
                            message.what = 3;
                            signInHandler.sendMessage(message);
                        }
                    } catch (IOException IOe) {
                        IOe.printStackTrace();
                        Message message = Message.obtain();
                        message.what = 4;
                        signInHandler.sendMessage(message);
                    }
                }
            }).start();
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler signInHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin",true);
                    editor.apply();
                    activity.finish();
                    context.startActivity(new Intent(context, MainActivity.class));
                    break;
                case 1:
                    Toast.makeText(context,"密码错误",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context,"用户不存在",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(context,"系统异常",Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(context,"连接超时",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private String signInInputCheck(TextView userName, TextView password, CheckBox checkBox1, CheckBox checkBox2){
        String post = "";
        if(checkBox1.isChecked()&&checkBox2.isChecked()||
                !(checkBox1.isChecked()||checkBox2.isChecked())) {
            Toast.makeText(context, "请选择账号登录或者邮箱登录,且只能选择一种登录方式", Toast.LENGTH_LONG).show();
        } else{
            if(checkBox1.isChecked()) {
                if(!userName.getText().toString().matches("\\d{5,11}")){
                    Toast.makeText(context, "账号格式错误：\n账号为五位以上且少于十一位的纯数字", Toast.LENGTH_LONG).show();
                } else if(!password.getText().toString().matches("^[a-zA-Z]\\w{5,17}$")){
                    Toast.makeText(context, "密码格式错误：\n密码应以字母开头，长度在6~18之间，只能包含字母、数字和下划线", Toast.LENGTH_LONG).show();
                } else {
                    post = "id=" + userName.getText() + "&" + "password=" + password.getText();
                }
            }
            else {
                if(!userName.getText().toString().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
                    Toast.makeText(context, "邮箱格式错误：\n邮箱格式应为xxxxx@xxx.xxx", Toast.LENGTH_LONG).show();
                } else if(!password.getText().toString().matches("^[a-zA-Z]\\w{5,17}$")){
                    Toast.makeText(context, "密码格式错误：\n密码应以字母开头，长度在6~18之间，只能包含字母、数字和下划线", Toast.LENGTH_LONG).show();
                } else {
                    post = "email=" + userName.getText() + "&" + "password=" + password.getText();
                }
            }
        }
        return post;
    }
}
