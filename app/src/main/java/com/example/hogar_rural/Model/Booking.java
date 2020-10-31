package com.example.hogar_rural.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Booking {

    // VARIABLES
    private Timestamp date_reserved;
    private DocumentReference user;

    // CONSTRUCTORES
    public Booking() {
    }

    public Booking(Timestamp date_reserved, DocumentReference user) {
        this.date_reserved = date_reserved;
        this.user = user;
    }

    public Timestamp getDate_reserved() {
        return date_reserved;
    }

    public void setDate_reserved(Timestamp date_reserved) {
        this.date_reserved = date_reserved;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }
}
