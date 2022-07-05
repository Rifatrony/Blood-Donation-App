package com.rony.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rony.blooddonationapp.MainActivity;
import com.rony.blooddonationapp.R;

public class SelectBloodGroupActivity extends AppCompatActivity implements View.OnClickListener {

    TextView A_Positive, B_Positive, AB_Positive, O_Positive,
            A_Negative, B_Negative, AB_Negative, O_Negative;

    AppCompatImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_blood_group);

        initialization();
        setListener();

    }

    private void initialization() {

        imageBack = findViewById(R.id.imageBack);

        A_Positive = findViewById(R.id.A_Positive);
        B_Positive = findViewById(R.id.B_Positive);
        AB_Positive = findViewById(R.id.AB_Positive);
        O_Positive = findViewById(R.id.O_Positive);

        A_Negative = findViewById(R.id.A_Negative);
        B_Negative = findViewById(R.id.B_Negative);
        AB_Negative = findViewById(R.id.AB_Negative);
        O_Negative = findViewById(R.id.O_Negative);
    }

    private void setListener() {

        imageBack.setOnClickListener(this);

        A_Positive.setOnClickListener(this);
        B_Positive.setOnClickListener(this);
        AB_Positive.setOnClickListener(this);
        O_Positive.setOnClickListener(this);

        A_Negative.setOnClickListener(this);
        B_Negative.setOnClickListener(this);
        AB_Negative.setOnClickListener(this);
        O_Negative.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.A_Positive:

                Intent intent1 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent1.putExtra("group", "A+");
                startActivity(intent1);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;

            case R.id.B_Positive:
                Intent intent2 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent2.putExtra("group", "B+");
                startActivity(intent2);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;

            case R.id.AB_Positive:
                Intent intent3 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent3.putExtra("group", "AB+");
                startActivity(intent3);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;

            case R.id.O_Positive:
                Intent intent4 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent4.putExtra("group", "O+");
                startActivity(intent4);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;

            case R.id.A_Negative:
                Intent intent5 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent5.putExtra("group", "A-");
                startActivity(intent5);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;

            case R.id.B_Negative:
                Intent intent6 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent6.putExtra("group", "B-");
                startActivity(intent6);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;

            case R.id.AB_Negative:
                Intent intent7 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent7.putExtra("group", "AB-");
                startActivity(intent7);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);
                break;

            case R.id.O_Negative:
                Intent intent8 = new Intent(getApplicationContext(), SearchByActivity.class);
                intent8.putExtra("group", "O-");
                startActivity(intent8);
                Animatoo.animateSwipeLeft(SelectBloodGroupActivity.this);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(SelectBloodGroupActivity.this);
    }
}