package com.example.hogar_rural.Model;

import com.google.firebase.Timestamp;

public class Comment {

    private String id;

    public String getId_homes() {
        return id_homes;
    }

    public void setId_homes(String id_homes) {
        this.id_homes = id_homes;
    }

    private String id_homes;
    private String comment;
    private String user;
    private String name_user;
    private Long valoration;
    private Timestamp date;

    public Comment() {
    }

    public Comment(String id, String id_homes,  String comment, String user, String name_user, Long valoration, Timestamp date) {
        this.id = id;
        this.id_homes = id_homes;
        this.comment = comment;
        this.user = user;
        this.name_user=name_user;
        this.valoration = valoration;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getValoration() {
        return valoration;
    }

    public void setValoration(Long valoration) {
        this.valoration = valoration;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }
}
