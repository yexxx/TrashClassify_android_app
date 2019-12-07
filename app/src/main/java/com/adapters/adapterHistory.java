package com.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trash.R;
import com.utils.HistoryItem;

import java.util.List;

public class adapterHistory extends ArrayAdapter<HistoryItem> {
    int resourceId;
    public adapterHistory(Context context, int textViewResourceId, List<HistoryItem> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        HistoryItem historyItem = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imageView = view.findViewById(R.id.history_image);
        TextView nameText = view.findViewById(R.id.history_name);
        TextView textView = view.findViewById(R.id.history_text);
        try {
            assert historyItem != null;
            imageView.setImageBitmap(historyItem.getPic());
            nameText.setText(historyItem.getPicName());
            textView.setText(historyItem.getPicText());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return view;
    }
}
