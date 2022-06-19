package com.example.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.RequestModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ViewSentRequestAdapter extends RecyclerView.Adapter<ViewSentRequestAdapter.ViewSentRequestViewHolder> {

    Context context;
    List<RequestModel> requestModelList;

    public ViewSentRequestAdapter() {
    }

    public ViewSentRequestAdapter(Context context, List<RequestModel> requestModelList) {
        this.context = context;
        this.requestModelList = requestModelList;
    }

    @NonNull
    @Override
    public ViewSentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_sent_request_layout, parent, false);
        return new ViewSentRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSentRequestViewHolder holder, int position) {
        RequestModel data = requestModelList.get(position);
        holder.nameTextView.setText(data.getName());
        holder.messageTextView.setText(data.getMessage());
        holder.requestTimeTextView.setText(data.getCurrent_time());

        holder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                dialog.show();

                dialog.setContentView(R.layout.custom_dialog_layout);

                TextView titleTextView, messageTextView;
                titleTextView = dialog.findViewById(R.id.titleTextView);
                messageTextView = dialog.findViewById(R.id.messageTextView);

                titleTextView.setText("Are you sure you want to cancel the request?");

                Button btn_cancel, btn_delete;
                btn_cancel = dialog.findViewById(R.id.btn_cancel);
                btn_delete = dialog.findViewById(R.id.btn_delete);
                btn_delete.setText("Yes");
                btn_cancel.setText("No");

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FirebaseDatabase.getInstance().getReference()
                                .child("Request").child(data.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Request Cancel Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return requestModelList.size();
    }

    public class ViewSentRequestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView,messageTextView,requestTimeTextView;
        AppCompatButton cancel_button;

        public ViewSentRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            requestTimeTextView = itemView.findViewById(R.id.requestTimeTextView);
            cancel_button = itemView.findViewById(R.id.cancel_button);

        }
    }

}
