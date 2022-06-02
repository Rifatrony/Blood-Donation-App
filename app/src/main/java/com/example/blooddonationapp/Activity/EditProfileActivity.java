package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.R;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {


    Spinner bloodGroupSpinner;
    ArrayAdapter<String> adapter;
    String[] bloodGroups = {"A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"};

    AppCompatButton updateButton;
    EditText nameEditText, stateEditText;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialization();
        setListener();

    }

    private void initialization() {

        nameEditText = findViewById(R.id.nameEditText);
        stateEditText = findViewById(R.id.stateEditText);

        updateButton = findViewById(R.id.updateButton);

        progressBar = findViewById(R.id.progressBar);

        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        bloodGroupSpinner.setAdapter(adapter);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               System.out.println("Select Item Name is : " + adapterView.getSelectedItem());

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

    }

    private void setListener() {
        updateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.updateButton){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}