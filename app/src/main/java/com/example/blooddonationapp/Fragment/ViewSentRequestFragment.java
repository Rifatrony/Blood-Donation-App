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
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.ViewSentRequestAdapter;
import com.example.blooddonationapp.Model.RequestIdModel;
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


public class ViewSentRequestFragment extends Fragment {

    View view;

    TextView viewYourRequestTextView;
    ProgressBar progressBar;
    TextView noRequestFoundTextView;

    AppCompatImageView imageBack;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<RequestModel> requestModelList;
    ViewSentRequestAdapter adapter;

    FirebaseUser firebaseUser;
    DatabaseReference dbViewRequest, dbRequestId;

    String requestSentUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_sent_request, container, false);

        imageBack = view.findViewById(R.id.imageBack);
        noRequestFoundTextView = view.findViewById(R.id.noRequestFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        requestModelList = new ArrayList<>();
        adapter = new ViewSentRequestAdapter(getContext(), requestModelList);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        dbRequestId = FirebaseDatabase.getInstance().getReference().child("RequestId");

        dbRequestId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                requestModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    RequestIdModel data = dataSnapshot.getValue(RequestIdModel.class);
                    requestSentUid = data.getUid();

                    if (!data.getUid().isEmpty()){
                        dbViewRequest = FirebaseDatabase.getInstance().getReference()
                                .child("Request").child(data.getUid());

                        dbViewRequest.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                requestModelList.clear();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    RequestModel user = dataSnapshot.getValue(RequestModel.class);
                                    if (!user.getUid().equals(mAuth.getUid())){
                                        requestModelList.add(user);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        noRequestFoundTextView.setVisibility(View.INVISIBLE);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                                if (requestModelList.isEmpty()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    noRequestFoundTextView.setVisibility(View.VISIBLE);
                                    noRequestFoundTextView.setText("No Sent Request Found");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}