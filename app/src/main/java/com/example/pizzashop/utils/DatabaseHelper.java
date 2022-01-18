package com.example.pizzashop.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pizza_shop.db";
    private static final int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "favorites";
    public final static String ID = "_ID";
    public final static String COLUMN_PIZZA_NAME = "name";
    public final static String COLUMN_PIZZA_DESC = "description";
    public final static String COLUMN_PIZZA_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUERY = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER)",
                TABLE_NAME,
                ID,
                COLUMN_PIZZA_NAME,
                COLUMN_PIZZA_DESC,
                COLUMN_PIZZA_PRICE
        );

        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        onCreate(db);
    }
}

