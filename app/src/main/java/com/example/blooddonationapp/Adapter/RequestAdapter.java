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
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    Context context;
    List<RequestModel> requestModelList;

    public RequestAdapter() {
    }

    public RequestAdapter(Context context, List<RequestModel> requestModelList) {
        this.context = context;
        this.requestModelList = requestModelList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_sample_layout, parent, false);
        return new RequestViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestModel data = requestModelList.get(position);

        holder.nameTextView.setText(data.getName()+" (Me)");
        holder.messageTextView.setText(data.getMessage());
        holder.requestTimeTextView.setText(data.getCurrent_time()+"s");
        holder.patientProblemTextView.setText("Patient Problem: "+data.getPatient_problem());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_layout);

                dialog.show();
                dialog.setCancelable(false);

                Button btn_cancel, btn_delete;
                btn_cancel = dialog.findViewById(R.id.btn_cancel);
                btn_delete = dialog.findViewById(R.id.btn_delete);

                btn_cancel.setOnClickListener(view1 -> dialog.dismiss());
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(context, "Current Uid is " + data.getUid(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Request Uid is " + data.getRequest_uid(), Toast.LENGTH_SHORT).show();

                        FirebaseDatabase.getInstance().getReference()
                                .child("Request").child(data.getUid())
                                .removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "Request Deleted", Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        dialog.dismiss();
                    }
                });

            }
        });

        /*This code is ok*/
        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.view_request_details);

                dialog.show();
                dialog.setCancelable(false);

                TextView patientProblemTextView = dialog.findViewById(R.id.patientProblemTextView);
                TextView bloodAmountTextView = dialog.findViewById(R.id.bloodAmountTextView);
                TextView donatDateTextView = dialog.findViewById(R.id.donatDateTextView);
                TextView donateAddressTextView = dialog.findViewById(R.id.donateAddressTextView);
                Button confirmRequestButton = dialog.findViewById(R.id.confirmRequestButton);
                Button cancelRequestButton = dialog.findViewById(R.id.cancelRequestButton);

                //requestSenderNameTextView.setText(data.getName());
                patientProblemTextView.setText("Patient Problem: "+data.getPatient_problem());
                bloodAmountTextView.setText("Blood Amount: "+data.getBlood_amount() +" "+ data.getBlood_group());
                donatDateTextView.setText("Donate Date: "+data.getDonate_date() + "   At " + data.getDonate_time());
                donateAddressTextView.setText("Address: "+data.getDonate_location());

                cancelRequestButton.setOnClickListener(view13 -> dialog.dismiss());

                confirmRequestButton.setOnClickListener(view12 -> {
                    dialog.dismiss();
                    if (!data.getStatus().equals("ok")){
                        HashMap hashMap = new HashMap();
                        hashMap.put("status", "ok");

                        DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference()
                                .child("Request").child(data.getUid()).child(data.getRequest_uid());

                        dbRequest.updateChildren(hashMap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){

                                dbRequest.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        HashMap acceptRequest = new HashMap();
                                        acceptRequest.put("name", data.getName());
                                        acceptRequest.put("message", " accept your request to donate 1 Bag " );
                                        acceptRequest.put("blood_group", data.getBlood_group()+" blood.");
                                        acceptRequest.put("my_uid", data.getRequest_uid());
                                        acceptRequest.put("accepted_uid", data.getUid());
                                        acceptRequest.put("blood_amount", data.getBlood_amount());
                                        acceptRequest.put("donate_location", data.getDonate_location());
                                        acceptRequest.put("donate_time", data.getDonate_time());
                                        acceptRequest.put("patient_problem", data.getPatient_problem());
                                        acceptRequest.put("recipient_number", data.getRecipient_number());
                                        acceptRequest.put("number", data.getNumber());

                                        System.out.println("\n\nUid is " + data.getUid());

                                        DatabaseReference dbAcceptRequest = FirebaseDatabase.getInstance().getReference()
                                                .child("Accept Request").child(data.getRequest_uid()).child(data.getUid());

                                        dbAcceptRequest.updateChildren(acceptRequest).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return  requestModelList == null ? 0 : requestModelList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, messageTextView, requestTimeTextView,patientProblemTextView;
        AppCompatButton confirmButton, deleteButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            requestTimeTextView = itemView.findViewById(R.id.requestTimeTextView);
            patientProblemTextView = itemView.findViewById(R.id.patientProblemTextView);
            confirmButton = itemView.findViewById(R.id.confirmButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }

}
