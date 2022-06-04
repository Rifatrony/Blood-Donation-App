package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blooddonationapp.R;

public class AccountTypeActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton donorAccountButton, searchAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        initialization();
        setListener();

    }

    private void initialization() {
        donorAccountButton = findViewById(R.id.donorAccountButton);
        searchAccountButton = findViewById(R.id.searchAccountButton);
    }

    private void setListener() {
        donorAccountButton.setOnClickListener(this);
        searchAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.donorAccountButton:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("donor", "");
                startActivity(intent);


            case R.id.searchAccountButton:
                Toast.makeText(this, "Create Search type account", Toast.LENGTH_SHORT).show();
        }
    }
}