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

public class RecipientRecordAdapter extends RecyclerView.Adapter<RecipientRecordAdapter.RecipientRecordViewHolder> {

    Context context;
    List<ConfirmBloodModel> confirmBloodModelList;

    public RecipientRecordAdapter() {
    }

    public RecipientRecordAdapter(Context context, List<ConfirmBloodModel> confirmBloodModelList) {
        this.context = context;
        this.confirmBloodModelList = confirmBloodModelList;
    }

    @NonNull
    @Override
    public RecipientRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_blood_layout, parent, false);
        return new RecipientRecordViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipientRecordViewHolder holder, int position) {
        ConfirmBloodModel data = confirmBloodModelList.get(position);
        holder.bloodGroupTextView.setText("I received 1 bag "+data.getBlood_group() +"\nFrom "+ data.getName() + " ( " + data.getNumber()+" )");
        holder.locationTextView.setText("Received Location was "+data.getDonate_location() + " and received date "+data.getDonate_date());
        holder.patientProblemTextView.setText("My patient has "+data.getPatient_problem() + " problem");
    }

    @Override
    public int getItemCount() {
        return confirmBloodModelList.size();
    }

    public class RecipientRecordViewHolder extends RecyclerView.ViewHolder {

        TextView bloodGroupTextView, locationTextView, patientProblemTextView;

        public RecipientRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            patientProblemTextView = itemView.findViewById(R.id.patientProblemTextView);

        }
    }

}
