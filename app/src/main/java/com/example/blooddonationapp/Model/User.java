package com.example.blooddonationapp.Model;

public class User {

    String name, number, email, password, confirm_password, country, blood_group, dob, longitude, latitude,
            last_donate,address, type, search, id, token, organization, role, total_member, next_donate;

    public User() {
    }

    public User(String name, String number, String email, String password, String confirm_password, String country, String blood_group, String dob, String longitude, String latitude, String last_donate, String address, String type, String search, String id, String token, String organization, String role, String total_member, String next_donate) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
        this.country = country;
        this.blood_group = blood_group;
        this.dob = dob;
        this.longitude = longitude;
        this.latitude = latitude;
        this.last_donate = last_donate;
        this.address = address;
        this.type = type;
        this.search = search;
        this.id = id;
        this.token = token;
        this.organization = organization;
        this.role = role;
        this.total_member = total_member;
        this.next_donate = next_donate;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLast_donate() {
        return last_donate;
    }

    public void setLast_donate(String last_donate) {
        this.last_donate = last_donate;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTotal_member() {
        return total_member;
    }

    public void setTotal_member(String total_member) {
        this.total_member = total_member;
    }

    public String getNext_donate() {
        return next_donate;
    }

    public void setNext_donate(String next_donate) {
        this.next_donate = next_donate;
    }
}
