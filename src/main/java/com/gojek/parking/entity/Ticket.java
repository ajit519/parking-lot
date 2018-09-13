package com.gojek.parking.entity;

public class Ticket {

    private long id;
    private Vehicle vehicle;
    private Slot slot;
    private long localDateTime;

    public Ticket(long id, Slot slot, Vehicle vehicle) {
        this.id = id;
        this.slot = slot;
        this.vehicle = vehicle;
        this.localDateTime = System.currentTimeMillis();
    }
}
