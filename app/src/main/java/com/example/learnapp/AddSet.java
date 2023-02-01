package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddSet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set);

        EditText setName = findViewById(R.id.addSetNameInput);
        Button addSet = findViewById(R.id.addSet);
        SetsDb db = new SetsDb(this);

        setName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addSet.setOnClickListener(View->{
            String newSet = String.valueOf(setName.getText());
            if(!newSet.equals("")) {
                //newSet = newSet.replace(" ", "@"); //żeby można było dać spację w nazwie
                db.open();
                db.createTable(newSet);
                db.close();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Proszę podać nazwę zestawu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}