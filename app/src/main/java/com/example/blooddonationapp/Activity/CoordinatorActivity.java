package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Adapter.CoordinatorAdapter;
import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fabAddCoordinator;
    AppCompatImageView imageBack;

    EditText searchCoordinatorEditText;

    RecyclerView recyclerView;
    CoordinatorAdapter adapter;
    List<CoordinatorModel> coordinatorModelList;

    DatabaseReference dbCoordinator;

    FirebaseUser user;
    String added_by;

    DatabaseReference dbRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        initialization();
        setListener();

        dbRole = FirebaseDatabase.getInstance().getReference().child("User");

        dbRole.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        if (user.getRole().equals("user")){
                            fabAddCoordinator.setVisibility(View.INVISIBLE);
                        } else {
                            fabAddCoordinator.setVisibility(View.VISIBLE);
                        }
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        added_by = user.getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        coordinatorModelList = new ArrayList<>();
        adapter = new CoordinatorAdapter(this, coordinatorModelList);
        recyclerView.setAdapter(adapter);

        dbCoordinator = FirebaseDatabase.getInstance().getReference().child("Coordinator");
        dbCoordinator.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                coordinatorModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    CoordinatorModel coordinatorModel = dataSnapshot.getValue(CoordinatorModel.class);
                    coordinatorModelList.add(coordinatorModel);

                }

                adapter.notifyDataSetChanged();
                if (coordinatorModelList.isEmpty()){
                    Toast.makeText(CoordinatorActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchCoordinatorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


    }

    private void filter(String text) {
        ArrayList<CoordinatorModel> filteredList = new ArrayList<>();

        for (CoordinatorModel item : coordinatorModelList) {
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


    private void initialization() {
        searchCoordinatorEditText = findViewById(R.id.searchCoordinatorEditText);
        recyclerView = findViewById(R.id.recyclerView);
        fabAddCoordinator = findViewById(R.id.fabAddCoordinator);
        imageBack = findViewById(R.id.imageBack);
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        fabAddCoordinator.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.fabAddCoordinator:
                startActivity(new Intent(getApplicationContext(), AddNewCoordinatorActivity.class));
                Animatoo.animateSwipeLeft(CoordinatorActivity.this);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(CoordinatorActivity.this);
    }
}