package com.rony.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rony.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CoordinatorTypeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText coordinatorTypeEditText;
    AppCompatButton saveButton;

    AppCompatImageView imageBack;

    String type, uid;

    DatabaseReference dbType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_type);

        initialization();

        setListener();
        
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void initialization() {

        dbType = FirebaseDatabase.getInstance().getReference().child("Coordinator Type");

        coordinatorTypeEditText = findViewById(R.id.coordinatorTypeEditText);
        saveButton = findViewById(R.id.saveButton);
        imageBack = findViewById(R.id.imageBack);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.saveButton:
                checkValidation();
                break;

        }
    }

    private void checkValidation() {
        type = coordinatorTypeEditText.getText().toString().trim();

        if (type.isEmpty()){
            Toast.makeText(this, "Add Type", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            saveType();
        }
    }

    private void saveType() {

        uid = dbType.push().getKey();

        HashMap hashMap = new HashMap();
        hashMap.put("type", type);
        hashMap.put("uid", uid);

        dbType.child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    coordinatorTypeEditText.setText("");
                    Toast.makeText(CoordinatorTypeActivity.this, "Type Added Successfully", Toast.LENGTH_SHORT).show();

                    finish();
                    Animatoo.animateSwipeRight(CoordinatorTypeActivity.this);

                }
                else {
                    Toast.makeText(CoordinatorTypeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(CoordinatorTypeActivity.this);
    }

}