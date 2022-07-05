package com.rony.blooddonationapp.Fragment;

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

import com.rony.blooddonationapp.Adapter.UserAdapter;
import com.rony.blooddonationapp.Model.User;
import com.rony.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CompatibleWithMeFragment extends Fragment {

    View view;

    TextView noDataFoundTextView;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter adapter;
    DatabaseReference dbUser;

    String my_blood_group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_compatible_with_me, container, false);

        noDataFoundTextView = view.findViewById(R.id.noDataFoundTextView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        adapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        dbUser = FirebaseDatabase.getInstance().getReference().child("User");

        Query query = dbUser.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    User user = dataSnapshot.getValue(User.class);

                    if (user.getId().equals(FirebaseAuth.getInstance().getUid())){
                        my_blood_group = user.getBlood_group();

                        if (user.getBlood_group().equals(my_blood_group)){

                            userList.add(user);
                        }


                        Toast.makeText(getContext(), "my group is " + user.getBlood_group(), Toast.LENGTH_SHORT).show();
                    }

                    /*User user = dataSnapshot.getValue(User.class);
                    my_blood_group = user.getBlood_group();

                    Toast.makeText(getContext(), user.getBlood_group(), Toast.LENGTH_SHORT).show();
                    if (user.getId() .equals(FirebaseAuth.getInstance().getUid())){

                    }
                    if (!user.getId().equals(FirebaseAuth.getInstance().getUid()) && user.getBlood_group().equals(my_blood_group)){
                        userList.add(user);
                        progressBar.setVisibility(View.INVISIBLE);
                    }*/
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noDataFoundTextView.setVisibility(View.VISIBLE);
                    noDataFoundTextView.setText("No Donor Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}