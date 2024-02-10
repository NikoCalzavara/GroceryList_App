package com.example.grocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends ArrayAdapter<String> {
    ArrayList<String> list;
    HashMap<String, Integer> quantityMap;
    Context context;

    public ListViewAdapter(Context context, ArrayList<String> items, HashMap<String, Integer> quantityMap){
        super(context, R.layout.list_row, items);
        this.context = context;
        list = items;
        this.quantityMap = quantityMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, parent, false);

            holder = new ViewHolder();
            holder.number = convertView.findViewById(R.id.number);
            holder.name = convertView.findViewById(R.id.name);
            holder.duplicate = convertView.findViewById(R.id.copy);
            holder.remove = convertView.findViewById(R.id.remove);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String itemName = list.get(position);
        int quantity = quantityMap.get(itemName);

        holder.number.setText(String.valueOf(quantity));
        holder.name.setText(itemName);

        holder.duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = quantityMap.get(itemName) + 1;
                quantityMap.put(itemName, newQuantity);
                notifyDataSetChanged();
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = quantityMap.get(itemName) - 1;
                if (newQuantity > 0) {
                    quantityMap.put(itemName, newQuantity);
                } else {
                    quantityMap.remove(itemName);
                    list.remove(itemName);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView name;
        ImageView duplicate;
        ImageView remove;
    }
}
