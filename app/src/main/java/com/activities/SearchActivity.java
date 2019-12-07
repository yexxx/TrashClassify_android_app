package com.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adapters.adapterSearch;
import com.example.trash.R;
import com.ui.Feedback;
import com.ui.initActionBar;
import com.utils.GetJsonDataUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    ArrayList<String> strings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);

        initLayout();
    }

    //set the home key's function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void initLayout(){
        int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        //init for contextView  & statusBar & actionBar
        setContentView(R.layout.search_activity);//contextView
        getWindow().setStatusBarColor(themeColor);//statusBar
        new initActionBar(getSupportActionBar(),"搜索",R.drawable.ic_arrow_back_black_24dp,themeColor);//actionBar

        //init data for listView
        //strings = new getJsonData().getJson(SearchActivity.this,"Garbage.json");
        String string = new GetJsonDataUtil().getJson(SearchActivity.this, "Garbage.json");
        String[] strings1 = string.split("\\{");
        for (String value : strings1) {
            if (value.matches("[\\S\\s]*category[\\S\\s]*"))
                strings.add(value);
        }

        //init for listView
        final ListView listView = findViewById(R.id.list_view_search);
        listView.setAdapter(new adapterSearch(SearchActivity.this,R.layout.list_search,strings));

        //init for searchView
        SearchView searchView = findViewById(R.id.search_view);
        try{
            searchView.onActionViewExpanded();
            Toast.makeText(this,"请点击输入法上的搜索按键进行搜索",Toast.LENGTH_LONG).show();
            searchView.setQuery(Objects.requireNonNull(getIntent().getExtras()).getString("s",""),true);
        }catch (Exception e){
            e.printStackTrace();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if(query.matches("[\\u4e00-\\u9fa5]*")) {
                    SharedPreferences.Editor editor = getSharedPreferences("history", MODE_PRIVATE).edit();
                    editor.putString("search_text", getSharedPreferences("history", MODE_PRIVATE).getString("search_text", "") + "&" +
                            query + "*" + new SimpleDateFormat("YYYY-MM-DD hh:mm:ss", Locale.getDefault()).format(new Date()));
                    editor.apply();
                    new Feedback(strings).matches(SearchActivity.this, query,
                            (ListView) findViewById(R.id.list_view_search),
                            (TextView) findViewById(R.id.text_search1),
                            (TextView) findViewById(R.id.text_search2));
                    TextView textView = findViewById(R.id.text_search2);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Feedback().showAlertDialog(query, SearchActivity.this, "add");
                        }
                    });
                }else Toast.makeText(SearchActivity.this,"暂时只支持汉字的搜索,请输入汉字",Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if(newText.matches("[\\u4e00-\\u9fa5]*")) {
                    new Feedback(strings).matches(SearchActivity.this, newText,
                            (ListView) findViewById(R.id.list_view_search),
                            (TextView) findViewById(R.id.text_search1),
                            (TextView) findViewById(R.id.text_search2));
                    TextView textView = findViewById(R.id.text_search2);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Feedback().showAlertDialog(newText, SearchActivity.this, "add");
                        }
                    });
                }else Toast.makeText(SearchActivity.this,"暂时只支持汉字的搜索,请输入汉字",Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
}
