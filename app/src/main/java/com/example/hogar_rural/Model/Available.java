package com.example.hogar_rural.Model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Available {

    String id;
    List<Timestamp> dates_reserved;
    String id_user;

    public Available(){

    }

    public Available(String id, List<Timestamp> dates_reserved, String id_user) {
        this.id = id;
        this.dates_reserved = dates_reserved;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Timestamp> getDates_reserved() {
        return dates_reserved;
    }

    public void setDates_reserved(List<Timestamp> dates_reserved) {
        this.dates_reserved = dates_reserved;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
