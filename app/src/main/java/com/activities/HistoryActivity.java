package com.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adapters.adapterHistory;
import com.example.trash.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ui.initActionBar;
import com.utils.HistoryItem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    private List<HistoryItem> historyItems_photo = new ArrayList<>();
    private List<HistoryItem> historyItems_edit = new ArrayList<>();
    private List<HistoryItem> historyItems_search = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();
    }

    //function of home key
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private String getClass(String s){
        switch (s){
            case "0":
                return "厨余垃圾";
            case "1":
                return "可回收物";
            case "2":
                return "有害垃圾";
            case "3":
                return "其它垃圾";
            default:
                    return "";
        }
    }

    private void initLayout(){
        //init for contextView  & statusBar & actionBar
        setContentView(R.layout.history);
        int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        getWindow().setStatusBarColor(themeColor);
        new initActionBar(getSupportActionBar(),"历史记录",R.drawable.ic_arrow_back_black_24dp,themeColor);

        //init data for listView
        //edit
        SharedPreferences sharedPreferences = getSharedPreferences("history",MODE_PRIVATE);
        String edit_history = sharedPreferences.getString("edit_history","");
        String[] edit_histories = edit_history.split(",");
        for(String s:edit_histories){
            try{
            historyItems_edit.add(
                    new HistoryItem(s.split("\\+")[0]+"->"+ getClass(s.split("\\+")[1]),
                            "\n"+s.split("\\+")[2],
                            getPic(s.split("\\+")[1])));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.reverse(historyItems_edit);
        //photo
        String search_photo_path = sharedPreferences.getString("search_photo_path","");
        System.out.println(search_photo_path);
        File[] fileNames = new File(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES)).getPath()+"/").listFiles();
        try{
            for(int i = 0; i < Objects.requireNonNull(fileNames).length; i++){
                FileInputStream stream = new FileInputStream(fileNames[i]);
                if(stream.getChannel().size() != 0 && !fileNames[i].getName().matches("[\\s\\S]*11.jpg[\\s\\S]*")) {
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    String filePath = fileNames[i].getName();
                    int beginIndex = search_photo_path.indexOf(filePath) + filePath.length();
                    historyItems_photo.add(
                            new HistoryItem(search_photo_path.substring(beginIndex,beginIndex + getCharacters(search_photo_path,beginIndex)),
                            fileNames[i].getName()+"\n"+ search_photo_path.substring(beginIndex -99, beginIndex-80),
                            bitmap));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //search_text
        String search_text = getSharedPreferences("history", MODE_PRIVATE).getString("search_text","");
        System.out.println(search_text);
        try {
            String[] reg = search_text.split("&");
            for(String s:reg){
                if(!s.equals("")) {
                    historyItems_search.add(new HistoryItem(
                            s.split("\\*")[0],
                            s.split("\\*")[1],
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_text_format_black_48dp)));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Collections.reverse(historyItems_search);

        //init bottomNavigationView and its listView
        final ListView listView = findViewById(R.id.list_view_history);
        listView.setAdapter(new adapterHistory(HistoryActivity.this,R.layout.history_items, historyItems_edit));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_history);
        bottomNavigationView.setItemBackground(new ColorDrawable(themeColor));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history_edit:
                        listView.setAdapter(new adapterHistory(HistoryActivity.this,R.layout.history_items, historyItems_edit));
                        break;
                    case R.id.history_photo:
                        listView.setAdapter(new adapterHistory(HistoryActivity.this,R.layout.history_items, historyItems_photo));
                        break;
                    case R.id.history_search:
                        listView.setAdapter(new adapterHistory(HistoryActivity.this,R.layout.history_items, historyItems_search));
                        break;
                }
                return true;
            }
        });
    }

    private int getCharacters(String search_photo_path, int beginIndex){
        int i = 1;
        try{
        while (search_photo_path.substring(beginIndex,beginIndex+i).matches("[\\u4e00-\\u9fa5]*")) {
            i++;
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return i-1;
    }


    private Bitmap getPic(String s){
        switch (s){
            case "0":
                return BitmapFactory.decodeResource(getResources(), R.drawable.vegetables_156);
            case "1":
                return BitmapFactory.decodeResource(getResources(), R.drawable.recycle_121);
            case "2":
                return BitmapFactory.decodeResource(getResources(), R.drawable.garbage_94);
            case "3":
                return BitmapFactory.decodeResource(getResources(), R.drawable.others);
            default:return null;
        }
    }
}

