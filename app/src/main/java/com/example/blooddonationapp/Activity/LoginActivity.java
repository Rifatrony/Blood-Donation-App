package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText loginNumberEditText, loginPasswordEditText;
    AppCompatButton loginButton;
    ProgressBar progressBar;
    TextView noAccountTextView;

    String number, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        setListener();

    }

    private void initialization() {
        loginNumberEditText = findViewById(R.id.loginNumberEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        noAccountTextView = findViewById(R.id.noAccountTextView);

        progressBar = findViewById(R.id.progressBar);

    }

    private void setListener() {

        noAccountTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);

    }


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.noAccountTextView:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                checkValidation();
        }
    }

    private void checkValidation() {
        number = loginNumberEditText.getText().toString().trim();
        password = loginPasswordEditText.getText().toString().trim();

        if (number.isEmpty()){
            showToast("Enter Phone Number");
            return;
        }

        if (password.isEmpty()){
            showToast("Enter Password");
            return;
        }

        else {
            userLogin();
        }

    }

    private void userLogin() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}