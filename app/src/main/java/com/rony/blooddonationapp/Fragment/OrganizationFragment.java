package com.rony.blooddonationapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rony.blooddonationapp.Activity.AddOrganizationActivity;
import com.rony.blooddonationapp.Activity.OrganizationActivity;
import com.rony.blooddonationapp.Adapter.OrganizationAdapter;
import com.rony.blooddonationapp.Model.OrganizationModel;
import com.rony.blooddonationapp.Model.User;
import com.rony.blooddonationapp.R;
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

    EditText searchOrganizationEditText;

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
        searchOrganizationEditText = view.findViewById(R.id.searchOrganizationEditText);
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

                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabAddOrganization.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), AddOrganizationActivity.class));
            Animatoo.animateSwipeLeft(getContext());
        });

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

        return view;
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
}