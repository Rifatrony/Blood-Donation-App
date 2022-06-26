package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blooddonationapp.Adapter.OrganizationAdapter;
import com.example.blooddonationapp.Adapter.OrganizationNameAdapter;
import com.example.blooddonationapp.Model.OrganizationModel;
import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrganizationNameListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noOrganizationFoundTextView;
    ProgressBar progressBar;

    AppCompatImageView imageBack;

    OrganizationNameAdapter adapter;
    List<OrganizationModel> organizationModelList;

    DatabaseReference dbOrganization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_name_list);

        initialization();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        organizationModelList = new ArrayList<>();
        adapter= new OrganizationNameAdapter(this, organizationModelList);
        recyclerView.setAdapter(adapter);

        dbOrganization = FirebaseDatabase.getInstance().getReference()
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

    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerView);
        noOrganizationFoundTextView = findViewById(R.id.noOrganizationFoundTextView);
        progressBar = findViewById(R.id.progressBar);
    }
}