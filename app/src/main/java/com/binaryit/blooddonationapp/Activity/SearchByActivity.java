package com.binaryit.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.R;

public class SearchByActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton searchByLocationButton, searchByOrganizationButton;

    String blood_group;

    AppCompatImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by);

        initialization();
        setListener();

        blood_group = getIntent().getStringExtra("group");
        System.out.println("Group Is " + blood_group);
    }

    private void initialization() {
        imageBack = findViewById(R.id.imageBack);

        searchByLocationButton = findViewById(R.id.searchByLocationButton);
        searchByOrganizationButton = findViewById(R.id.searchByOrganizationButton);
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        searchByLocationButton.setOnClickListener(this);
        searchByOrganizationButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchByLocationButton:
                Intent intent = new Intent(SearchByActivity.this, SearchLocationActivity.class);
                intent.putExtra("group", blood_group);
                startActivity(intent);
                Animatoo.animateSwipeLeft(SearchByActivity.this);
                finish();
                break;

            case R.id.searchByOrganizationButton:
                startActivity(new Intent(SearchByActivity.this, OrganizationNameListActivity.class));
                Animatoo.animateSwipeLeft(SearchByActivity.this);
                finish();
                break;

            case R.id.imageBack:
                onBackPressed();
                Animatoo.animateSwipeRight(SearchByActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(SearchByActivity.this);
    }
}