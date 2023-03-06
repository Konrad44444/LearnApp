package com.example.learnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowSetElements extends AppCompatActivity {

    //główny widok
    private final String MESSAGE = "tableName";
    private final String DEF = "definition";
    private final String DESC = "description";
    private String tableName;
    private Set set;
    private SetsDb db = new SetsDb(this);
    private TextView setNameTextView;
    private Button goBackButton, editButton;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private List<String> definitionsList;
    private HashMap<String, String> defDescHashMap;

    //edytowanie
    private int defID = 0;
    private RelativeLayout editLayout;
    private EditText editDef, editDesc;
    private Button editCancelButton, editSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_set_elements);
        Intent intent = getIntent();

        //inicjalizacja obiektów
        tableName = intent.getStringExtra(MESSAGE);
        setNameTextView = (TextView) findViewById(R.id.setName);
        goBackButton = (Button) findViewById(R.id.goBackButton);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        definitionsList = new ArrayList<>();
        defDescHashMap = new HashMap<>();

        //nazwa zestawu
        setNameTextView.setText(tableName);

        //powrót do ekranu zestawu
        goBackButton.setOnClickListener(View -> {
            Intent intent1 = new Intent(getApplicationContext(), ShowSet.class);
            intent1.putExtra(MESSAGE, tableName);
            startActivity(intent1);
            finish();
        });

        //wyświetlanie listy słówek
        showExpandableList();

        //edytowanie
        editLayout = findViewById(R.id.editLayout);
        editCancelButton = findViewById(R.id.editCancelButton);
        editSaveButton = findViewById(R.id.editSaveButton);
        editDef = findViewById(R.id.editLayoutDef);
        editDesc = findViewById(R.id.editLayoutDesc);

        //kliknięcie w przycisk ołówka - nie działa
//        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View listView = layoutInflater.inflate(R.layout.list_desc_layout, null);
//        editButton = listView.findViewById(R.id.editButton);
//
//        editButton.setOnClickListener(View -> {
//            editLayout.setVisibility(android.view.View.VISIBLE);
//        });


        //kliknięcie w pojęcie (opis)
        expandableListView.setOnChildClickListener((parent, view, groupPosition, childPosition, id) -> {
            editLayout.setVisibility(View.VISIBLE);

            defID = groupPosition + 1; //id w tablicy

            editDef.setText(definitionsList.get(groupPosition));
            editDesc.setText(defDescHashMap.get(definitionsList.get(groupPosition)));

            editDef.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editDef, InputMethodManager.SHOW_IMPLICIT);


            return false;
        });

        //anulowanie edycji
        editCancelButton.setOnClickListener(View -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

            editLayout.setVisibility(android.view.View.GONE);
        });

        //zmiana danych w bazie
        editSaveButton.setOnClickListener(View -> {
            String newDef = editDef.getText().toString();
            String newDesc = editDesc.getText().toString();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DEF, newDef);
            contentValues.put(DESC, newDesc);

            db.open();
            db.updateInTable(tableName, defID, contentValues);
            db.close();

            editCancelButton.callOnClick();

            //ponowne załadowanie listy po edycji
            recreate();
        });
    }

    private void showExpandableList() {
        //pobranie danych z bazy
        set = new Set(tableName, this);
        definitionsList = set.getDefinitions();
        defDescHashMap = set.getDefDescMap();
        int setQuantity = set.getQuantity();

        //zestaw pusty
        if(setQuantity == 0) {
            Toast.makeText(this, "Zestaw pusty, dodaj pojęcia", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), ShowSet.class);
            intent2.putExtra(MESSAGE, tableName);
            startActivity(intent2);
            finish();
        }

        //utworzenie listy
        expandableListAdapter = new ExpandableListAdapter(this, definitionsList, defDescHashMap);

        expandableListView.setAdapter(expandableListAdapter);
    }
}