package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_NAME = "GroceryListPrefs";
    private static final String ITEM_KEY_PREFIX = "item_";
    private static final String QUANTITY_KEY_PREFIX = "quantity_";

    ListView listView;
    ArrayList<String> items;
    HashMap<String, Integer> quantityMap;
    ListViewAdapter adapter;
    EditText input;
    ImageView enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);

        loadListFromSharedPreferences();

        adapter = new ListViewAdapter(getApplicationContext(), items, quantityMap);
        listView.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString().replaceAll("[^a-zA-Z]", "").trim();
                if (text.isEmpty()) {
                    makeToast("Write something!");
                } else {
                    addItem(text);
                    input.setText("");
                    makeToast("Added: " + text);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItem(position);
                return true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveListToSharedPreferences();
    }

    private void saveListToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("list_size", items.size());
        for (int i = 0; i < items.size(); i++) {
            editor.putString(ITEM_KEY_PREFIX + i, items.get(i));
            editor.putInt(QUANTITY_KEY_PREFIX + i, quantityMap.get(items.get(i)));
        }
        editor.apply();
    }

    private void loadListFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("list_size", 0);
        items = new ArrayList<>();
        quantityMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String item = sharedPreferences.getString(ITEM_KEY_PREFIX + i, "");
            int quantity = sharedPreferences.getInt(QUANTITY_KEY_PREFIX + i, 0);
            items.add(item);
            quantityMap.put(item, quantity);
        }
    }

    public void addItem(String item) {
        if (quantityMap.containsKey(item)) {
            int quantity = quantityMap.get(item);
            quantityMap.put(item, quantity + 1);
        } else {
            items.add(item);
            quantityMap.put(item, 1);
        }
        adapter.notifyDataSetChanged();
    }

    public void removeItem(int position) {
        String item = items.get(position);
        items.remove(position);
        quantityMap.remove(item);
        adapter.notifyDataSetChanged();
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
