package com.example.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.OrganizationModel;
import com.example.blooddonationapp.R;

import java.util.List;

public class OrganizationNameAdapter extends RecyclerView.Adapter<OrganizationNameAdapter.OrganizationNameViewHolder> {

    Context context;
    List<OrganizationModel> organizationModelList;

    public OrganizationNameAdapter() {
    }

    public OrganizationNameAdapter(Context context, List<OrganizationModel> organizationModelList) {
        this.context = context;
        this.organizationModelList = organizationModelList;
    }

    @NonNull
    @Override
    public OrganizationNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.organization_name_layout, parent, false);
        return new OrganizationNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationNameViewHolder holder, int position) {
        holder.nameTextView.setText(organizationModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return organizationModelList.size();
    }


    public class OrganizationNameViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public OrganizationNameViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);

        }
    }
}
