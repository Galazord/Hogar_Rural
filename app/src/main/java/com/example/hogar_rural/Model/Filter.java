package com.example.hogar_rural.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Filter implements Parcelable {
    int numPeople;
    String dateEntrace;
    String dateExit;
    int valoration;
    int typeRoom;
    int prices;
    List<String> services;
    List<String> order;

    public Filter(int numPeople, String dateEntrace, String dateExit, int valoration, int typeRoom, int prices, List<String> services, List<String> order) {
        this.numPeople = numPeople;
        this.dateEntrace = dateEntrace;
        this.dateExit = dateExit;
        this.valoration = valoration;
        this.typeRoom = typeRoom;
        this.prices = prices;
        this.services = services;
        this.order = order;
    }

    protected Filter(Parcel in) {
        numPeople = in.readInt();
        dateEntrace = in.readString();
        dateExit = in.readString();
        valoration = in.readInt();
        typeRoom = in.readInt();
        prices = in.readInt();
        services = in.createStringArrayList();
        order = in.createStringArrayList();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public String getDateEntrace() {
        return dateEntrace;
    }

    public void setDateEntrace(String dateEntrace) {
        this.dateEntrace = dateEntrace;
    }

    public String getDateExit() {
        return dateExit;
    }

    public void setDateExit(String dateExit) {
        this.dateExit = dateExit;
    }

    public int getValoration() {
        return valoration;
    }

    public void setValoration(int valoration) {
        this.valoration = valoration;
    }

    public int getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(int typeRoom) {
        this.typeRoom = typeRoom;
    }

    public int getPrices() {
        return prices;
    }

    public void setPrices(int prices) {
        this.prices = prices;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numPeople);
        dest.writeString(dateEntrace);
        dest.writeString(dateExit);
        dest.writeInt(valoration);
        dest.writeInt(typeRoom);
        dest.writeInt(prices);
        dest.writeStringList(services);
        dest.writeStringList(order);
    }
}
