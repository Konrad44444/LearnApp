package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class ShowSet extends AppCompatActivity {
    private final String MESSAGE = "tableName";
    private Button addElement, learn, deleteSet, goBack;
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

        deleteSet.setOnClickListener(View -> {
            db.open();
            db.deleteTable(tableName);
            db.close();

            goBack.callOnClick();
        });

        goBack.setOnClickListener(View -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }
}