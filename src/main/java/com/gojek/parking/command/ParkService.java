package com.gojek.parking.command;


import com.gojek.parking.entity.Ticket;

public interface ParkService {

    Ticket park(String registrationNumber, String color);

    void unpark(int slotId);
}
