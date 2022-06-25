package com.example.blooddonationapp.Model;

public class CoordinatorTypeModel {

    String type, uid;

    public CoordinatorTypeModel() {
    }

    public CoordinatorTypeModel(String type, String uid) {
        this.type = type;
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
