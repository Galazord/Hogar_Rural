package com.example.hogar_rural.Model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class User {

    // VARIABLES
    private String id, email, nickname, password, name, lastname, image, dni, birthday, phone, address, postal, municipality, province;
    private boolean advertising;
    private List<DocumentReference> favorites;



    public User(String email, String nickname, String password, String name, String lastname, String image, String dni, String birthday, String phone, String address, String postal, String municipality, String province, boolean advertising) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.image = image;
        this.dni = dni;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.postal = postal;
        this.municipality = municipality;
        this.province = province;
        this.advertising = advertising;
    }

    public User(String email, String nickname, String password, String name, String lastname, String image, String dni, String birthday, String phone, String address, String postal, String municipality, String province, boolean advertising, List<DocumentReference> favorites, List<DocumentReference> avaiables) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.image = image;
        this.dni = dni;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.postal = postal;
        this.municipality = municipality;
        this.province = province;
        this.advertising = advertising;
        this.favorites = favorites;
    }
    // CONTRUCTOR
    public User() {}

    // GET & SET
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isAdvertising() {
        return advertising;
    }

    public void setAdvertising(boolean advertising) {
        this.advertising = advertising;
    }

    public List<DocumentReference> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<DocumentReference> listFav) {
        this.favorites = listFav;
    }
}
