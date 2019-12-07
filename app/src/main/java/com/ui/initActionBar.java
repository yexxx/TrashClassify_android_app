package com.ui;

import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.ActionBar;

public class initActionBar {
    public initActionBar(ActionBar actionBar, String title, int iconId, int themeColor){
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(themeColor));
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(iconId);
        }
    }
}
