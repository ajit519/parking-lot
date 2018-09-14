package com.gojek.parking.command;


import com.gojek.parking.entity.Slot;
import com.gojek.parking.entity.Ticket;

import java.util.List;

public interface ParkService {

    Ticket park(String registrationNumber, String color);

    void unpark(int slotId);

    void status();

    List<String> getRegistrationNumber(String color);

    Slot getSlot(String registration);

    List<Integer> getSlotsForColour(String color);

}
