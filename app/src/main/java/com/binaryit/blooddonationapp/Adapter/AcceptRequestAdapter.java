package com.binaryit.blooddonationapp.Adapter;

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

import com.binaryit.blooddonationapp.Model.AcceptRequestModel;
import com.binaryit.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AcceptRequestAdapter extends RecyclerView.Adapter<AcceptRequestAdapter.AcceptRequestViewHolder> {

    Context context;
    List<AcceptRequestModel> acceptRequestModelList;

    Date date;
    String afterThreeMonthsDate;

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_layout);
                dialog.setCancelable(false);
                dialog.show();

                TextView titleTextView = dialog.findViewById(R.id.titleTextView);
                TextView messageTextView = dialog.findViewById(R.id.messageTextView);
                titleTextView.setText("Confirm Delete");
                messageTextView.setText("Are you sure you want to delete ? ");
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                Button btn_delete = dialog.findViewById(R.id.btn_delete);
                btn_delete.setText("Delete");

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
                                .child("Accept Request").child(FirebaseAuth.getInstance().getUid())
                                .child(data.getAccepted_uid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.dismiss();
                                            Toast.makeText(context, "Request Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });


                return false;

            }
        });

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
                titleTextView.setText("Confirm Blood");
                messageTextView.setText("You take blood from " + data.getName()+" ? press confirm button.");
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

                        Calendar c = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = sdf.format(c.getTime());

                        try {
                            date = sdf.parse(strDate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MONTH, +3);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                        afterThreeMonthsDate = date.format(calendar.getTime());

                        String  id;

                        id = dbConfirm.push().getKey();

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
                        hashMap.put("donate_date", strDate);
                        hashMap.put("times", 1);
                        hashMap.put("id", id);

                        dbConfirm.child(id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                FirebaseDatabase.getInstance().getReference()
                                        .child("Accept Request").child(data.getMy_uid()).child(data.getAccepted_uid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){


                                                    Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show();

                                                    DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference()
                                                            .child("User").child(data.getAccepted_uid());

                                                    HashMap updateUser = new HashMap();
                                                    updateUser.put("last_donate", strDate);
                                                    updateUser.put("next_donate", afterThreeMonthsDate);

                                                    dbUser.updateChildren(updateUser).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {

                                                            if (task.isSuccessful()){
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        });

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
