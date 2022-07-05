package com.rony.blooddonationapp.Fragment;

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

import com.rony.blooddonationapp.Adapter.ViewSentRequestAdapter;
import com.rony.blooddonationapp.Model.RequestIdModel;
import com.rony.blooddonationapp.Model.RequestModel;
import com.rony.blooddonationapp.R;
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

        dbRequestId = FirebaseDatabase.getInstance().getReference().child("RequestId");

        dbRequestId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                requestModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    RequestIdModel data = dataSnapshot.getValue(RequestIdModel.class);
                    requestSentUid = data.getUid().toString();

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
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                if (requestModelList.isEmpty()){

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