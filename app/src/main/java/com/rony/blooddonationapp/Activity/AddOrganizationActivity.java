package com.rony.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rony.blooddonationapp.Model.OrganizationModel;
import com.rony.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddOrganizationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText organizationNameEditText, organizationPhoneNumberEditText;
    AppCompatButton addNewOrganizationButton;

    String name, number, uid, added_by;

    DatabaseReference dbOrganization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_organization);

        initialization();
        setListener();

    }

    private void initialization() {
        organizationNameEditText = findViewById(R.id.organizationNameEditText);
        organizationPhoneNumberEditText = findViewById(R.id.organizationPhoneNumberEditText);
        addNewOrganizationButton = findViewById(R.id.addNewOrganizationButton);

        dbOrganization = FirebaseDatabase.getInstance().getReference().child("Organization");
    }

    private void setListener() {
        addNewOrganizationButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.addNewOrganizationButton:
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        name = organizationNameEditText.getText().toString().trim();
        number = organizationPhoneNumberEditText.getText().toString().trim();

        if (name.isEmpty()){
            showToast("Enter Name");
            return;
        }

        if (number.isEmpty()){
            showToast("Enter Number");
            return;
        }

        else {
            addCOrganization();
        }
    }

    private void addCOrganization() {
        uid = dbOrganization.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        added_by = user.getUid();

        System.out.println("KEy is : " + uid);

        OrganizationModel organizationModel = new OrganizationModel(name, number, uid, added_by,"");

        dbOrganization.child(uid).setValue(organizationModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                organizationNameEditText.setText("");
                organizationPhoneNumberEditText.setText("");
                showToast("Added");

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
        Animatoo.animateSwipeRight(AddOrganizationActivity.this);
    }

}