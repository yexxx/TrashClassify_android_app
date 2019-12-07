package com.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.adapters.adapterMain;
import com.example.trash.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.net.BaiduAI;
import com.utils.GetJsonDataUtil;
import com.ui.PictureCapture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private long firstTime = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_PHOTO = 2;
    static final int REQUEST_IMAGE_CUTTING = 3;
    static final int REQUEST_IMAGE_CUTTING_ = 4;
    private ArrayList<String> kitchen = new ArrayList<>();
    private ArrayList<String> recycle = new ArrayList<>();
    private ArrayList<String> harmful = new ArrayList<>();
    private ArrayList<String> others = new ArrayList<>();
    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    System.out.println(photoFile);
                    startActivityForResult(new PictureCapture(getExternalFilesDir(Environment.DIRECTORY_PICTURES),this,this)
                            .imageZoom(FileProvider.getUriForFile(this,"com.example.trash.fileprovider",photoFile)),REQUEST_IMAGE_CUTTING);
                    break;

                case REQUEST_SELECT_PHOTO:
                    Toast.makeText(MainActivity.this, "success1", Toast.LENGTH_SHORT).show();
                    startActivityForResult(new PictureCapture(getExternalFilesDir(Environment.DIRECTORY_PICTURES),this,this)
                            .imageZoom(new PictureCapture(getExternalFilesDir(Environment.DIRECTORY_PICTURES),this,this).getImageUri(data)),REQUEST_IMAGE_CUTTING_);
                    break;

                case REQUEST_IMAGE_CUTTING:
                    //上传11提供识别
                    Toast.makeText(MainActivity.this, "success2", Toast.LENGTH_SHORT).show();
                    new BaiduAI(this).advancedGeneral(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "11.jpg",photoFile);
                    break;

                case REQUEST_IMAGE_CUTTING_:
                    //上传11提供识别
                    Toast.makeText(MainActivity.this, "success2", Toast.LENGTH_SHORT).show();
                    new BaiduAI(this).advancedGeneral(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "11.jpg",null);
                    break;
            }
        }
    }

    @Override         //右上角显示menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override          //double click to back
    public void onBackPressed() {                     //两次返回退出
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //连按两次退出
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "请再次返回以退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//控制选项建
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.take_photo:
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},300);
                }else {
                    try {
                        photoFile = new PictureCapture(getExternalFilesDir(Environment.DIRECTORY_PICTURES), this, this).createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    startActivityForResult(new PictureCapture(getExternalFilesDir(Environment.DIRECTORY_PICTURES), this, this)
                            .dispatchTakePictureIntent(photoFile), REQUEST_IMAGE_CAPTURE);
                }
                break;

            case R.id.select_photo:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent,REQUEST_SELECT_PHOTO);
                } else {
                    Toast.makeText(this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.search:
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout(){
        setContentView(R.layout.mainactivity);
        //状态栏和工具栏
        int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        getWindow().setStatusBarColor(themeColor);
        RelativeLayout relativeLayout = findViewById(R.id.relative_layout_main);
        try {
            relativeLayout.setBackground(new ColorDrawable(themeColor));
        }catch (Exception e){
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolBar_mainActivity);
        toolbar.setTitle("主页");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {                                         //生成选项键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setBackgroundDrawable(new ColorDrawable(themeColor));
        }

        //抽屉定义及初始化
        drawerLayout = findViewById(R.id.drawerLayout_mainActivity);
        NavigationView navigationView = findViewById(R.id.navigationView_mainActivity);
        View headView = navigationView.getHeaderView(0);
        CircleImageView circleImageView = headView.findViewById(R.id.icon_image);
        TextView textUserName = headView.findViewById(R.id.username);
        TextView textEmail = headView.findViewById(R.id.user_email);

        //抽屉按键设置
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history:
                        Intent history = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(history);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about:
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                .setPositiveButton("确认",null)
                                .setMessage("软件工程第五组\n\n\n叶鑫、刘彪、任家鑫")
                                .create();
                        alertDialog.show();
                        break;
                }
                return false;
            }
        });

        //个人资料及登出界面
        final SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isLogin",false)){
            textUserName.setText(String.valueOf(sharedPreferences.getInt("id",10000)));
            textEmail.setText(sharedPreferences.getString("email","ClickAvatar.To.Login"));
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("是否登出？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isLogin",false);
                                    editor.apply();
                                    recreate();
                                    Toast.makeText(MainActivity.this,"登出成功",Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            });
        }else{
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
            });
        }

        //垃圾表初始化
        String string = new GetJsonDataUtil().getJson(MainActivity.this, "Garbage.json");
        String[] Reg = string.split("\\{");
        for (String s : Reg) {
            if (s.matches("[\\s\\S]*category[\\s\\S]*")) {
                String[] Reg1 = s.split(",");
                String trashName = Reg1[2].split(":")[1].split("\"")[1];
                if ((Reg1[0].split(":")[1]).matches("[\\s\\S]*4[\\s\\S]*"))
                    kitchen.add(trashName);
                else if ((Reg1[0].split(":")[1]).matches("[\\s\\S]*1[\\s\\S]*"))
                    recycle.add(trashName);
                else if ((Reg1[0].split(":")[1]).matches("[\\s\\S]*2[\\s\\S]*"))
                    harmful.add(trashName);
                else
                    others.add(trashName);
            }
        }
        //初始界面
        final ListView listView = findViewById(R.id.list_view_main);
        listView.setAdapter(new adapterMain(MainActivity.this,R.layout.list_main,kitchen));
        listView.setTextFilterEnabled(true);
        //点击改变垃圾种类
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemBackground(new ColorDrawable(themeColor));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                ListView listView = findViewById(R.id.list_view_main);
                switch (menuItem.getItemId()){
                    case R.id.kitchen:
                        listView.setAdapter(new adapterMain(MainActivity.this,R.layout.list_main,kitchen));
                        break;
                    case R.id.recycle:
                        listView.setAdapter(new adapterMain(MainActivity.this,R.layout.list_main,recycle));
                        break;
                    case R.id.harmful:
                        listView.setAdapter(new adapterMain(MainActivity.this,R.layout.list_main,harmful));
                        break;
                    case R.id.others:
                        listView.setAdapter(new adapterMain(MainActivity.this,R.layout.list_main,others));
                        break;
                }
                return true;
            }
        });
    }
}
