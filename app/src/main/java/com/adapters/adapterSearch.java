package com.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trash.R;

import java.util.List;
import java.util.Objects;

public class adapterSearch extends ArrayAdapter<String> {
    int resourceId;
    public adapterSearch(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView textView_name = view.findViewById(R.id.trash_name_search);
        TextView textView_type = view.findViewById(R.id.trash_type_search);
        String[] Reg = Objects.requireNonNull(getItem(position)).split(",");
        textView_name.setText(Reg[2].split(":")[1].split("\"")[1]);
        if((Reg[0].split(":")[1]).matches("[\\s\\S]*1[\\s\\S]*"))
            textView_type.setText("可回收垃圾");
        else if((Reg[0].split(":")[1]).matches("[\\s\\S]*4[\\s\\S]*"))
            textView_type.setText("厨余垃圾");
        else if((Reg[0].split(":")[1]).matches("[\\s\\S]*2[\\s\\S]*"))
            textView_type.setText("有害垃圾");
        else
            textView_type.setText("其它垃圾");
        return view;
    }
}
