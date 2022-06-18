package com.example.blooddonationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.AcceptRequestModel;
import com.example.blooddonationapp.R;

import java.util.List;

public class AcceptRequestAdapter extends RecyclerView.Adapter<AcceptRequestAdapter.AcceptRequestViewHolder> {

    Context context;
    List<AcceptRequestModel> acceptRequestModelList;

    public AcceptRequestAdapter(Context context, List<AcceptRequestModel> acceptRequestModelList) {
        this.context = context;
        this.acceptRequestModelList = acceptRequestModelList;
    }

    @NonNull
    @Override
    public AcceptRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.accept_request_layout, parent, false);
        return new AcceptRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptRequestViewHolder holder, int position) {
        AcceptRequestModel data = acceptRequestModelList.get(position);
        holder.messageTextView.setText(data.getMessage());
        holder.nameTextView.setText(data.getName());
        holder.bloodGroupTextView.setText(data.getBlood_group());
    }

    @Override
    public int getItemCount() {
        return acceptRequestModelList.size();
    }

    public class AcceptRequestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, bloodGroupTextView, messageTextView;

        public AcceptRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);

        }
    }

}
