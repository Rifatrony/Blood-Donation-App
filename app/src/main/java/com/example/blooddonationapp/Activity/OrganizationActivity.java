package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OrganizationActivity extends AppCompatActivity {


    FloatingActionButton fabAddOrganization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        fabAddOrganization = findViewById(R.id.fabAddOrganization);
        fabAddOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddOrganizationActivity.class));
            }
        });

    }
}