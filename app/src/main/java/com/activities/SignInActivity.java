package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trash.R;
import com.net.SignIn;
import com.ui.initActionBar;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        initLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//控制选项建
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void initLayout(){
        setContentView(R.layout.signinactivity);
        int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        getWindow().setStatusBarColor(themeColor);
        new initActionBar(getSupportActionBar(),"登录",R.drawable.ic_arrow_back_black_24dp,themeColor);

        TextView textView = findViewById(R.id.GoToSignUp_SignIn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

        Button button = findViewById(R.id.SignIn_SignIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignIn(SignInActivity.this,SignInActivity.this).sendSignInInfo(
                        (TextView) findViewById(R.id.UserName_SignIn),
                        (TextView) findViewById(R.id.Password_SignIn),
                        (CheckBox) findViewById(R.id.cb_1),
                        (CheckBox) findViewById(R.id.cb_2)
                );
            }
        });
    }
}

