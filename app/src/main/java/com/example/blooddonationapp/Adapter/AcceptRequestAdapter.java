package com.example.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationapp.Model.AcceptRequestModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class AcceptRequestAdapter extends RecyclerView.Adapter<AcceptRequestAdapter.AcceptRequestViewHolder> {

    Context context;
    List<AcceptRequestModel> acceptRequestModelList;

    public AcceptRequestAdapter() {
    }

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AcceptRequestViewHolder holder, int position) {
        AcceptRequestModel data = acceptRequestModelList.get(position);
        holder.messageTextView.setText(data.getMessage() + data.getBlood_group()
                +" At "+ data.getDonate_location());
        holder.nameTextView.setText(data.getName());
        holder.numberTextView.setText(data.getNumber());

        holder.callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setData(Uri.parse("tel:+8801772333793"));

                String num = "tel:"+data.getNumber();
                intent.setData(Uri.parse(num));
                context.startActivity(intent);
            }
        });

        //holder.bloodGroupTextView.setText(data.getBlood_group());

        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_layout);
                dialog.setCancelable(false);

                TextView titleTextView = dialog.findViewById(R.id.titleTextView);
                TextView messageTextView = dialog.findViewById(R.id.messageTextView);
                titleTextView.setText("You take blood from " + data.getName()+" ?");
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                Button btn_delete = dialog.findViewById(R.id.btn_delete);
                btn_delete.setText("Confirm");

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference dbConfirm = FirebaseDatabase.getInstance().getReference().child("Confirm Blood");

                        HashMap hashMap = new HashMap();
                        hashMap.put("name", data.getName());
                        hashMap.put("accepted_id", data.getAccepted_uid());
                        hashMap.put("blood_group", data.getBlood_group());
                        hashMap.put("donate_location", data.getDonate_location());
                        hashMap.put("donate_time", data.getDonate_time());
                        hashMap.put("message", data.getMessage());
                        hashMap.put("my_uid", data.getMy_uid());
                        hashMap.put("number", data.getNumber());
                        hashMap.put("patient_problem", data.getPatient_problem());
                        hashMap.put("recipient_number", data.getRecipient_number());

                        dbConfirm.child(data.getAccepted_uid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(context, "Confirm Blood", Toast.LENGTH_SHORT).show();
                            }
                        });


                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return acceptRequestModelList.size();
    }

    public class AcceptRequestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, bloodGroupTextView, messageTextView, numberTextView;

        AppCompatButton confirmButton;
        AppCompatImageView callImageView;

        public AcceptRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            confirmButton = itemView.findViewById(R.id.confirmButton);
            callImageView = itemView.findViewById(R.id.callImageView);

        }
    }

}
