package com.example.learnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    private final String MESSAGE = "tableName";
    private Set set;
    private ArrayList<String> definitions;
    private ArrayList<String> definitionsCopy;
    private HashMap<String, String> defDescMap;
    private String tableName;

    //layout elements
    TextView setName, descriptionTextView;
    ArrayList<Button> answer;
    Button goNextButton;

    private int correct;
    ArrayList<Boolean> available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        tableName = intent.getStringExtra(MESSAGE);

        set = new Set(tableName, this);
        set.getData();

        definitions = set.getDefinitions();
        definitionsCopy = set.getDefinitions();
        defDescMap = set.getDefDescMap();

        available = new ArrayList<>(4);
        available = getTrueArray();

        //layout
        setName = findViewById(R.id.setName);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        answer = new ArrayList<>(4);
        answer.add(0, findViewById(R.id.answer1));
        answer.add(1, findViewById(R.id.answer2));
        answer.add(2, findViewById(R.id.answer3));
        answer.add(3, findViewById(R.id.answer4));

        goNextButton = findViewById(R.id.goNext);

        if(set.getQuantity() < 4) {
            Toast.makeText(this, "Zestaw ma za mało pojęć", Toast.LENGTH_SHORT).show();

            Intent intent1 = new Intent(getApplicationContext(), ShowSet.class);
            intent1.putExtra(MESSAGE, tableName);
            startActivity(intent1);
            finish();
        }

        doTest();

    }

    @NonNull
    private ArrayList<Boolean> getTrueArray() {
        ArrayList<Boolean> res = new ArrayList<>(4);
        available.add(true);
        available.add(true);
        available.add(true);
        available.add(true);

        return res;
    }

    private boolean allAvailableFalse() {
        return !available.get(0) && !available.get(1) && !available.get(2) && !available.get(3);
    }

    private void doTest() {
        if(definitions.size() == 0) {
            Toast.makeText(this, "Test zakończony", Toast.LENGTH_SHORT).show();

            Intent intent1 = new Intent(getApplicationContext(), ShowSet.class);
            intent1.putExtra(MESSAGE, tableName);
            startActivity(intent1);
            finish();
        }

        //wylosowanie pojęcia
        int rand = (int) ((Math.random() * 1000000) % definitions.size());
        String pickedDef = definitions.get(rand);

        //wypisanie definicji
        descriptionTextView.setText(defDescMap.get(pickedDef));

        //wylosowanie przycisku
        int randPlace = (int) ((Math.random() * 100) % 4);

        answer.get(randPlace).setText(pickedDef);
        available.set(randPlace, false);

        //oznaczenie poprawnej odpowiedzi
        correct = randPlace;

        //zapełnienie innych odpowiedzi
//        while(!allAvailableFalse()) {
//            randPlace = (int) ((Math.random() * 100) % 4);
//
//            rand = (int) ((Math.random() * 1000000) % definitions.size());
//            String randDef = definitions.get(rand);
//
//            if(available.get(randPlace) && !randDef.equals(pickedDef)) {
//                available.set(randPlace, false);
//                answer.get(randPlace).setText(randDef);
//            }
//        }

    }
}