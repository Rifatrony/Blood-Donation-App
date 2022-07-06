package com.binaryit.blooddonationapp.Fragment;

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

import com.binaryit.blooddonationapp.Adapter.RequestAdapter;
import com.binaryit.blooddonationapp.Model.RequestModel;
import com.binaryit.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRequestFragment extends Fragment {

    View view;
    TextView noDataFoundTextView;
    ProgressBar progressBar;

    AppCompatImageView imageBack;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<RequestModel> userList;
    RequestAdapter adapter;

    DatabaseReference userRef;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_request, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        progressBar = view.findViewById(R.id.progressBar);
        noDataFoundTextView = view.findViewById(R.id.noDataFoundTextView);

        imageBack = view.findViewById(R.id.imageBack);
        recyclerView = view.findViewById(R.id.recyclerView);
        //viewSentRequestTextView = view.findViewById(R.id.viewSentRequestTextView);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        adapter = new RequestAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        userRef = FirebaseDatabase.getInstance().getReference().child("Request").child(firebaseUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RequestModel user = dataSnapshot.getValue(RequestModel.class);

                    if (user.getUid().equals(firebaseUser.getUid())){
                        userList.add(user);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noDataFoundTextView.setVisibility(View.VISIBLE);
                    noDataFoundTextView.setText("No Request Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}