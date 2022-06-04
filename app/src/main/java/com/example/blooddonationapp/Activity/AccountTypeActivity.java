package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.blooddonationapp.R;

public class AccountTypeActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton donorAccountButton, recipientAccountButton;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String clickedItemIs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        initialization();
        setListener();


    }

    private void initialization() {
        donorAccountButton = findViewById(R.id.donorAccountButton);
        recipientAccountButton = findViewById(R.id.recipientAccountButton);
    }


    private void setListener() {
        donorAccountButton.setOnClickListener(this);
        recipientAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.donorAccountButton:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
                break;

            case R.id.recipientAccountButton:
                startActivity(new Intent(getApplicationContext(), RecipientActivity.class));
                finish();
                break;
        }
    }
}