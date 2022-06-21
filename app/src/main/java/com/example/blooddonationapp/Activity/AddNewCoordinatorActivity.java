package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddNewCoordinatorActivity extends AppCompatActivity implements View.OnClickListener {

    EditText coordinatorNameEditText, coordinatorPhoneNumberEditText, coordinatorAddressEditText;
    AppCompatButton addNewCoordinatorButton;
    ProgressBar progressBar;
    AppCompatImageView imageBack;

    String name, number, address;

    DatabaseReference dbCoordinator;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid, added_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_coordinator);

        initialization();
        setListener();

    }

    private void initialization() {
        coordinatorNameEditText = findViewById(R.id.coordinatorNameEditText);
        coordinatorPhoneNumberEditText = findViewById(R.id.coordinatorPhoneNumberEditText);
        coordinatorAddressEditText = findViewById(R.id.coordinatorAddressEditText);
        addNewCoordinatorButton = findViewById(R.id.addNewCoordinatorButton);
        progressBar = findViewById(R.id.progressBar);
        imageBack = findViewById(R.id.imageBack);

        dbCoordinator = FirebaseDatabase.getInstance().getReference().child("Coordinator");


    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        addNewCoordinatorButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.addNewCoordinatorButton:
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        name = coordinatorNameEditText.getText().toString().trim();
        number = coordinatorPhoneNumberEditText.getText().toString().trim();
        address = coordinatorAddressEditText.getText().toString().trim();

        if (name.isEmpty()){
            showToast("Enter Name");
            return;
        }

        if (number.isEmpty()){
            showToast("Enter Number");
            return;
        }
        if (address.isEmpty()){
            showToast("Enter Address");
            return;
        }

        else {
            addCoordinator();
        }

    }

    private void addCoordinator() {

        progressBar.setVisibility(View.VISIBLE);
        addNewCoordinatorButton.setVisibility(View.GONE);

        uid = dbCoordinator.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        added_by = user.getUid();

        System.out.println("KEY is : " + uid);

        CoordinatorModel coordinatorModel = new CoordinatorModel(name, number, uid, added_by, address);

        dbCoordinator.child(uid).setValue(coordinatorModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                progressBar.setVisibility(View.GONE);
                addNewCoordinatorButton.setVisibility(View.INVISIBLE);

                coordinatorNameEditText.setText("");
                coordinatorPhoneNumberEditText.setText("");
                showToast("Added");
                finish();
            }
            else {
                showToast(task.getException().toString());
            }
        });

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}