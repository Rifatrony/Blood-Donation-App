package com.example.blooddonationapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Activity.AddOrganizationActivity;
import com.example.blooddonationapp.Activity.OrganizationActivity;
import com.example.blooddonationapp.Adapter.OrganizationAdapter;
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


public class OrganizationFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView noOrganizationTextView;
    OrganizationAdapter adapter;
    List<OrganizationModel> organizationModelList;

    FloatingActionButton fabAddOrganization;

    DatabaseReference dbRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_organization, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddOrganization = view.findViewById(R.id.fabAddOrganization);
        progressBar = view.findViewById(R.id.progressBar);
        noOrganizationTextView = view.findViewById(R.id.noOrganizationTextView);

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

                        Toast.makeText(getContext(), user.getRole(), Toast.LENGTH_SHORT).show();
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabAddOrganization.setOnClickListener(view -> startActivity(new Intent(getContext(), AddOrganizationActivity.class)));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        organizationModelList = new ArrayList<>();
        adapter= new OrganizationAdapter(getContext(), organizationModelList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

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
                    progressBar.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();

                if (organizationModelList.isEmpty()){
                    noOrganizationTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    noOrganizationTextView.setText("No Organization Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}