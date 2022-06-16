package com.example.blooddonationapp.Model;

public class RequestModel {

    String name, message, status, uid;

    public RequestModel() {
    }

    public RequestModel(String name, String message, String status, String uid) {
        this.name = name;
        this.message = message;
        this.status = status;
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
}
