package com.example.blooddonationapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.blooddonationapp.Activity.AddNewCoordinatorActivity;
import com.example.blooddonationapp.Activity.CoordinatorActivity;
import com.example.blooddonationapp.Adapter.CoordinatorAdapter;
import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.Model.OrganizationModel;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CoordinatorFragment extends Fragment {

    View view;

    EditText searchCoordinatorEditText;
    TextView noCoordinatorFoundTextView;
    ProgressBar progressBar;

    FloatingActionButton fabAddCoordinator;

    RecyclerView recyclerView;
    CoordinatorAdapter adapter;
    List<CoordinatorModel> coordinatorModelList;

    DatabaseReference dbCoordinator;

    FirebaseUser user;
    String added_by;

    DatabaseReference dbRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_coordinator, container, false);

        searchCoordinatorEditText = view.findViewById(R.id.searchCoordinatorEditText);
        recyclerView = view.findViewById(R.id.recyclerView);
        noCoordinatorFoundTextView = view.findViewById(R.id.noCoordinatorFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);
        fabAddCoordinator = view.findViewById(R.id.fabAddCoordinator);

        fabAddCoordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddNewCoordinatorActivity.class));
            }
        });

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

        user = FirebaseAuth.getInstance().getCurrentUser();

        added_by = user.getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        coordinatorModelList = new ArrayList<>();
        adapter = new CoordinatorAdapter(getContext(), coordinatorModelList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        dbCoordinator = FirebaseDatabase.getInstance().getReference().child("Coordinator");

        Query query = dbCoordinator.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                coordinatorModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CoordinatorModel coordinatorModel = dataSnapshot.getValue(CoordinatorModel.class);
                    coordinatorModelList.add(coordinatorModel);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                adapter.notifyDataSetChanged();
                if (coordinatorModelList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noCoordinatorFoundTextView.setVisibility(View.VISIBLE);
                    noCoordinatorFoundTextView.setText("No Coordinator Found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

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
}