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

import com.binaryit.blooddonationapp.Adapter.RecipientRecordAdapter;
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


public class TakeBloodRecordFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    List<ConfirmBloodModel> confirmBloodModelList;
    RecipientRecordAdapter adapter;

    DatabaseReference dbConfirmBlood;

    TextView noDataFoundTextView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_take_blood_record, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noDataFoundTextView = view.findViewById(R.id.noDataFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        confirmBloodModelList = new ArrayList<>();
        adapter = new RecipientRecordAdapter(getContext(), confirmBloodModelList);
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
                    assert confirmBloodModel != null;
                    if (confirmBloodModel.getMy_uid().equals(FirebaseAuth.getInstance().getUid())){

                        confirmBloodModelList.add(confirmBloodModel);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();

                if (confirmBloodModelList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noDataFoundTextView.setVisibility(View.VISIBLE);
                    noDataFoundTextView.setText("You Never Take Blood");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}