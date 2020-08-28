package com.example.hogar_rural.Model;

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
                new Service("","Adaptado","ic_services_adapted.png"),
                new Service("","Aire acondicionado","ic_service_air.png")

        );

        return services;
    }
}
