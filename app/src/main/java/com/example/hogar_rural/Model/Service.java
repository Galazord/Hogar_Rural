package com.example.hogar_rural.Model;

import com.example.hogar_rural.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Service {

    // VARIABLES
    private String  id, name, icon;

    public Service(String id, String name, String icon) {
        this.id = id;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Service> getServices(){

        List<Service> services = new ArrayList<>();
        Collections.addAll(services,
                new Service("","" + R.string.filters_adapted,"ic_services_adapted_off.png"),
                new Service("","" + R.string.filters_air,"ic_service_air_off.png"),
                new Service("","" + R.string.filters_barbacue,"ic_service_barbecue_off.png"),
                new Service("","" + R.string.filters_bath,"ic_service_bath_off.png"),
                new Service("","" + R.string.filters_beach,"ic_service_beach_off.png"),
                new Service("","" + R.string.filters_breakfast,"ic_service_breakfast_off.png"),
                new Service("","" + R.string.filters_children,"ic_service_children_off.png"),
                new Service("","" + R.string.filters_climatized,"iic_service_climatized_pool_off.png"),
                new Service("","" + R.string.filters_fireplace,"ic_service_fireplace_off.png"),
                new Service("","" + R.string.filters_garden,"ic_service_garden_off.png"),
                new Service("","" + R.string.filters_heating,"ic_service_heating_off.png"),
                new Service("","" + R.string.filters_jacuzzi,"ic_service_jacuzzi_off.png"),
                new Service("","" + R.string.filters_kitchen,"ic_service_kitchen_off.png"),
                new Service("","" + R.string.filters_mountain,"ic_service_mountain_off.png"),
                new Service("","" + R.string.filters_parking,"ic_service_parking_off.png"),
                new Service("","" + R.string.filters_pets,"ic_service_pets_off.png"),
                new Service("","" + R.string.filters_pool,"ic_service_pool_off.png"),
                new Service("","" + R.string.filters_spa,"ic_service_spa_off.png"),
                new Service("","" + R.string.filters_tv,"ic_service_tv_off.png"),
                new Service("","" + R.string.filters_wifi,"ic_service_wifi_off.png")

        );

        return services;
    }
}
