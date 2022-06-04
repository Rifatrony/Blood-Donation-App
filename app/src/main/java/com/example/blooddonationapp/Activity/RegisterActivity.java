package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.Model.UserRegisterModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    TextView haveAccountTextView;
    EditText emailEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText;
    AppCompatButton registerButton;

    CountryCodePicker countryCodePicker;

    String ccp, email, number, password, confirm_password;
    String uid;

    FirebaseAuth mAuth;
    DatabaseReference dbUserInfo;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialization();
        setListener();

    }

    private void initialization(){

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        countryCodePicker = findViewById(R.id.countryCodePicker);

        haveAccountTextView = findViewById(R.id.haveAccountTextView);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        registerButton = findViewById(R.id.registerButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbUserInfo = FirebaseDatabase.getInstance().getReference().child("Donor List").child("Donor Details");

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
        if (!confirm_password.equals(password)){
            showToast("Password and Confirm Password Should be Same");
            return;
        }
        else {
            /*Intent intent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            startActivity(intent);*/
            registerNewUser();
        }
    }

    private void registerNewUser(){

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    registerButton.setVisibility(View.VISIBLE);

                    emailEditText.setText("");
                    passwordEditText.setText("");
                    phoneNumberEditText.setText("");
                    confirmPasswordEditText.setText("");
                    showToast("Register Successfully");

                    uid = dbUserInfo.push().getKey();

                    UserRegisterModel obj = new UserRegisterModel(ccp, email, number, password, null, null, null,null, null, null, uid);

                    dbUserInfo.setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                            startActivity(intent);
                            showToast("Update Profile");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);
                emailEditText.setText("");
                passwordEditText.setText("");
                phoneNumberEditText.setText("");
                confirmPasswordEditText.setText("");
                showToast(e.getMessage().toString());
            }
        });
    }
}