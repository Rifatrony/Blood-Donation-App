package com.example.blooddonationapp.Model;

public class RequestModel {

    String name, message,blood_amount, patient_problem,blood_group, donate_date, donate_location,
            donate_time, recipient_number, reference, current_time, status, uid, request_uid;

    public RequestModel() {
    }

    public RequestModel(String name, String message, String blood_amount, String patient_problem, String blood_group, String donate_date, String donate_location, String donate_time, String recipient_number, String reference, String current_time, String status, String uid, String request_uid) {
        this.name = name;
        this.message = message;
        this.blood_amount = blood_amount;
        this.patient_problem = patient_problem;
        this.blood_group = blood_group;
        this.donate_date = donate_date;
        this.donate_location = donate_location;
        this.donate_time = donate_time;
        this.recipient_number = recipient_number;
        this.reference = reference;
        this.current_time = current_time;
        this.status = status;
        this.uid = uid;
        this.request_uid = request_uid;
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

    public String getBlood_amount() {
        return blood_amount;
    }

    public void setBlood_amount(String blood_amount) {
        this.blood_amount = blood_amount;
    }

    public String getPatient_problem() {
        return patient_problem;
    }

    public void setPatient_problem(String patient_problem) {
        this.patient_problem = patient_problem;
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

    public String getDonate_time() {
        return donate_time;
    }

    public void setDonate_time(String donate_time) {
        this.donate_time = donate_time;
    }

    public String getRecipient_number() {
        return recipient_number;
    }

    public void setRecipient_number(String recipient_number) {
        this.recipient_number = recipient_number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRequest_uid() {
        return request_uid;
    }

    public void setRequest_uid(String request_uid) {
        this.request_uid = request_uid;
    }
}
