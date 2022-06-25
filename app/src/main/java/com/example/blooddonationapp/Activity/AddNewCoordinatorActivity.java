package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.Model.CoordinatorTypeModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kenmeidearu.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewCoordinatorActivity extends AppCompatActivity implements View.OnClickListener {

    EditText coordinatorNameEditText, coordinatorPhoneNumberEditText, coordinatorAddressEditText;
    AppCompatButton addNewCoordinatorButton;
    ProgressBar progressBar;
    AppCompatImageView imageBack;

    Spinner coordinatorTypeSpinner;

    String name, number,type, address;

    DatabaseReference dbCoordinator, dbType;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid, added_by;

    List<String> coordinatorTypeList;
    ArrayAdapter<String> coordinatorTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_coordinator);

        initialization();
        setListener();

        coordinatorTypeList = new ArrayList<>();
        coordinatorTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, coordinatorTypeList);
        coordinatorTypeSpinner.setAdapter(coordinatorTypeAdapter);

        dbType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coordinatorTypeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CoordinatorTypeModel coordinatorTypeModel = dataSnapshot.getValue(CoordinatorTypeModel.class);
                    if (coordinatorTypeModel != null)
                    {
                        coordinatorTypeList.add(coordinatorTypeModel.getType());

                        //Toast.makeText(RegisterActivity.this, "Count is " + data.getTotal_member(), Toast.LENGTH_SHORT).show();
                    }
                }
                coordinatorTypeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialization() {
        coordinatorTypeSpinner = findViewById(R.id.coordinatorTypeSpinner);
        coordinatorNameEditText = findViewById(R.id.coordinatorNameEditText);
        coordinatorPhoneNumberEditText = findViewById(R.id.coordinatorPhoneNumberEditText);
        coordinatorAddressEditText = findViewById(R.id.coordinatorAddressEditText);
        addNewCoordinatorButton = findViewById(R.id.addNewCoordinatorButton);
        progressBar = findViewById(R.id.progressBar);
        imageBack = findViewById(R.id.imageBack);

        dbCoordinator = FirebaseDatabase.getInstance().getReference().child("Coordinator");
        dbType = FirebaseDatabase.getInstance().getReference().child("Coordinator Type");

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
        type = coordinatorTypeSpinner.getSelectedItem().toString().trim();

        name = coordinatorNameEditText.getText().toString().trim();
        number = coordinatorPhoneNumberEditText.getText().toString().trim();
        address = coordinatorAddressEditText.getText().toString().trim();

        Toast.makeText(this, "Spinner item is " + type, Toast.LENGTH_SHORT).show();

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

        CoordinatorModel coordinatorModel = new CoordinatorModel(name, number, uid, added_by, address, type);

        dbCoordinator.child(uid).setValue(coordinatorModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                progressBar.setVisibility(View.GONE);
                addNewCoordinatorButton.setVisibility(View.INVISIBLE);

                coordinatorNameEditText.setText("");
                coordinatorPhoneNumberEditText.setText("");
                showToast("Added");
                finish();
                Animatoo.animateSwipeRight(AddNewCoordinatorActivity.this);
            }
            else {
                showToast(task.getException().toString());
            }
        });

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(AddNewCoordinatorActivity.this);
    }

}