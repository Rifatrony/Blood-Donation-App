package com.example.blooddonationapp.Model;

public class TodayReadyModel {

    String address, blood_group, date, id, location, name, number, time, uid;

    public TodayReadyModel() {
    }

    public TodayReadyModel(String address, String blood_group, String date, String id, String location, String name, String number, String time, String uid) {
        this.address = address;
        this.blood_group = blood_group;
        this.date = date;
        this.id = id;
        this.location = location;
        this.name = name;
        this.number = number;
        this.time = time;
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
