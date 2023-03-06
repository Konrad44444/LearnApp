package com.example.learnapp;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class Set {
    private final String name;
    private final SetsDb db;
    private ArrayList<String> definitions;
    private HashMap<String, String> defDescMap;
    private int quantity;

    public Set(String name, Context context) {
        this.name = name;
        this.db = new SetsDb(context);

        this.definitions = new ArrayList<>();
        this.defDescMap = new HashMap<>();
        this.quantity = 0;
    }

    public void getData() {
        this.db.open();
        Cursor cursor = db.getDataFromTable(this.name);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            defDescMap.put(cursor.getString(1), cursor.getString(2));
            definitions.add(cursor.getString(1));
            this.quantity++;
            cursor.moveToNext();
        }
        db.close();
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    public HashMap<String, String> getDefDescMap() {
        return defDescMap;
    }

    public int getQuantity() {
        return quantity;
    }
}
