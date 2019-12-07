package com.utils;

import android.graphics.Bitmap;

//struct for history view
 public class HistoryItem {
        private String picName;
        private String picText;
        private Bitmap pic;
        public HistoryItem(String SName, String SText, Bitmap bitmap){
            picName = SName;
            picText = SText;
            pic = bitmap;
        }
        public String getPicName(){
            return picName;
        }
        public String getPicText(){
            return picText;
        }
        public Bitmap getPic(){
            return pic;
        }
    }
