package com.example.blooddonationapp.Model;

public class AcceptRequestModel {

    String name, message, blood_group, uid;

    public AcceptRequestModel() {
    }

    public AcceptRequestModel(String name, String message, String blood_group, String uid) {
        this.name = name;
        this.message = message;
        this.blood_group = blood_group;
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
