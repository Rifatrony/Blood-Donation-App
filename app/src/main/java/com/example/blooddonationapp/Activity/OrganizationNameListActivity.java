package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Adapter.OrganizationAdapter;
import com.example.blooddonationapp.Adapter.OrganizationNameAdapter;
import com.example.blooddonationapp.Model.OrganizationModel;
import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    //String blood_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_name_list);

        initialization();

        /*blood_group = getIntent().getStringExtra("group");
        System.out.println("Blood group receive " + blood_group);*/

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        organizationModelList = new ArrayList<>();
        adapter= new OrganizationNameAdapter(this, organizationModelList);
        recyclerView.setAdapter(adapter);

        dbOrganization = FirebaseDatabase.getInstance().getReference()
                .child("Organization");

        Query query = dbOrganization.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
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
        imageBack = findViewById(R.id.imageBack);
        recyclerView = findViewById(R.id.recyclerView);
        noOrganizationFoundTextView = findViewById(R.id.noOrganizationFoundTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(OrganizationNameListActivity.this);
    }
}