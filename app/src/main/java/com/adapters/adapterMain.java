package com.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trash.R;
import com.ui.Feedback;

import java.util.ArrayList;

public class adapterMain extends ArrayAdapter<String> {
    private int resourceId;
    private Context context;

    public adapterMain(Context context, int textViewResourceId, ArrayList<String> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        final String s = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView textView = view.findViewById(R.id.trash_name);
        textView.setText(s);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Feedback().showAlertDialog(s, context, "modify");
            }
        });
        return view;
    }
}
