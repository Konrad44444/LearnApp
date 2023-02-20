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
    private Button readyButton, hintButton;
    private EditText definitionTextEdit;
    private TextView descriptionTextView, correctAfterCheck, insertedAfterCheck, remainText;
    private HashMap<String, String> defDescList = new HashMap<>();
    private ArrayList<String> definitionsArray = new ArrayList<>();
    private String tableName;
    private RelativeLayout check, mainLay;
    int currentWord = -1, notCorrectCnt = 0;
    boolean wasGuessed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Intent intent = getIntent();

        definitionTextEdit = findViewById(R.id.def); //TextEdit przed wpisaniem
        descriptionTextView = findViewById(R.id.desc); //TextView przed wpisaniem
        readyButton = findViewById(R.id.readyButton); //Button
        tableName = intent.getStringExtra(MESSAGE);
        check = findViewById(R.id.check); //layout po wpisaniu
        mainLay = findViewById(R.id.mainLayout); //layout przed wpisaniem
        correctAfterCheck = findViewById(R.id.correct); //TextView w check
        insertedAfterCheck = findViewById(R.id.inserted); //TextVew w check
        remainText = findViewById(R.id.remainText); //wyświetlanie liczby pozostałych słów
        hintButton = findViewById(R.id.hintButton); //podpowiedź - pierwsza litera

        db.open();
        Cursor cursor = db.getDataFromTable(tableName);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            defDescList.put(cursor.getString(1), cursor.getString(2));
            definitionsArray.add(cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();

        readyButton.setOnClickListener(View -> {
            check.setVisibility(View.VISIBLE);
            String insertedWord = definitionTextEdit.getText().toString();

            correctAfterCheck.setText(definitionsArray.get(currentWord));
            insertedAfterCheck.setText(insertedWord);

            if(definitionsArray.get(currentWord).equals(insertedWord)) {
                check.setBackground(ContextCompat.getDrawable(this, R.drawable.border_green));
                insertedAfterCheck.setTextColor(ContextCompat.getColor(this, R.color.greenBorder));
                wasGuessed = true;
            } else {
                check.setBackground(ContextCompat.getDrawable(this, R.drawable.border_red));
                insertedAfterCheck.setTextColor(ContextCompat.getColor(this, R.color.redBorder));
                notCorrectCnt++;
                wasGuessed = false;
            }
        });

        check.setOnClickListener(View -> {
            currentWord = displayRandom(currentWord, wasGuessed);
        });

        currentWord = displayRandom(currentWord, wasGuessed);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        hintButton.setOnClickListener(View -> {
           if(String.valueOf(definitionTextEdit.getText()).equals("")) {
                String word = definitionsArray.get(currentWord);
                word = String.valueOf(word.charAt(0));
                definitionTextEdit.setText(word);

                definitionTextEdit.setSelection(definitionTextEdit.getText().length());
           }
        });
    }

    private int displayRandom(int prev, boolean wasGuessed) {
        mainLay.setVisibility(View.VISIBLE);
        check.setVisibility(View.GONE);
        definitionTextEdit.setText("");
        definitionTextEdit.requestFocus();

        if(definitionsArray.size() == 0) {
            Toast.makeText(this, "Koniec nauki", Toast.LENGTH_LONG).show();
            SystemClock.sleep(50);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            finish();
        }

        if(wasGuessed && prev > -1 && prev < definitionsArray.size()) definitionsArray.remove(prev);

        int rand = (int) ((Math.random() * 10000) % definitionsArray.size()) ;
        descriptionTextView.setText(defDescList.get(definitionsArray.get(rand)));

        remainText.setText("");
        remainText.setText(R.string.remain);
        remainText.append(" ");
        remainText.append( String.valueOf(definitionsArray.toArray().length));

        return rand;
    }
}