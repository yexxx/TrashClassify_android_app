package com.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.activities.SignInActivity;
import com.adapters.adapterSearch;
import com.example.trash.R;
import com.net.UpdateData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Feedback {
    private String alertName = "警告：";
    private String alertContext = "条款1\n\n" +
            "条款2\n\n" +
            "条款3\n\n" +
            "条款4\n\n" +
            "条款5\n\n" +
            "......\n\n" +
            "条款n";
    private String nullString = "";

    final private String[] trashTypes = new String[]{"厨余垃圾","可回收物","有害垃圾","其他垃圾"};
    final private boolean[] trashTypesSelected = new boolean[]{false,false,false,false};
    private ArrayList<String> strings;



    public Feedback(ArrayList<String> strings){
        this.strings = strings;
    }
    public Feedback(){}

    public void matches(Context context, final String str, ListView listView, TextView textView1, TextView textView2){
        ArrayList<String> strings1 = new ArrayList<>();
        for(int i=0; i<strings.size(); i++)
            if(strings.get(i).matches("[\\s\\S]*"+str+"[\\s\\S]*"))
                strings1.add(strings.get(i));
        if(!strings1.isEmpty()) {
            listView.setAdapter(new adapterSearch(context,R.layout.list_search,strings1));
            textView1.setText(nullString);
            textView2.setText(nullString);
        }
        else {
            listView.setAdapter(new adapterSearch(context,R.layout.list_search,strings1));
            textView1.setText("未查询到相关结果,欢迎向我们提交");
            textView2.setText("反馈");
        }
    }

    public void showAlertDialog(final String str, final Context context, final String method){
        final SharedPreferences SPHistory = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(alertName)
                .setMessage(alertContext)
                .setNeutralButton("取消",null)
                .setPositiveButton("我明白了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String string = SPHistory.getString("edit_history", "")
                                + str + "+"
                                + (trashTypesSelected[0]?"0":"")
                                + (trashTypesSelected[1]?"1":"")
                                + (trashTypesSelected[2]?"2":"")
                                + (trashTypesSelected[3]?"3":"")
                                + "+" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date())
                                + ",";
                        SPHistory.edit()
                                .putString("edit_history",string)
                                .apply();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new UpdateData("method=" + method
                                            + "&userid=" + context.getSharedPreferences("account", Context.MODE_PRIVATE).getInt("id", 10000)
                                            + "&name=" + str
                                            + "&mtype="
                                            + (trashTypesSelected[0]?"4":"")
                                            + (trashTypesSelected[1]?"1":"")
                                            + (trashTypesSelected[2]?"2":"")
                                            + (trashTypesSelected[3]?"8":""));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Toast.makeText(context,"提交反馈成功，等待管理员审核",Toast.LENGTH_LONG).show();
                    }
                })
                .create();

        if(context.getSharedPreferences("account",Context.MODE_PRIVATE).getBoolean("isLogin",false)) {
            new AlertDialog.Builder(context)
                    .setTitle("请选择垃圾" + str + "属于：")
                    .setMultiChoiceItems(trashTypes, trashTypesSelected, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            trashTypesSelected[which] = isChecked;
                        }
                    })
                    .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (trashTypesSelected[0] && (!(trashTypesSelected[1] || trashTypesSelected[2] || trashTypesSelected[3]))) {
                                alertDialog.show();
                            } else if (trashTypesSelected[1] && (!(trashTypesSelected[0] || trashTypesSelected[2] || trashTypesSelected[3]))) {
                                alertDialog.show();
                            } else if (trashTypesSelected[2] && (!(trashTypesSelected[1] || trashTypesSelected[0] || trashTypesSelected[3]))) {
                                alertDialog.show();
                            } else if (trashTypesSelected[3] && (!(trashTypesSelected[1] || trashTypesSelected[2] || trashTypesSelected[0]))) {
                                alertDialog.show();
                            } else
                                Toast.makeText(context, "请选择且仅选择一个选项", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        else {
            context.startActivity(new Intent(context, SignInActivity.class));
            Toast.makeText(context,"您必须登录后才能执行更改操作",Toast.LENGTH_LONG).show();
        }
    }
}