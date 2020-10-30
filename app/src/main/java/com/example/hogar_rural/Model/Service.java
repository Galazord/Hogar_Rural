package com.example.hogar_rural.Model;

import android.content.Context;

import com.example.hogar_rural.R;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Service {

    // VARIABLES
    private String  id, name;
    int icon;

    int icon_on;
    
    public Service(String id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
    public Service(String id, String name, int icon, int icon_on) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.icon_on = icon_on;
    }
    public Service(String name, int icon) {
     
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // CONTRUCTOR
    public Service() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon_on() {
        return icon_on;
    }

    public void setIcon_on(int icon_on) {
        this.icon_on = icon_on;
    }

    public List<Service> getServicesOff(Context context){

        List<Service> services = new ArrayList<>();
        Collections.addAll(services,
                new Service("",context.getString(R.string.filters_adapted),R.drawable.ic_service_adapted_off),
                new Service("",context.getString(R.string.filters_air),R.drawable.ic_service_air_off,R.drawable.ic_service_air_on),
                new Service("",context.getString(R.string.filters_barbacue),R.drawable.ic_service_barbecue_off,R.drawable.ic_service_barbecue_on),
                new Service("",context.getString(R.string.filters_bath),R.drawable.ic_service_bath_off,R.drawable.ic_service_bath_on),
                new Service("",context.getString(R.string.filters_beach),R.drawable.ic_service_beach_off,R.drawable.ic_service_beach_on),
                new Service("",context.getString(R.string.filters_breakfast),R.drawable.ic_service_breakfast_off,R.drawable.ic_service_breakfast_on),
                new Service("",context.getString(R.string.filters_children),R.drawable.ic_service_children_off,R.drawable.ic_service_children_on),
                new Service("",context.getString(R.string.filters_climatized),R.drawable.ic_service_climatized_pool_off,R.drawable.ic_service_climatized_pool_on),
                new Service("",context.getString(R.string.filters_fireplace),R.drawable.ic_service_fireplace_off,R.drawable.ic_service_fireplace_on),
                new Service("",context.getString(R.string.filters_garden),R.drawable.ic_service_garden_off,R.drawable.ic_service_garden_on),
                new Service("",context.getString(R.string.filters_heating),R.drawable.ic_service_heating_off,R.drawable.ic_service_heating_on),
                new Service("",context.getString(R.string.filters_jacuzzi),R.drawable.ic_service_jacuzzi_off,R.drawable.ic_service_jacuzzi_on),
                new Service("",context.getString(R.string.filters_kitchen),R.drawable.ic_service_kitchen_off,R.drawable.ic_service_kitchen_on),
                new Service("",context.getString(R.string.filters_mountain),R.drawable.ic_service_mountain_off,R.drawable.ic_service_mountain_on),
                new Service("",context.getString(R.string.filters_parking),R.drawable.ic_service_parking_off,R.drawable.ic_service_parking_on),
                new Service("",context.getString(R.string.filters_pets),R.drawable.ic_service_pets_off,R.drawable.ic_service_pets_on),
                new Service("",context.getString(R.string.filters_pool),R.drawable.ic_service_pool_off,R.drawable.ic_service_pool_on),
                new Service("",context.getString(R.string.filters_spa),R.drawable.ic_service_spa_off,R.drawable.ic_service_spa_on),
                new Service("",context.getString(R.string.filters_tv),R.drawable.ic_service_tv_off,R.drawable.ic_service_tv_on),
                new Service("",context.getString(R.string.filters_wifi),R.drawable.ic_service_wifi_off,R.drawable.ic_service_wifi_on)

        );

        return services;
    }
    public List<Service> getServices(Context context){

        List<Service> services = new ArrayList<>();
        Collections.addAll(services,
                new Service("",context.getString(R.string.filters_adapted),R.drawable.ic_service_adapted_on),
                new Service("",context.getString(R.string.filters_air),R.drawable.ic_service_air_on),
                new Service("",context.getString(R.string.filters_barbacue),R.drawable.ic_service_barbecue_on),
                new Service("",context.getString(R.string.filters_bath),R.drawable.ic_service_bath_on),
                new Service("",context.getString(R.string.filters_beach),R.drawable.ic_service_beach_on),
                new Service("",context.getString(R.string.filters_breakfast),R.drawable.ic_service_breakfast_on),
                new Service("",context.getString(R.string.filters_children),R.drawable.ic_service_children_on),
                new Service("",context.getString(R.string.filters_climatized),R.drawable.ic_service_climatized_pool_on),
                new Service("",context.getString(R.string.filters_fireplace),R.drawable.ic_service_fireplace_on),
                new Service("",context.getString(R.string.filters_garden),R.drawable.ic_service_garden_on),
                new Service("",context.getString(R.string.filters_heating),R.drawable.ic_service_heating_on),
                new Service("",context.getString(R.string.filters_jacuzzi),R.drawable.ic_service_jacuzzi_on),
                new Service("",context.getString(R.string.filters_kitchen),R.drawable.ic_service_kitchen_on),
                new Service("",context.getString(R.string.filters_mountain),R.drawable.ic_service_mountain_on),
                new Service("",context.getString(R.string.filters_parking),R.drawable.ic_service_parking_on),
                new Service("",context.getString(R.string.filters_pets),R.drawable.ic_service_pets_on),
                new Service("",context.getString(R.string.filters_pool),R.drawable.ic_service_pool_on),
                new Service("",context.getString(R.string.filters_spa),R.drawable.ic_service_spa_on),
                new Service("",context.getString(R.string.filters_tv),R.drawable.ic_service_tv_on),
                new Service("",context.getString(R.string.filters_wifi),R.drawable.ic_service_wifi_on)

        );

        return services;
    }
}
