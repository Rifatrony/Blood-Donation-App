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

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.DonorListViewHolder> {

    Context context;
    ArrayList<User> list;

    public DonorListAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DonorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donor_list_layout, parent, false);
        return new DonorListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorListViewHolder holder, int position) {
        User data = list.get(position);
        holder.donorNameTextView.setText(data.getName());
        holder.donorBloodGroupTextView.setText(data.getBlood_group());
        holder.donorAddressTextView.setText(data.getAddress());
        holder.donorPhoneNumberTextView.setText(data.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DonorListViewHolder extends RecyclerView.ViewHolder {

        TextView donorNameTextView, donorBloodGroupTextView, donorAddressTextView, donorPhoneNumberTextView;

        public DonorListViewHolder(@NonNull View itemView) {
            super(itemView);

            donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
            donorBloodGroupTextView = itemView.findViewById(R.id.donorBloodGroupTextView);
            donorAddressTextView = itemView.findViewById(R.id.donorAddressTextView);
            donorPhoneNumberTextView = itemView.findViewById(R.id.donorPhoneNumberTextView);

        }
    }

}
