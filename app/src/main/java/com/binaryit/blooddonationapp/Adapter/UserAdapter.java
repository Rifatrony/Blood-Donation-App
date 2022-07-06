package com.binaryit.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryit.blooddonationapp.Model.User;
import com.binaryit.blooddonationapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTextView.setText(user.getName());
        holder.userNumberTextView.setText(user.getEmail());
        holder.userBloodGroupTextView.setText(user.getBlood_group());
        holder.userAddressTextView.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void filterList(ArrayList<User> filteredList) {
        userList = filteredList;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, userNumberTextView,
                userBloodGroupTextView, userAddressTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            userAddressTextView = itemView.findViewById(R.id.userAddressTextView);

        }
    }

}