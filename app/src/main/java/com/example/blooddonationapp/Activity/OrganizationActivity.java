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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Adapter.OrganizationAdapter;
import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.Model.OrganizationModel;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrganizationActivity extends AppCompatActivity {

    EditText searchOrganizationEditText;

    AppCompatImageView imageBack;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView noOrganizationTextView;
    OrganizationAdapter adapter;
    List<OrganizationModel> organizationModelList;

    FloatingActionButton fabAddOrganization;

    DatabaseReference dbRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        initialization();

        dbRole = FirebaseDatabase.getInstance().getReference().child("User");
        dbRole.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        if (user.getRole().equals("user")){
                            fabAddOrganization.setVisibility(View.INVISIBLE);
                        } else {
                            fabAddOrganization.setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(OrganizationActivity.this, user.getRole(), Toast.LENGTH_SHORT).show();
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageBack.setOnClickListener(view -> onBackPressed());

        fabAddOrganization.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), AddOrganizationActivity.class));
            Animatoo.animateSwipeRight(OrganizationActivity.this);
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        organizationModelList = new ArrayList<>();
        adapter= new OrganizationAdapter(this, organizationModelList);
        recyclerView.setAdapter(adapter);

        DatabaseReference dbOrganization = FirebaseDatabase.getInstance().getReference()
                .child("Organization");

        dbOrganization.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                organizationModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OrganizationModel organizationModel = dataSnapshot.getValue(OrganizationModel.class);
                    organizationModelList.add(organizationModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchOrganizationEditText.addTextChangedListener(new TextWatcher() {
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
        ArrayList<OrganizationModel> filteredList = new ArrayList<>();

        for (OrganizationModel item : organizationModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void initialization() {

        imageBack = findViewById(R.id.imageBack);
        searchOrganizationEditText = findViewById(R.id.searchOrganizationEditText);
        fabAddOrganization = findViewById(R.id.fabAddOrganization);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        noOrganizationTextView = findViewById(R.id.noOrganizationTextView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(OrganizationActivity.this);
    }

}