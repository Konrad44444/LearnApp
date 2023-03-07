package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class ShowSet extends AppCompatActivity {
    private final String MESSAGE = "tableName";
    private Button addElement, learn, deleteSet, goBack, showSetElementsButton, test;
    private TextView setName;
    private String tableName, setNameShown;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_set);

        SetsDb db = new SetsDb(this);
        addElement = findViewById(R.id.addElement);
        learn = findViewById(R.id.learn);
        deleteSet = findViewById(R.id.deleteSet);
        goBack = findViewById(R.id.returnToMain);
        setName = findViewById(R.id.showSetName);
        showSetElementsButton = (Button) findViewById(R.id.showSetElements);
        test = findViewById(R.id.test);
        intent = getIntent();
        tableName = intent.getStringExtra(MESSAGE);
        //setNameShown = tableName.replace("@", " ");

        setName.setText(tableName);

        addElement.setOnClickListener(View -> {
            Intent intent1 = new Intent(getApplicationContext(), AddElement.class);
            intent1.putExtra(MESSAGE, tableName);
            startActivity(intent1);
        });

        learn.setOnClickListener(View -> {
            Intent intent2 = new Intent(getApplicationContext(), LearnActivity.class);
            intent2.putExtra(MESSAGE, tableName);
            startActivity(intent2);
            finish();
        });

        showSetElementsButton.setOnClickListener(View -> {
            Intent intent3 = new Intent(getApplicationContext(), ShowSetElements.class);
            intent3.putExtra(MESSAGE, tableName);
            startActivity(intent3);
        });

        deleteSet.setOnClickListener(View -> {
            db.open();
            db.deleteTable(tableName);
            db.close();

            goBack.callOnClick();
        });

        test.setOnClickListener(View -> {
            Intent intent4 = new Intent(getApplicationContext(), TestActivity.class);
            intent4.putExtra(MESSAGE, tableName);
            startActivity(intent4);
        });

        goBack.setOnClickListener(View -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }
}