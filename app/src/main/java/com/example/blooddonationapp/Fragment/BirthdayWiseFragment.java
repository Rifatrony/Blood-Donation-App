package com.example.blooddonationapp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.AdminUserDetailsAdapter;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BirthdayWiseFragment extends Fragment {

    View view;

    EditText searchMemberEditText;
    ProgressBar progressBar;
    TextView nothingFoundTextView;
    RecyclerView recyclerView;
    DatabaseReference dbUser;
    Query query;
    List<User> userList;
    AdminUserDetailsAdapter adapter;

    String today_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_birthday_wise, container, false);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        today_date = sdf.format(c.getTime());

        Date date = null;

        try {
            date = sdf.parse(today_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        //String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        //String year         = (String) DateFormat.format("yyyy", date);
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthNumber  = (String) DateFormat.format("MM",   date); // 06

        System.out.println("Date is " + day);
        System.out.println("Month is " + monthNumber);

        nothingFoundTextView = view.findViewById(R.id.noTextView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchMemberEditText = view.findViewById(R.id.searchMemberEditText);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        adapter = new AdminUserDetailsAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        dbUser = FirebaseDatabase.getInstance().getReference().child("User");
        query = dbUser.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    Date date1 = null;

                    try {
                        date1 = sdf.parse(user.getDob());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String day1         = (String) DateFormat.format("dd",   date1); // 20
                    String monthNumber1 = (String) DateFormat.format("MM",   date1); // 06

                    if (day.equals(day1)  && monthNumber.equals(monthNumber1)){
                        userList.add(user);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Today is no one Birthday", Toast.LENGTH_SHORT).show();
                    //nothingFoundTextView.setVisibility(View.VISIBLE);
                    //nothingFoundTextView.setText("Today is no one Birthday");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}