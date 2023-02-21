package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LearnActivity extends AppCompatActivity {
    private final String MESSAGE = "tableName";
    private SetsDb db = new SetsDb(this);
    private Button readyButton, hintButton, goToMenu;
    private EditText definitionTextEdit;
    private TextView descriptionTextView, correctAfterCheck, insertedAfterCheck, remainText, wordsCnt, errCnt, mostErrDef, mostErrDesc;
    private HashMap<String, String> defDescList = new HashMap<>();
    private ArrayList<String> definitionsArray = new ArrayList<>();
    private ArrayList<String> definitionsArrayCopy = new ArrayList<>();
    private String tableName;
    private RelativeLayout check, mainLay, endLay;
    int currentWord = -1, notCorrectCnt = 0;
    boolean wasGuessed = false;
    int[] errorsCount;


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

        //ekran końcowy
        endLay = findViewById(R.id.endingLayout);
        goToMenu=findViewById(R.id.goToMenu);
        wordsCnt = findViewById(R.id.wordsCnt);
        errCnt = findViewById(R.id.errCnt);
        mostErrDef = findViewById(R.id.mostErrDefinition);
        mostErrDesc = findViewById(R.id.mostErrDescription);

        db.open();
        Cursor cursor = db.getDataFromTable(tableName);

        cursor.moveToFirst();
        int c = 0;
        while(!cursor.isAfterLast()) {
            defDescList.put(cursor.getString(1), cursor.getString(2));
            definitionsArray.add(cursor.getString(1));
            definitionsArrayCopy.add(cursor.getString(1));
            c++;
            cursor.moveToNext();
        }
        db.close();

        //ekran końcowy
        errorsCount = new int[c];
        Arrays.fill(errorsCount, 0);//zainicjalizowane zerami


        readyButton.setOnClickListener(View -> {
            check.setVisibility(android.view.View.VISIBLE);
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
                errorsCount[currentWord]++;
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

        goToMenu.setOnClickListener(View -> {
            Intent intentGoToMenu = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentGoToMenu);

            finish();
        });
    }

    private int displayRandom(int prev, boolean wasGuessed) {
        mainLay.setVisibility(View.VISIBLE);
        check.setVisibility(View.GONE);
        definitionTextEdit.setText("");
        definitionTextEdit.requestFocus();

        if (wasGuessed && prev > -1 && prev < definitionsArray.size())
            definitionsArray.remove(prev);

        if(definitionsArray.toArray().length == 0) {
            //Toast.makeText(this, "pusty", Toast.LENGTH_SHORT).show();
            drawEnd();


            return -1;
        }

        int rand = (int) ((Math.random() * 10000) % definitionsArray.size());
        descriptionTextView.setText(defDescList.get(definitionsArray.get(rand)));

        remainText.setText("");
        remainText.setText(R.string.remain);
        remainText.append(" ");
        remainText.append(String.valueOf(definitionsArray.toArray().length));

        return rand;
    }

    private void drawEnd() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        remainText.setVisibility(View.INVISIBLE);
        endLay.setVisibility(android.view.View.VISIBLE);

        //wyświetlenie podsumowania
        //wyświetlenie ilości słów w zestawie
        wordsCnt.setText(" ");
        wordsCnt.append(String.valueOf(definitionsArrayCopy.toArray().length));
        wordsCnt.append(" ");

        //wyświetlenie ilości błędów
        errCnt.setText(" ");
        errCnt.append(String.valueOf(notCorrectCnt));
        errCnt.append(" ");

        //najczęściej popełniany błąd
        if(notCorrectCnt != 0) {
            int mostErrors = maxIndex(errorsCount);

            mostErrDef.setText(definitionsArrayCopy.get(mostErrors));
            mostErrDesc.setText(defDescList.get(definitionsArrayCopy.get(mostErrors)));
        } else {
            TextView t = findViewById(R.id.mostErrorsTextView);
            t.setText("");
        }
    }

    private int maxIndex(int[] array) {
        if(array.length != 0) {
            int max = 0;
            for(int i = 1; i < array.length; i++) {
                if(array[i] > array[max]) max = i;
            }

            return max;
        }
        return -1;
    }
}