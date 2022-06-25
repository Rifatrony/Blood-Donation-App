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
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.RequestAdapter;
import com.example.blooddonationapp.Model.RequestModel;
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


public class BloodRequestFragment extends Fragment {

    View view;

    TextView noRequestFoundTextView;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    List<RequestModel> userList;
    RequestAdapter adapter;

    DatabaseReference userRef;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blood_request, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noRequestFoundTextView = view.findViewById(R.id.noRequestFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        adapter = new RequestAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference().child("Request").child(firebaseUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RequestModel user = dataSnapshot.getValue(RequestModel.class);

                    if (user.getUid().equals(firebaseUser.getUid())){
                        progressBar.setVisibility(View.INVISIBLE);
                        noRequestFoundTextView.setVisibility(View.INVISIBLE);
                        userList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){

                    progressBar.setVisibility(View.INVISIBLE);
                    noRequestFoundTextView.setVisibility(View.VISIBLE);
                    noRequestFoundTextView.setText("No Request Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}