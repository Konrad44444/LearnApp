package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private SetsDb db = new SetsDb(this);
    private static ArrayList<String> sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        drawSets();

        Button mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(View -> {
            Intent addSet = new Intent(getApplicationContext(), AddSet.class);
            startActivity(addSet);
        });


        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(View -> {
            finishAffinity();
            System.exit(0);
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        drawSets();
    }

    private void drawSets() {
        TableLayout table = findViewById(R.id.table);
        table.removeAllViews();

        db.open();
        sets = db.getTables();
        for(int i = 3; i < sets.size(); i++) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.MyThemeSetButton), null, 0);
            String setName = String.valueOf(sets.get(i));
            //String setNameShown = setName.replace("@", " ");
            button.setText(setName);
            button.setElevation(5);
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
//            params.setMargins(0, 0, 0, 25);
//            button.setLayoutParams(params);
            button.setOnClickListener(View -> {
                Intent startSet = new Intent(getApplicationContext(), ShowSet.class);
                startSet.putExtra("tableName", setName);
                startActivity(startSet);
            });
            table.addView(button);
        }
        db.close();
    }
}