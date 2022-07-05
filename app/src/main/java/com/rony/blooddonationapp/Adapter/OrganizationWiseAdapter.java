package com.rony.blooddonationapp.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rony.blooddonationapp.Activity.RegisterActivity;
import com.rony.blooddonationapp.Model.User;
import com.rony.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OrganizationWiseAdapter extends RecyclerView.Adapter<OrganizationWiseAdapter.OrganizationWiseViewHolder> {

    Context context;
    List<User> userList;

    String name, blood_amount, blood_group, current_time, donate_date, donate_location, donate_time, message,
            number, patient_problem, recipient_number, reference, status = "pending", uid;

    public OrganizationWiseAdapter() {
    }

    public OrganizationWiseAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public OrganizationWiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_profile_layout, parent, false);
        return new OrganizationWiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationWiseViewHolder holder, int position) {
        User data = userList.get(position);
        holder.userNameTextView.setText(data.getName());
        holder.userNumberTextView.setText(data.getEmail());
        holder.userBloodGroupTextView.setText(data.getBlood_group());
        holder.userAddressTextView.setText(data.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.today_ready_details_layout);
                dialog.setCancelable(false);

                dialog.show();

                TextView nameAndBloodGroupTextView = dialog.findViewById(R.id.nameAndBloodGroupTextView);
                EditText bloodAmountEditText = dialog.findViewById(R.id.bloodAmountEditText);
                EditText patientProblemEditText = dialog.findViewById(R.id.patientProblemEditText);
                EditText bloodDonateDateEditText = dialog.findViewById(R.id.bloodDonateDateEditText);
                EditText bloodDonateTimeEditText = dialog.findViewById(R.id.bloodDonateTimeEditText);
                EditText bloodDonateAddressEditText = dialog.findViewById(R.id.bloodDonateAddressEditText);
                EditText bloodRecipientNumberEditText = dialog.findViewById(R.id.bloodRecipientNumberEditText);
                EditText bloodReferenceEditText = dialog.findViewById(R.id.bloodReferenceEditText);

                Button cancelButton = dialog.findViewById(R.id.cancelButton);
                Button sendRequestButton = dialog.findViewById(R.id.sendRequestButton);

                nameAndBloodGroupTextView.setText(data.getName()+" ( "+data.getBlood_group()+" )");

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                bloodDonateDateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                String date;
                                month = month +1;

                                if (month < 10){
                                    date = day+"/0"+month+"/"+year;
                                    /*THIS PART*/
                                    if (day < 10) {
                                        date = "0"+day+"/0"+month+"/"+year;
                                    }
                                }
                                else {
                                    date = day+"/"+month+"/"+year;

                                    /*THIS PART*/

                                    if (day < 10) {
                                        date = "0"+day+"/0"+month+"/"+year;
                                    }
                                }
                                bloodDonateDateEditText.setText(date);
                            }
                        },year,month,day);

                        datePickerDialog.show();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                sendRequestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Calendar calendar = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm:ss");
                        String currentTime = mdformat.format(calendar.getTime());
                        System.out.println(currentTime);

                        name = data.getName();
                        blood_amount = bloodAmountEditText.getText().toString().trim();
                        blood_group = data.getBlood_group().toString().trim();
                        current_time = currentTime;
                        donate_date = bloodDonateDateEditText.getText().toString().trim();
                        donate_location = bloodDonateAddressEditText.getText().toString().trim();
                        donate_time = bloodDonateTimeEditText.getText().toString().trim();

                        message = data.getName()+"Request For 1 bag "+data.getBlood_group();
                        number = data.getNumber();
                        patient_problem = patientProblemEditText.getText().toString().trim();
                        recipient_number = bloodRecipientNumberEditText.getText().toString().trim();
                        reference = bloodReferenceEditText.getText().toString().trim();
                        status = "pending";

                        //String request_uid = ; //will current user id
                        uid = data.getId();


                        DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference().child("Request");


                        HashMap hashMap = new HashMap();
                        hashMap.put("name", name);
                        hashMap.put("blood_amount", blood_amount);
                        hashMap.put("blood_group", blood_group);
                        hashMap.put("current_time", current_time);
                        hashMap.put("donate_date", donate_date);
                        hashMap.put("donate_location", donate_location);
                        hashMap.put("donate_time", donate_time);
                        hashMap.put("message", message);
                        hashMap.put("number", number);
                        hashMap.put("patient_problem", patient_problem);
                        hashMap.put("recipient_number", recipient_number);
                        hashMap.put("reference", reference);
                        hashMap.put("status", status);
                        hashMap.put("request_uid", FirebaseAuth.getInstance().getUid());  // current user id
                        hashMap.put("uid", uid);

                        dbRequest.child(uid).child(FirebaseAuth.getInstance().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(context, "Request Sent to " + data.getName(), Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class OrganizationWiseViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, userNumberTextView,
                userBloodGroupTextView, userAddressTextView;

        public OrganizationWiseViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userBloodGroupTextView = itemView.findViewById(R.id.userBloodGroupTextView);
            userAddressTextView = itemView.findViewById(R.id.userAddressTextView);

        }
    }

}
