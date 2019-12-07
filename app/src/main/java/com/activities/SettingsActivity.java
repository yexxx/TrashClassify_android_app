package com.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.trash.R;
import com.ui.initActionBar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        final int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        getWindow().setStatusBarColor(themeColor);//statusBar
        new initActionBar(getSupportActionBar(),"设置",R.drawable.ic_arrow_back_black_24dp,themeColor);//actionBar
        final TextView color1 = findViewById(R.id.color1);
        final TextView color2 = findViewById(R.id.color2);
        final TextView color3 = findViewById(R.id.color3);
        final TextView color4 = findViewById(R.id.color4);
        final TextView color5 = findViewById(R.id.color5);
        final TextView color6 = findViewById(R.id.color6);
        final TextView color7 = findViewById(R.id.color7);
        final EditText color8 = findViewById(R.id.color8);
        final SharedPreferences.Editor editor = getSharedPreferences("themeColor",MODE_PRIVATE).edit();
        setcolor(color1,editor);
        setcolor(color2,editor);
        setcolor(color3,editor);
        setcolor(color4,editor);
        setcolor(color5,editor);
        setcolor(color6,editor);
        setcolor(color7,editor);
    }

    //set the home key's function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setcolor(final TextView color, final SharedPreferences.Editor editor){
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = color.getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) drawable;
                editor.putInt("themeColor",colorDrawable.getColor());
                editor.apply();
                recreate();
                Toast.makeText(SettingsActivity.this,"部分界面可能要重启应用才能使修改生效",Toast.LENGTH_LONG).show();
            }
        });
    }
}