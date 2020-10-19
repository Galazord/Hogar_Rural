package com.example.hogar_rural.Model;


import com.example.hogar_rural.Utils.UtilMethod;
import com.google.firebase.Timestamp;

import java.util.List;

public class HomeFilter {

    private Filter filter;
    private Home home;

    public HomeFilter(Filter filter, Home home) {
        this.filter = filter;
        this.home = home;
    }

    public boolean filterNumberPeopleActive(){
        return filter.getNumPeople()!=0;
    }
    public boolean filterNumberPeople(){
        return filter.getNumPeople() == home.getAmount();
    }

    public boolean filterRangeOfDateActive(){

        return (!filter.getDateEntrace().equals("")  || !filter.getDateExit().equals("") );
    }
    public boolean filterRangeOfDate(Available available){

        Timestamp fromFilter = UtilMethod.getTimestamp(filter.getDateEntrace());
        Timestamp sinceFilter = UtilMethod.getTimestamp(filter.getDateExit());


        List<Timestamp> dates_reserved = available.getDates_reserved();
        return (dates_reserved.contains(fromFilter) || dates_reserved.contains(sinceFilter));
    }
    public boolean filterValorationsActive(){
        return filter.getValoration() != 0;
    }
    public boolean filterValorations(){
        return filter.getValoration() == home.getValoration();
    }
    public boolean filterRoomsActive(){
        return filter.getTypeRoom() != -1;
    }

    public boolean filterRooms(){
        return filter.getTypeRoom() == home.getType();
    }



    public boolean filterPrice(){
        return filter.getPrices() == home.getPrice();
    }

    public boolean filterServicesActive(){
       return filter.getServices().size()!=0;
    }
    public boolean filterServices(){
        List<String> servicesHome = home.getServices();
        List<String> servicesFilter = filter.getServices();

        for (String serviceName: servicesHome
             ) {
            if(servicesFilter.contains(serviceName)){
                return true;
            }
        }
        return false;
    }
}
