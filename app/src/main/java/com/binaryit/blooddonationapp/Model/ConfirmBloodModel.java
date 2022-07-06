package com.binaryit.blooddonationapp.Model;

public class ConfirmBloodModel {

    String blood_group, donate_date, donate_location, number, accepted_id,
    my_uid, patient_problem, donate_time, message, name,recipient_number;

    public ConfirmBloodModel() {
    }

    public ConfirmBloodModel(String blood_group, String donate_date, String donate_location, String number, String accepted_id, String my_uid, String patient_problem, String donate_time, String message, String name, String recipient_number) {
        this.blood_group = blood_group;
        this.donate_date = donate_date;
        this.donate_location = donate_location;
        this.number = number;
        this.accepted_id = accepted_id;
        this.my_uid = my_uid;
        this.patient_problem = patient_problem;
        this.donate_time = donate_time;
        this.message = message;
        this.name = name;
        this.recipient_number = recipient_number;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDonate_date() {
        return donate_date;
    }

    public void setDonate_date(String donate_date) {
        this.donate_date = donate_date;
    }

    public String getDonate_location() {
        return donate_location;
    }

    public void setDonate_location(String donate_location) {
        this.donate_location = donate_location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccepted_id() {
        return accepted_id;
    }

    public void setAccepted_id(String accepted_id) {
        this.accepted_id = accepted_id;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
    }

    public String getPatient_problem() {
        return patient_problem;
    }

    public void setPatient_problem(String patient_problem) {
        this.patient_problem = patient_problem;
    }

    public String getDonate_time() {
        return donate_time;
    }

    public void setDonate_time(String donate_time) {
        this.donate_time = donate_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipient_number() {
        return recipient_number;
    }

    public void setRecipient_number(String recipient_number) {
        this.recipient_number = recipient_number;
    }
}
