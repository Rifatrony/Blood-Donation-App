package com.rony.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rony.blooddonationapp.Model.User;
import com.rony.blooddonationapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdminUserDetailsAdapter extends RecyclerView.Adapter<AdminUserDetailsAdapter.AdminUserDetailsViewHolder> {

    Context context;
    List<User> userList;

    public AdminUserDetailsAdapter() {
    }

    public AdminUserDetailsAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdminUserDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_user_profile_layout, parent, false);
        return new AdminUserDetailsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminUserDetailsViewHolder holder, int position) {
        User data = userList.get(position);
        holder.userNameTextView.setText(data.getName() + " ( " + data.getOrganization()+" )");
        holder.userNumberTextView.setText("Number :   "+data.getNumber());
        holder.userBloodGroupTextView.setText("Blood Group :   "+data.getBlood_group());
        holder.userAddressTextView.setText("Address :   "+data.getAddress());
        holder.userOrganizationTextView.setText("Organization :   "+data.getOrganization());
        holder.userDobTextView.setText("DOB :   "+data.getDob());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void filterList(ArrayList<User> list) {
        userList = list;
        notifyDataSetChanged();
    }


    public class AdminUserDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, userNumberTextView,
                userBloodGroupTextView, userAddressTextView, userOrganizationTextView, userDobTextView;

        public AdminUserDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            userAddressTextView = itemView.findViewById(R.id.userAddressTextView);
            userOrganizationTextView = itemView.findViewById(R.id.userOrganizationTextView);
            userDobTextView = itemView.findViewById(R.id.userDobTextView);

        }
    }
}
