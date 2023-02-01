package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class LearnActivity extends AppCompatActivity {
    private final String MESSAGE = "tableName";
    private SetsDb db = new SetsDb(this);
    private Button readyButton;
    private EditText definition;
    private TextView description, correct, inserted;
    private HashMap<String, String> defDescList = new HashMap<>();
    private ArrayList<String> definitions = new ArrayList<>();
    private String tableName;
    private RelativeLayout check, mainLay;
    int currentWord = -1;
    boolean wasGuessed = false, doLoop = true, goNext = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Intent intent = getIntent();

        definition = findViewById(R.id.def); //TextEdit przed wpisaniem
        description = findViewById(R.id.desc); //TextView przed wpisaniem
        readyButton = findViewById(R.id.readyButton); //Button
        tableName = intent.getStringExtra(MESSAGE);
        check = findViewById(R.id.check); //layout po wpisaniu
        mainLay = findViewById(R.id.mainLayout); //layout przed wpisaniem
        correct = findViewById(R.id.correct); //TextView w check
        inserted = findViewById(R.id.inserted); //TextVew w check

        db.open();
        Cursor cursor = db.getDataFromTable(tableName);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            defDescList.put(cursor.getString(1), cursor.getString(2));
            definitions.add(cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();

        readyButton.setOnClickListener(View -> {
            check.setVisibility(View.VISIBLE);
            String insertedWord = definition.getText().toString();

            correct.setText(definitions.get(currentWord));
            inserted.setText(insertedWord);

            if(definitions.get(currentWord).equals(insertedWord)) {
                check.setBackground(ContextCompat.getDrawable(this, R.drawable.border_green));
                inserted.setTextColor(ContextCompat.getColor(this, R.color.greenBorder));
                wasGuessed = true;
            } else {
                check.setBackground(ContextCompat.getDrawable(this, R.drawable.border_red));
                inserted.setTextColor(ContextCompat.getColor(this, R.color.redBorder));
                wasGuessed = false;
            }
        });

        check.setOnClickListener(View -> {
            currentWord = displayRandom(currentWord, wasGuessed);
        });

        currentWord = displayRandom(currentWord, wasGuessed);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private int displayRandom(int prev, boolean wasGuessed) {
        mainLay.setVisibility(View.VISIBLE);
        check.setVisibility(View.GONE);
        definition.setText("");
        definition.requestFocus();

        if(definitions.size() == 0) {
            Toast.makeText(this, "Koniec nauki", Toast.LENGTH_LONG).show();
            SystemClock.sleep(50);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            finish();
        }

        if(wasGuessed && prev > -1 && prev < definitions.size()) definitions.remove(prev);

        int rand = (int) ((Math.random() * 10000) % definitions.size()) ;
        description.setText(defDescList.get(definitions.get(rand)));
        return rand;
    }
}