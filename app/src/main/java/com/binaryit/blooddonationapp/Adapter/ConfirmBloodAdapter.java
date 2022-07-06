package com.binaryit.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryit.blooddonationapp.Model.ConfirmBloodModel;
import com.binaryit.blooddonationapp.R;

import java.util.List;

public class ConfirmBloodAdapter extends RecyclerView.Adapter<ConfirmBloodAdapter.ConfirmBloodViewHolder> {

    Context context;
    List<ConfirmBloodModel> confirmBloodModelList;

    public ConfirmBloodAdapter() {
    }

    public ConfirmBloodAdapter(Context context, List<ConfirmBloodModel> confirmBloodModelList) {
        this.context = context;
        this.confirmBloodModelList = confirmBloodModelList;
    }

    @NonNull
    @Override
    public ConfirmBloodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_blood_layout, parent, false);
        return new ConfirmBloodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ConfirmBloodViewHolder holder, int position) {
        ConfirmBloodModel data = confirmBloodModelList.get(position);
        holder.bloodGroupTextView.setText("I donate 1 bag "+data.getBlood_group());
        holder.locationTextView.setText("Donate Location was "+data.getDonate_location() + " and donate date "+data.getDonate_date());
        holder.patientProblemTextView.setText("The patient problem was "+data.getPatient_problem());
    }

    @Override
    public int getItemCount() {
        return confirmBloodModelList.size();
    }

    public class ConfirmBloodViewHolder extends RecyclerView.ViewHolder {

        TextView bloodGroupTextView, locationTextView, patientProblemTextView;

        public ConfirmBloodViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            patientProblemTextView = itemView.findViewById(R.id.patientProblemTextView);

        }
    }


}
