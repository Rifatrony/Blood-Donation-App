package com.binaryit.blooddonationapp.Model;

public class CoordinatorModel {

    String name, number, uid, added_by, address, type;

    public CoordinatorModel() {
    }

    public CoordinatorModel(String name, String number, String uid, String added_by, String address, String type) {
        this.name = name;
        this.number = number;
        this.uid = uid;
        this.added_by = added_by;
        this.address = address;
        this.type = type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
