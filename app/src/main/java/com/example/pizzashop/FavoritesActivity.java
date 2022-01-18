package com.example.pizzashop;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pizzashop.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    List favoritePizzas;
    ListView pizzaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritePizzas = new ArrayList();
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        pizzaList = findViewById(R.id.favorites_list);

        this.populateData();
    }

    @Override
    protected void onDestroy() {
        database.close();
        dbHelper.close();
        super.onDestroy();
    }

    private void populateData() {
        Cursor dataCursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

        if (dataCursor.getCount() == 0) {
            Toast.makeText(FavoritesActivity.this, "Favorites list is empty", Toast.LENGTH_SHORT).show();
        } else {
            String name;
            String desc;
            int price;

            while (dataCursor.moveToNext()) {
                name = dataCursor.getString(dataCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PIZZA_NAME));
                desc = dataCursor.getString(dataCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PIZZA_DESC));
                price = dataCursor.getInt(dataCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PIZZA_PRICE));
                favoritePizzas.add(new Pizza(name, desc, price));
            }

            // TODO: Create custom adapter
            ArrayAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoritePizzas);
            pizzaList.setAdapter(listAdapter);
        }
        dataCursor.close();
    }
}