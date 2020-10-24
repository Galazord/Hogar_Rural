package com.example.hogar_rural.Model;

import com.google.firebase.Timestamp;

public class Booking {

    // VARIABLES
    private String id, nameUser, date;

    // CONSTRUCTORES
    public Booking() {
    }

    public Booking(String id, String nameUser, String date) {
        this.id = id;
        this.nameUser = nameUser;
        this.date = date;
    }

    // GET & SET
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
