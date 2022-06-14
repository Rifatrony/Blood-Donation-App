package com.example.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {

    AppCompatButton sendButton;
    EditText bloodAmountEditText, bloodDonateDateEditText, bloodDonateTimeEditText,
            bloodDonateLocationEditText, bloodRecipientNumberEditText, bloodReferenceEditText;

    String blood_amount, donate_date, donate_time, donate_location, recipient_number, reference;

    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        initialization();
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

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
        bloodAmountEditText = findViewById(R.id.bloodAmountEditText);
        bloodDonateDateEditText = findViewById(R.id.bloodDonateDateEditText);
        bloodDonateTimeEditText = findViewById(R.id.bloodDonateTimeEditText);
        bloodDonateLocationEditText = findViewById(R.id.bloodDonateLocationEditText);
        bloodRecipientNumberEditText = findViewById(R.id.bloodRecipientNumberEditText);
        bloodReferenceEditText = findViewById(R.id.bloodReferenceEditText);

        sendButton = findViewById(R.id.sendButton);

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendRequest(){
        uid = getIntent().getStringExtra("uid");
        System.out.println(uid);
    }
}