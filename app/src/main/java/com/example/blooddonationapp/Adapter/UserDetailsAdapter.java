package com.example.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.UserRegisterModel;
import com.example.blooddonationapp.R;

import java.util.List;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserDetailsViewHolder> {

    Context context;
    List<UserRegisterModel> userRegisterModelList;

    @NonNull
    @Override
    public UserDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_layout, parent, false);
        return new UserDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsViewHolder holder, int position) {
        UserRegisterModel data = userRegisterModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return userRegisterModelList.size();
    }

    public class UserDetailsViewHolder extends RecyclerView.ViewHolder {


        public UserDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
