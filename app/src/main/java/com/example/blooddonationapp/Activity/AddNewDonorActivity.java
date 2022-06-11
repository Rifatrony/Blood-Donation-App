package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AddNewDonorActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatImageView imageBack;
    AppCompatButton addNewDonorButton;

    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    String name, email, password, confirm_password;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_donor);

        initialization();
        setListener();

    }

    private void initialization() {

        mAuth = FirebaseAuth.getInstance();

        imageBack = findViewById(R.id.imageBack);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        addNewDonorButton = findViewById(R.id.addNewDonorButton);

    }

    private void setListener (){
        imageBack.setOnClickListener(this);
        addNewDonorButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;
            case R.id.addNewDonorButton:
                addNewDonor();
                break;

        }
    }

    private void addNewDonor() {

        name = nameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirm_password = confirmPasswordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    nameEditText.setText("");
                    emailEditText.setText("");
                    passwordEditText.setText("");
                    confirmPasswordEditText.setText("");
                    Toast.makeText(AddNewDonorActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                    


                }
            }
        });

    }

    public void checkButton(View view) {

    }
}