package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.R;

import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {

    AppCompatImageView imageBack;
    AppCompatButton sendButton;
    TextView bloodGroupTextView;

    EditText bloodAmountEditText, bloodDonateDateEditText, bloodDonateTimeEditText, bloodDonateLocationEditText,
            bloodRecipientNumberEditText, bloodReferenceEditText, patientProblemEditText;

    String blood_amount, patient_problem, donate_date, donate_time, donate_location, recipient_number, reference;

    String uid;

    String blood_group;
    String searchLat, searchLng, searchAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        initialization();

        searchLat = getIntent().getStringExtra("lat");
        searchLng = getIntent().getStringExtra("lng");
        searchAddress = getIntent().getStringExtra("address");
        System.out.println("Lat is: "+ searchLat);
        System.out.println("Lng is: "+ searchLng);
        System.out.println("address is: "+ searchAddress);

        bloodDonateLocationEditText.setText(searchAddress);

        blood_group = getIntent().getStringExtra("group");
        System.out.println("Group is : " + blood_group);

        bloodGroupTextView.setText(String.valueOf(blood_group));
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        bloodDonateDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestActivity.this, new DatePickerDialog.OnDateSetListener() {
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
    }

    private void checkValidation() {
        blood_amount = bloodAmountEditText.getText().toString().trim();
        patient_problem = patientProblemEditText.getText().toString().trim();
        donate_date = bloodDonateDateEditText.getText().toString().trim();
        donate_time = bloodDonateTimeEditText.getText().toString().trim();
        donate_location = bloodDonateLocationEditText.getText().toString().trim();
        recipient_number = bloodRecipientNumberEditText.getText().toString().trim();
        reference = bloodReferenceEditText.getText().toString().trim();

        if (blood_amount.isEmpty()){
            showToast("Amount Required");
            return;
        }
        if (blood_amount.equals("0")){
            showToast("Blood amount can't be Zero");
            return;
        }
        if (donate_date.isEmpty()){
            showToast("Need Date");
            return;
        }
        if (donate_time.isEmpty()){
            showToast("Need Time");
            return;
        }
        if (donate_location.isEmpty()){
            showToast("Give Location");
            return;
        }
        else {
            sendRequest();
        }

    }

    private void initialization() {

        imageBack = findViewById(R.id.imageBack);

        bloodGroupTextView = findViewById(R.id.bloodGroupTextView);
        patientProblemEditText = findViewById(R.id.patientProblemEditText);
        bloodAmountEditText = findViewById(R.id.bloodAmountEditText);
        bloodDonateDateEditText = findViewById(R.id.bloodDonateDateEditText);
        bloodDonateTimeEditText = findViewById(R.id.bloodDonateTimeEditText);
        bloodDonateLocationEditText = findViewById(R.id.bloodDonateAddressEditText);
        bloodRecipientNumberEditText = findViewById(R.id.bloodRecipientNumberEditText);
        bloodReferenceEditText = findViewById(R.id.bloodReferenceEditText);

        sendButton = findViewById(R.id.sendButton);

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendRequest(){

        Intent intent = new Intent(getApplicationContext(), GroupWiseBloodActivity.class);
        intent.putExtra("blood_group", blood_group);
        intent.putExtra("patient_problem", patient_problem);
        intent.putExtra("blood_amount", blood_amount);
        intent.putExtra("donate_date", donate_date);
        intent.putExtra("donate_time", donate_time);
        intent.putExtra("donate_location", donate_location);
        intent.putExtra("recipient_number", recipient_number);
        intent.putExtra("reference", reference);
        intent.putExtra("latitude", searchLat);
        intent.putExtra("longitude", searchLng);
        startActivity(intent);

    }
}