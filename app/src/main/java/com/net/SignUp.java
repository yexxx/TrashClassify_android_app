package com.net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.activities.signUpSuccess;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp {
    private Context context;
    private Activity activity;
    public SignUp(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }

    public void sendSignUpInfo(EditText email, EditText password1, EditText password2){
        final String baseUrl = "http://149.129.106.120:8000/signup/";
        if(!email.getText().toString().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
            Toast.makeText(context, "邮箱格式错误：\n邮箱格式应为xxxxx@xxx.xxx", Toast.LENGTH_LONG).show();
        }
        else if(password1.getText().toString().equals(password2.getText().toString())){
            if(!password1.getText().toString().matches("^[a-zA-Z]\\w{5,17}$")){
                Toast.makeText(context, "密码格式错误：\n密码应以字母开头，长度在6~18之间，只能包含字母、数字和下划线", Toast.LENGTH_LONG).show();
            }else {
                final String post = "email=" + email.getText() + "&" + "password=" + password1.getText();
                final String emailS = email.getText().toString();
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
                            if (sb.toString().matches("success\\+\\d+")) {
                                SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String[] Reg = sb.toString().split("\\+");
                                editor.putInt("id",Integer.parseInt(Reg[1]));
                                editor.putString("email",emailS);
                                editor.apply();
                                Message message = Message.obtain();
                                message.what = 0;
                                message.arg1=Integer.parseInt(Reg[1]);
                                signUpHandler.sendMessage(message);
                            } else if (sb.toString().equals("repeat email")) {
                                Message message = Message.obtain();
                                message.what = 1;
                                signUpHandler.sendMessage(message);
                            } else {
                                Message message = Message.obtain();
                                message.what = 2;
                                signUpHandler.sendMessage(message);
                            }
                        } catch (IOException IOe) {
                            IOe.printStackTrace();
                            Message message = Message.obtain();
                            message.what = 3;
                            signUpHandler.sendMessage(message);
                        }
                    }
                }).start();
            }
        }else {
            Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler signUpHandler = new Handler() {
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
                    context.startActivity(new Intent(context, signUpSuccess.class).putExtra("id",msg.arg1));
                    break;
                case 1:
                    Toast.makeText(context,"该邮箱已经注册过了，请联系管理员找回密码或换个邮箱注册",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context,"系统错误",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(context,"连接超时",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
