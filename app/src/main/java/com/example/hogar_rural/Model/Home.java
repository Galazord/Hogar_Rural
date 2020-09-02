package com.example.hogar_rural.Model;


import com.google.firebase.Timestamp;

import java.util.List;

public class Home {

    // VARIABLES
    private String id, owner, name, address, postal, municipality, province, description, activities, interesting_places;
    private Long type, amount, price, valoration;
    private Timestamp creation_date, update_date;
    private List<String> services;
    private List<String> images;

    public Home(String id, String owner, String name, String address, String postal, String municipality, String province, String description, String activities, String interesting_places, Long type, Long amount, Long price, Long valoration, Timestamp creation_date, Timestamp update_date, List<String> services) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.municipality = municipality;
        this.province = province;
        this.description = description;
        this.activities = activities;
        this.interesting_places = interesting_places;
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.valoration = valoration;
        this.creation_date = creation_date;
        this.update_date = update_date;
        this.services =services;
    }

    // CONTRUCTOR
    public Home() {}

    // GET & SET
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getInteresting_places() {
        return interesting_places;
    }

    public void setInteresting_places(String interesting_places) {
        this.interesting_places = interesting_places;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getValoration() {
        return valoration;
    }

    public void setValoration(Long valoration) {
        this.valoration = valoration;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }

    public Timestamp getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Timestamp update_date) {
        this.update_date = update_date;
    }
}
