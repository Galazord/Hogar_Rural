package com.example.hogar_rural.Model;


import com.example.hogar_rural.Utils.UtilMethod;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;

public class HomeFilter {

    private Filter filter;

    private Home home;

    public HomeFilter(Filter filter, Home home) {
        this.filter = filter;
        this.home = home;
    }

    // Filtro de número de personas
    public boolean filterNumberPeopleActive(){
        return filter.getNumPeople()!=0;
    }
    public boolean filterNumberPeople(){
        return filter.getNumPeople() <= home.getAmount();
    }

    // Filtro de fechas disponibles
    public boolean filterRangeOfDateActive(){
        return (!filter.getDateEntrace().equals("")  || !filter.getDateExit().equals("") );
    }
    public boolean filterRangeOfDate(Available available){

        Date fromFilter = UtilMethod.getDateFromStingUS(filter.getDateEntrace());
        Date sinceFilter = UtilMethod.getDateFromStingUS((filter.getDateExit()));

        for (Timestamp timestamp: available.getDates_reserved()
        ) {

            Date tmpToDate = UtilMethod.getDateFromSting(new Date(timestamp.toDate().getTime() + (1000 * 60 * 60 * 24)));

            if(tmpToDate.toString().equals(fromFilter.toString()) || tmpToDate.toString().equals(sinceFilter.toString())){
                return false;
            }
        }

        return true;
    }

    // Filtro de valoración
    public boolean filterValorationsActive(){
        return filter.getValoration() != 0;
    }
    public boolean filterValorations(){
        return filter.getValoration() == home.getValoration();
    }

    // Filtro de tipo de vivienda (Íntegra/ Habitaciones)
    public boolean filterRoomsActive(){
        return filter.getTypeRoom() != -1;
    }
    public boolean filterRooms(){
        return filter.getTypeRoom() == home.getType();
    }

    // Filtro de precio
    public boolean filterPriceActive(){
        return filter.getPrices() != 0;
    }
    public boolean filterPrice(){
        return filter.getPrices() <= home.getPrice();
    }

    // Filtro de Servicios (iconos activos)
    public boolean filterServicesActive(){
        return filter.getServices().size()!=0;
    }
    public boolean filterServices(){
        List<String> servicesHome = home.getServices();
        List<String> servicesFilter = filter.getServices();
        /*
        for (String serviceName: servicesHome
        ) {
            boolean containtAll =
            if(servicesFilter.contains(serviceName)){
                return true;
            }
        }*/
        return servicesHome.containsAll(servicesFilter);
    }

    // GET & SET
    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }
}
