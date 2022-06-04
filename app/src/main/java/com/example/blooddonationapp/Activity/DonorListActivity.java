package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.blooddonationapp.Adapter.DonorListAdapter;
import com.example.blooddonationapp.Model.UserRegisterModel;
import com.example.blooddonationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonorListActivity extends AppCompatActivity {

    RecyclerView donorListRecyclerView;

    DatabaseReference dbDonorList;

    ArrayList<UserRegisterModel> userRegisterModelList;
    DonorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        initialization();

        dbDonorList = FirebaseDatabase.getInstance().getReference().child("Donor List").child("Donor Details");

        donorListRecyclerView.setHasFixedSize(true);
        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userRegisterModelList = new ArrayList<>();
        adapter = new DonorListAdapter(getApplicationContext(),userRegisterModelList);
        donorListRecyclerView.setAdapter(adapter);

        dbDonorList.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    UserRegisterModel sdh = data.getValue(UserRegisterModel.class);
                    userRegisterModelList.add(sdh);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialization() {
        donorListRecyclerView = findViewById(R.id.donorListRecyclerView);
    }
}