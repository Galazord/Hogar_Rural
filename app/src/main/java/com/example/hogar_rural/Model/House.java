package com.example.hogar_rural.Model;

public class House {
    //Hola
    // VARIABLES (mismo nombre que en las bases de datos)
    private String idHouse, name, rental, personMax, price, numOpinion, valoration, galleryImg;

    // CONSTRUCTOR
    // Constructor Vacío
    public House(){}

    // GET & SET hola
    public String getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(String idHouse) {
        this.idHouse = idHouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRental() {
        return rental;
    }

    public void setRental(String rental) {
        this.rental = rental;
    }

    public String getPersonMax() {
        return personMax;
    }

    public void setPersonMax(String personMax) {
        this.personMax = personMax;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumOpinion() {
        return numOpinion;
    }

    public void setNumOpinion(String numOpinion) {
        this.numOpinion = numOpinion;
    }

    public String getValoration() {
        return valoration;
    }

    public void setValoration(String valoration) {
        this.valoration = valoration;
    }

    public String getGalleryImg() {
        return galleryImg;
    }

    public void setGalleryImg(String galleryImg) {
        this.galleryImg = galleryImg;
    }

}
