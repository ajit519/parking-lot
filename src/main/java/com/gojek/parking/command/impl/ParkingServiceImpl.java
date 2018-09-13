package com.gojek.parking.command.impl;

import com.gojek.parking.command.ParkService;
import com.gojek.parking.entity.Slot;
import com.gojek.parking.entity.Ticket;
import com.gojek.parking.entity.Vehicle;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class ParkingServiceImpl implements ParkService {

    private Set<Slot> freeSlots;
    private Set<Slot> usedSlot;
    private Random random;

    public ParkingServiceImpl(Set<Slot> slots) {
        this.freeSlots = slots;
        this.usedSlot = new HashSet<>();
        random = new Random();
    }

    @Override
    public Ticket park(String registrationNumber, String color) {

        if (registrationNumber == null || registrationNumber.trim().length() == 0
                || color == null || color.trim().length() == 0) {
            throw new IllegalArgumentException("Please provide car details");
        }

        if (freeSlots.size() == 0){
            System.out.println("Sorry, parking lot is full");
            throw new RuntimeException("Sorry, parking lot is full");
        }

        for (Slot slot: usedSlot){
            Optional<Vehicle> alreadyParked = slot.getVehicles().stream()
                              .filter(vehicle -> vehicle.getRegistrationNumber().equals(registrationNumber))
                              .findFirst();

            if (alreadyParked.isPresent()){
                throw new RuntimeException(String.format("Vehicle with registration number %s already been parked", registrationNumber));
            }
        }

        Vehicle vehicle = new Vehicle(registrationNumber, color);
        Slot targetSlot = freeSlots.stream()
                .filter(Slot::isFree)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sorry, parking lot is full "));

        synchronized (targetSlot) {
            if (!targetSlot.isFree()) {
                targetSlot = freeSlots.stream()
                        .filter(Slot::isFree)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Sorry, parking lot is full "));
            }
        }
        targetSlot.add(vehicle);
        usedSlot.add(targetSlot);
        freeSlots.remove(targetSlot);
        System.out.println(String.format("Allocated slot number: %d", targetSlot.getId()));
        return issueTicket(vehicle, targetSlot);
    }

    @Override
    public void unpark(int slotId) {

    }

    private Ticket issueTicket(Vehicle vehicle, Slot slot) {
        long ticketNumber = random.nextLong() + 1;
        return new Ticket(ticketNumber, slot, vehicle);
    }
}
