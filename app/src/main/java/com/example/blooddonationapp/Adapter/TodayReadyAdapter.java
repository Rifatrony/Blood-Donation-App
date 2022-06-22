package com.example.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.TodayReadyModel;
import com.example.blooddonationapp.R;

import java.util.List;

public class TodayReadyAdapter extends RecyclerView.Adapter<TodayReadyAdapter.TodayReadyViewHolder> {

    Context context;
    List<TodayReadyModel> todayReadyModelList;

    public TodayReadyAdapter() {
    }

    public TodayReadyAdapter(Context context, List<TodayReadyModel> todayReadyModelList) {
        this.context = context;
        this.todayReadyModelList = todayReadyModelList;
    }

    @NonNull
    @Override
    public TodayReadyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_layout, parent, false);
        return new TodayReadyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayReadyViewHolder holder, int position) {
        TodayReadyModel data = todayReadyModelList.get(position);
        holder.userNameTextView.setText(data.getName());
        holder.userNumberTextView.setText(data.getNumber());
        holder.userBloodGroupTextView.setText(data.getBlood_group());
        holder.userAddressTextView.setText(data.getAddress());

    }

    @Override
    public int getItemCount() {
        return todayReadyModelList.size();
    }

    public class TodayReadyViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, userNumberTextView, userBloodGroupTextView, userAddressTextView;

        public TodayReadyViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            userAddressTextView = itemView.findViewById(R.id.userAddressTextView);


        }
    }

}
