package com.example.blooddonationapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestModel data = requestModelList.get(position);

        holder.nameTextView.setText(data.getName()+" (Me)");
        holder.messageTextView.setText(data.getMessage());
        holder.requestTimeTextView.setText(data.getCurrent_time()+"s");
        holder.patientProblemTextView.setText("Patient Problem: "+data.getPatient_problem());

        /*holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/

        /*This code is ok*/
        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.view_request_details);

                dialog.show();
                if (!data.getStatus().equals("ok")){
                    HashMap hashMap = new HashMap();
                    hashMap.put("status", "ok");
                    DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference().child("Request").child(data.getUid()).child(data.getRequest_uid());
                    dbRequest.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            /*if (task.isSuccessful()){
                                Toast.makeText(context, "You Confirm to donate Blood ", Toast.LENGTH_SHORT).show();
                            }*/
                        if (task.isSuccessful()){
                            dbRequest.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "You Confirm to donate Blood ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        }
                    });
                }



            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                /*FirebaseDatabase.getInstance().getReference("Request").child(user.getUid())
                        .child(data.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Delete Request", Toast.LENGTH_SHORT).show();
                            }
                        });*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestModelList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

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
