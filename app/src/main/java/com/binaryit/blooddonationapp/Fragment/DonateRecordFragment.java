package com.binaryit.blooddonationapp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.binaryit.blooddonationapp.Adapter.ConfirmBloodAdapter;
import com.binaryit.blooddonationapp.Model.ConfirmBloodModel;
import com.binaryit.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonateRecordFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    List<ConfirmBloodModel> confirmBloodModelList;
    ConfirmBloodAdapter adapter;

    DatabaseReference dbConfirmBlood;
    DatabaseReference dbRecord;

    TextView noDataFoundTextView;
    ProgressBar progressBar;

    String id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_donate_record, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noDataFoundTextView = view.findViewById(R.id.noDataFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        confirmBloodModelList = new ArrayList<>();
        adapter = new ConfirmBloodAdapter(getContext(), confirmBloodModelList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        dbConfirmBlood = FirebaseDatabase.getInstance().getReference().child("Confirm Blood");

        dbConfirmBlood.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //confirmBloodModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ConfirmBloodModel confirmBloodModel = dataSnapshot.getValue(ConfirmBloodModel.class);

                    if (confirmBloodModel.getAccepted_id().equals(FirebaseAuth.getInstance().getUid())){
                        confirmBloodModelList.add(confirmBloodModel);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
                adapter.notifyDataSetChanged();

                if (confirmBloodModelList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noDataFoundTextView.setVisibility(View.VISIBLE);
                    noDataFoundTextView.setText("You Never Donate Blood");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}