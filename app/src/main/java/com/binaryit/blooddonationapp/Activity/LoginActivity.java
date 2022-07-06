package com.binaryit.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.MainActivity;
import com.binaryit.blooddonationapp.R;
import com.binaryit.blooddonationapp.Utilities.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText loginEmailEditText, loginPasswordEditText;
    AppCompatButton loginButton;
    ProgressBar progressBar;
    TextView noAccountTextView;

    String email, password;

    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener authStateListener;

    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        setListener();

        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Animatoo.animateSwipeLeft(LoginActivity.this);
                }

            }
        };

    }

    private void initialization() {

        loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        noAccountTextView = findViewById(R.id.noAccountTextView);

        progressBar = findViewById(R.id.progressBar);

        //sessionManagement = new SessionManagement(this);
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
                Animatoo.animateSwipeLeft(LoginActivity.this);
                break;
            case R.id.loginButton:
                checkValidation();
        }
    }

    private void checkValidation() {
        email = loginEmailEditText.getText().toString().trim();
        password = loginPasswordEditText.getText().toString().trim();

        if (email.isEmpty()){
            showToast("Enter Email");
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

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    showToast("Login Successfully");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    Animatoo.animateSwipeLeft(LoginActivity.this);

                    finish();

                    loginEmailEditText.setText("");
                    loginPasswordEditText.setText("");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                loginEmailEditText.setText("");
                loginPasswordEditText.setText("");
                showToast(e.getMessage());

            }
        });
    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(authStateListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);

    }
}