package com.binaryit.blooddonationapp.Model;

public class AcceptRequestModel {

    String name, message, blood_group, my_uid, accepted_uid, recipient_number,
            donate_location, donate_time, number, patient_problem, donate_date;

    public AcceptRequestModel() {
    }

    public AcceptRequestModel(String name, String message, String blood_group, String my_uid, String accepted_uid, String donate_location, String donate_time, String number, String patient_problem, String recipient_number, String donate_date) {
        this.name = name;
        this.message = message;
        this.blood_group = blood_group;
        this.my_uid = my_uid;
        this.accepted_uid = accepted_uid;
        this.donate_location = donate_location;
        this.donate_time = donate_time;
        this.number = number;
        this.patient_problem = patient_problem;
        this.recipient_number = recipient_number;
        this.donate_date = donate_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
    }

    public String getAccepted_uid() {
        return accepted_uid;
    }

    public void setAccepted_uid(String accepted_uid) {
        this.accepted_uid = accepted_uid;
    }

    public String getDonate_location() {
        return donate_location;
    }

    public void setDonate_location(String donate_location) {
        this.donate_location = donate_location;
    }

    public String getDonate_time() {
        return donate_time;
    }

    public void setDonate_time(String donate_time) {
        this.donate_time = donate_time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPatient_problem() {
        return patient_problem;
    }

    public void setPatient_problem(String patient_problem) {
        this.patient_problem = patient_problem;
    }

    public String getRecipient_number() {
        return recipient_number;
    }

    public void setRecipient_number(String recipient_number) {
        this.recipient_number = recipient_number;
    }

    public String getDonate_date() {
        return donate_date;
    }

    public void setDonate_date(String donate_date) {
        this.donate_date = donate_date;
    }
}
