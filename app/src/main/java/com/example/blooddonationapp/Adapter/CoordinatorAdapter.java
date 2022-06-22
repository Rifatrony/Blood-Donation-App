package com.example.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CoordinatorViewHolder holder, int position) {
        CoordinatorModel data = coordinatorModelList.get(position);
        holder.nameTextView.setText(data.getName());
        holder.numberTextView.setText(data.getNumber());
        holder.totalMemberTextView.setText(data.getAddress());

        holder.callCoordinatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setData(Uri.parse("tel:+8801772333793"));

                String num = "tel:"+data.getNumber();
                intent.setData(Uri.parse(num));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coordinatorModelList.size();
    }

    public class CoordinatorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, numberTextView, totalMemberTextView;
        AppCompatImageView callCoordinatorButton;

        public CoordinatorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            totalMemberTextView = itemView.findViewById(R.id.totalMemberTextView);
            callCoordinatorButton = itemView.findViewById(R.id.callCoordinatorButton);

        }
    }

}