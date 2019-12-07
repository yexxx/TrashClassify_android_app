package com.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trash.R;
import com.net.SignUp;
import com.ui.initActionBar;

public class SignUpActivity extends AppCompatActivity {
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
        setContentView(R.layout.signupactivity);
        int themeColor = getSharedPreferences("themeColor",MODE_PRIVATE).getInt("themeColor",getResources().getColor(R.color.themeColor,null));
        getWindow().setStatusBarColor(themeColor);
        new initActionBar(getSupportActionBar(),"注册",R.drawable.ic_arrow_back_black_24dp,themeColor);
        Button button = findViewById(R.id.SignUp_SignUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignUp(SignUpActivity.this,SignUpActivity.this).sendSignUpInfo(
                        (EditText)findViewById(R.id.UserEmail_SignUp),
                        (EditText)findViewById(R.id.Password1_SignUp),
                        (EditText)findViewById(R.id.Password2_SignUp)
                );
            }
        });
    }
}
