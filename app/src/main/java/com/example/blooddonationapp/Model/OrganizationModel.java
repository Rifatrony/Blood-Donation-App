package com.example.blooddonationapp.Model;

public class OrganizationModel {

    String name, number, uid, added_by;

    public OrganizationModel() {
    }

    public OrganizationModel(String name, String number, String uid, String added_by) {
        this.name = name;
        this.number = number;
        this.uid = uid;
        this.added_by = added_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }
}
