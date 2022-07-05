package com.rony.blooddonationapp.Model;

public class RequestIdModel {

    String uid, current_uid;

    public RequestIdModel() {
    }

    public RequestIdModel(String uid, String current_uid) {
        this.uid = uid;
        this.current_uid = current_uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrent_uid() {
        return current_uid;
    }

    public void setCurrent_uid(String current_uid) {
        this.current_uid = current_uid;
    }
}
