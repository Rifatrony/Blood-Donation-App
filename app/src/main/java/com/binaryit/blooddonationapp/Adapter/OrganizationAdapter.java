package com.binaryit.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryit.blooddonationapp.Model.OrganizationModel;
import com.binaryit.blooddonationapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder> {

    Context context;
    List<OrganizationModel> organizationModelList;

    public OrganizationAdapter() {
    }

    public OrganizationAdapter(Context context, List<OrganizationModel> organizationModelList) {
        this.context = context;
        this.organizationModelList = organizationModelList;
    }

    @NonNull
    @Override
    public OrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.organization_layout, parent, false);
        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationViewHolder holder, int position) {
        OrganizationModel data = organizationModelList.get(position);

        holder.nameTextView.setText(data.getName());
        holder.numberTextView.setText(data.getNumber());
        holder.totalMemberTextView.setText("Total Member: "+data.getTotal_member());

    }

    @Override
    public int getItemCount() {
        return organizationModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<OrganizationModel> filteredList) {
        organizationModelList = filteredList;
        notifyDataSetChanged();
    }

    public class OrganizationViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, numberTextView, totalMemberTextView;

        public OrganizationViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            totalMemberTextView = itemView.findViewById(R.id.totalMemberTextView);

        }
    }

}
