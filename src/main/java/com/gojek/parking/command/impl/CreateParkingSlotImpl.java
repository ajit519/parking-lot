package com.gojek.parking.command.impl;

import com.gojek.parking.command.CreateParkingSlot;
import com.gojek.parking.entity.Slot;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class CreateParkingSlotImpl  implements CreateParkingSlot {

    private Set<Slot> slots ;

    @Override
    public void initializeParkingSlot(int capacity) {
            if(capacity <= 0){
                throw new IllegalArgumentException("Invalid input. Please provide positive number for slot ");
            }
            slots = new HashSet<>(capacity);
            addSlots(slots, capacity);
        System.out.println(String.format(" \n Created a parking lot with %d slots", capacity));
    }

    @Override
    public Set<Slot> getSlots() {
        return slots;
    }

    private void addSlots(Set<Slot> slots , int capacity){
        IntStream.rangeClosed(1, capacity).forEach(id -> createSlot(id, slots));
    }

    private void createSlot(int id, Set<Slot> slots){
        slots.add(new Slot(id));
    }
}
