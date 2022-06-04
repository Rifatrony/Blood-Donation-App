package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Activity.ProfileActivity;
import com.example.blooddonationapp.Utilities.SessionManagement;
import com.example.blooddonationapp.Utilities.SessionModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView profile;

    private long backPressedTime;
    private Toast backToast;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    SessionManagement sessionManagement;

    String email, password;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile = findViewById(R.id.profile);
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        mAuth = FirebaseAuth.getInstance();

        /*email = getIntent().getExtras().getString("email");
        password = getIntent().getStringExtra("password");*/



        sessionManagement = new SessionManagement(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (id){

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;

                    case R.id.nav_logout:
                        logOut();
                        break;

                    case R.id.nav_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Check this application");
                        intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.whatsapp");
                        startActivity(Intent.createChooser(intent,"Share Via"));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void logOut(){

        /*SessionModel dataModel = new SessionModel(email, password);
        email = dataModel.getUserEmail();
        password = dataModel.getPassword();
        System.out.println("Email is: " + email  +"\n Password is : " + password);

        Toast.makeText(this, "Signout", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 >System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else {
            backToast = Toast.makeText(this, "Tap again to exit...", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

}