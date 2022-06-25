package com.example.blooddonationapp.Fragment;

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

import com.example.blooddonationapp.Adapter.TodayReadyAdapter;
import com.example.blooddonationapp.Model.TodayReadyModel;
import com.example.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodayReadyFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    List<TodayReadyModel> todayReadyModelList;
    TodayReadyAdapter adapter;

    DatabaseReference dbTodayDonor;

    TextView noRequestFoundTextView;
    ProgressBar progressBar;

    String today_date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_today_ready, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noRequestFoundTextView = view.findViewById(R.id.noRequestFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        today_date = sdf.format(c.getTime());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todayReadyModelList = new ArrayList<>();
        adapter = new TodayReadyAdapter(getContext(), todayReadyModelList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        dbTodayDonor = FirebaseDatabase.getInstance().getReference().child("Today Ready");

        dbTodayDonor.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TodayReadyModel todayReadyModel = dataSnapshot.getValue(TodayReadyModel.class);
                    if (!todayReadyModel.getId().equals(FirebaseAuth.getInstance().getUid()) && todayReadyModel.getDate().equals(today_date)){
                        progressBar.setVisibility(View.INVISIBLE);
                        todayReadyModelList.add(todayReadyModel);
                    }
                }

                adapter.notifyDataSetChanged();

                if (todayReadyModelList.isEmpty()){

                    progressBar.setVisibility(View.INVISIBLE);
                    noRequestFoundTextView.setVisibility(View.VISIBLE);
                    noRequestFoundTextView.setText("No one is Available Today");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}