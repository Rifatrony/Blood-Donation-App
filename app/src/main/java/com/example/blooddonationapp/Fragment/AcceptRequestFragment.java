package com.example.blooddonationapp.Fragment;

import android.annotation.SuppressLint;
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

import com.example.blooddonationapp.Adapter.AcceptRequestAdapter;
import com.example.blooddonationapp.Model.AcceptRequestModel;
import com.example.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptRequestFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    AppCompatImageView imageBack;
    TextView noAcceptRequestTextView;
    ProgressBar progressBar;

    AcceptRequestAdapter adapter;
    List<AcceptRequestModel> acceptRequestModelList;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userRef;

    String user_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accept_request, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        imageBack = view.findViewById(R.id.imageBack);
        noAcceptRequestTextView = view.findViewById(R.id.noAcceptRequestTextView);
        progressBar = view.findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        user_id = user.getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        acceptRequestModelList = new ArrayList<>();
        adapter = new AcceptRequestAdapter(getContext(), acceptRequestModelList);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        userRef = FirebaseDatabase.getInstance().getReference().child("Accept Request").child(user_id);

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                acceptRequestModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AcceptRequestModel acceptRequestModel = dataSnapshot.getValue(AcceptRequestModel.class);

                    if (acceptRequestModel.getMy_uid().equals(user.getUid())){
                        acceptRequestModelList.add(acceptRequestModel);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
                adapter.notifyDataSetChanged();

                if (acceptRequestModelList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noAcceptRequestTextView.setVisibility(View.VISIBLE);
                    noAcceptRequestTextView.setText("No one Accept Your Request");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return view;
    }
}