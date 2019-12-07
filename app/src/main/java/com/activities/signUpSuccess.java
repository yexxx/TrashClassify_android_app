package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trash.R;
import com.ui.initActionBar;

public class signUpSuccess extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//控制选项建
        if (item.getItemId() == android.R.id.home) {
            finish();
            startActivity(new Intent(signUpSuccess.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout(){
        setContentView(R.layout.success_sign_up);
        int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        getWindow().setStatusBarColor(themeColor);
        new initActionBar(getSupportActionBar(),"注册成功",R.drawable.ic_arrow_back_black_24dp,themeColor);

        TextView textView = findViewById(R.id.success_sign_up);
        textView.setText(String.valueOf(getIntent().getIntExtra("id",0)));
    }
}
