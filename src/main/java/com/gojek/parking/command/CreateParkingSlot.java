package com.gojek.parking.command;

import com.gojek.parking.entity.Slot;

import java.util.Set;

public interface CreateParkingSlot {

     void initializeParkingSlot(int capacity);

     Set<Slot> getSlots();
}
