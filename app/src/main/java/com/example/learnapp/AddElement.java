package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddElement extends AppCompatActivity {
    private final String DESC = "description";
    private final String DEF = "definition";
    private final String MESSAGE = "tableName";
    private SetsDb db;
    private Button addToDb, goBack;
    private EditText def, desc;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_element);
        db = new SetsDb(this);
        addToDb = findViewById(R.id.addElementButton);
        goBack = findViewById(R.id.goToAddElementActivity);
        def = findViewById(R.id.addDef);
        desc = findViewById(R.id.addDesc);
        Intent intent = getIntent();
        tableName = intent.getStringExtra(MESSAGE);

        def.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        db.open();

        addToDb.setOnClickListener(View -> {
            String defText = def.getText().toString();
            String descText = desc.getText().toString();
            if(!defText.equals("") && !descText.equals("")) {
                ContentValues values = new ContentValues();
                values.put(DEF, defText);
                values.put(DESC, descText);
                db.addToTable(tableName, values);

                def.setText("");
                desc.setText("");
                def.requestFocus();
            } else {
                Toast.makeText(this, "Proszę podać wszystkie dane", Toast.LENGTH_SHORT).show();
            }
        });

        goBack.setOnClickListener(View -> {
            db.close();
            Intent intent1 = new Intent(getApplicationContext(), ShowSet.class);
            intent1.putExtra(MESSAGE, tableName);
            startActivity(intent1);
            finish();
        });
    }
}