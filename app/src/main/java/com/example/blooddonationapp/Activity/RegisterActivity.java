package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.R;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView haveAccountTextView;
    EditText emailEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText;
    AppCompatButton registerButton;

    CountryCodePicker countryCodePicker;

    String ccp, email, number, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialization();
        setListener();

    }

    private void initialization(){
        countryCodePicker = findViewById(R.id.countryCodePicker);

        haveAccountTextView = findViewById(R.id.haveAccountTextView);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        registerButton = findViewById(R.id.registerButton);
    }

    private void setListener(){
        haveAccountTextView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.haveAccountTextView:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.registerButton:
                checkValidation();
        }
    }

    private void checkValidation() {
        email = emailEditText.getText().toString().trim();
        number = phoneNumberEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirm_password = confirmPasswordEditText.getText().toString().trim();
        ccp = countryCodePicker.getSelectedCountryName().toString().trim();

        System.out.println("Selected country is ======>" + ccp);


        if (email.isEmpty()){
            showToast("Email Required");
            return;
        }

        if (number.isEmpty()){
            showToast("Number Required");
            return;
        }
        if (password.isEmpty()){
            showToast("Password Required");
            return;
        }
        if (confirm_password.isEmpty()){
            showToast("Confirm Password Required");
            return;
        }
        /*if (confirm_password != password){
            showToast("Password and Confirm Password Should be Same");
            return;
        }*/
        else {
            Intent intent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            startActivity(intent);
            //registerNewUser();
        }
    }

    private void registerNewUser(){
        showToast("Success");

    }

}