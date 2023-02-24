package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowSetElements extends AppCompatActivity {

    private final String MESSAGE = "tableName";
    private String tableName;
    private SetsDb db = new SetsDb(this);
    private TextView setNameTextView;
    private Button goBackButton;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private List<String> definitionsList;
    private HashMap<String, String> defDescHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_set_elements);
        Intent intent = getIntent();

        tableName = intent.getStringExtra(MESSAGE);
        setNameTextView = (TextView) findViewById(R.id.setName);
        goBackButton = (Button) findViewById(R.id.goBackButton);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        definitionsList = new ArrayList<>();
        defDescHashMap = new HashMap<>();

        setNameTextView.setText(tableName);

        goBackButton.setOnClickListener(View -> {
            Intent intent1 = new Intent(getApplicationContext(), ShowSet.class);
            intent1.putExtra(MESSAGE, tableName);
            startActivity(intent1);
            finish();
        });

        db.open();
        Cursor cursor = db.getDataFromTable(tableName);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            defDescHashMap.put(cursor.getString(1), cursor.getString(2));
            definitionsList.add(cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();

        if(definitionsList.isEmpty()) {
            Toast.makeText(this, "Zestaw pusty, dodaj pojęcia", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), ShowSet.class);
            intent2.putExtra(MESSAGE, tableName);
            startActivity(intent2);
            finish();
        }

        expandableListAdapter = new ExpandableListAdapter(this, definitionsList, defDescHashMap);

        expandableListView.setAdapter(expandableListAdapter);

    }
}