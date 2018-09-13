package com.gojek.parking.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Slot {

    private int id;
    private List<Vehicle> vehicles;
    private boolean isFree;

    public Slot(int id) {
        this.id = id;
        this.isFree = true;
        this.vehicles = new ArrayList<>(1);
    }

    public void add(Vehicle vehicle){
        vehicles.add(vehicle);
        isFree = false;
    }

    public boolean isFree() {
        return isFree;
    }

    public int getId(){
        return this.id;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return id == slot.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
