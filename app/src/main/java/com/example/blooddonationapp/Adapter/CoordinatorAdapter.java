package com.example.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.R;

import java.util.List;

public class CoordinatorAdapter extends RecyclerView.Adapter<CoordinatorAdapter.CoordinatorViewHolder> {

    Context context;
    List<CoordinatorModel> coordinatorModelList;

    public CoordinatorAdapter() {
    }

    public CoordinatorAdapter(Context context, List<CoordinatorModel> coordinatorModelList) {
        this.context = context;
        this.coordinatorModelList = coordinatorModelList;
    }

    @NonNull
    @Override
    public CoordinatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.coordiantor_details_layout, parent, false);

        return new CoordinatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoordinatorViewHolder holder, int position) {
        CoordinatorModel data = coordinatorModelList.get(position);
        holder.nameTextView.setText(data.getName());
        holder.numberTextView.setText(data.getNumber());
    }

    @Override
    public int getItemCount() {
        return coordinatorModelList.size();
    }

    public class CoordinatorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, numberTextView;
        AppCompatImageView callCoordinatorButton;

        public CoordinatorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            callCoordinatorButton = itemView.findViewById(R.id.callCoordinatorButton);

        }
    }

}
