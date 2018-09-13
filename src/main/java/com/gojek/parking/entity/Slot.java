package com.gojek.parking.entity;

import java.util.ArrayList;
import java.util.List;

public class Slot {

    private int id;
    private int florId;
    private List<Vehicle> vehicles;

    public Slot(int id, int florId) {
        this.id = id;
        this.florId = florId;
        this.vehicles = new ArrayList<>();
    }

    public void add(Vehicle vehicle){
        vehicles.add(vehicle);
    }
}
