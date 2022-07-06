package com.binaryit.blooddonationapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Activity.OrganizationWiseMemberActivity;
import com.binaryit.blooddonationapp.Model.OrganizationModel;
import com.binaryit.blooddonationapp.R;

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
        OrganizationModel data = organizationModelList.get(position);
        holder.nameTextView.setText(data.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrganizationWiseMemberActivity.class);
                intent.putExtra("title", data.getName());
                context.startActivity(intent);
                Animatoo.animateSwipeLeft(context);

                Toast.makeText(context, "ID id " + data.getName(), Toast.LENGTH_SHORT).show();
            }
        });

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
