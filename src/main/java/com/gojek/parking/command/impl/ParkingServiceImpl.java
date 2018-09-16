package com.gojek.parking.command.impl;

import com.gojek.parking.command.ParkService;
import com.gojek.parking.entity.Slot;
import com.gojek.parking.entity.Ticket;
import com.gojek.parking.entity.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

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

        if (freeSlots.size() == 0) {
            System.out.println("Sorry, parking lot is full");
            return null;
           // throw new RuntimeException("Sorry, parking lot is full");
        }

        for (Slot slot : usedSlot) {
            Optional<Vehicle> alreadyParked = slot.getVehicles().stream()
                    .filter(vehicle -> vehicle.getRegistrationNumber().equals(registrationNumber))
                    .findFirst();

            if (alreadyParked.isPresent()) {
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
        Slot allocatedSlot = usedSlot.stream()
                .filter(slot -> slot.getId() == slotId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid slotId"));
        synchronized (allocatedSlot){
            allocatedSlot.freeSlot(true);
            usedSlot.remove(allocatedSlot);
            freeSlots.add(allocatedSlot);
        }
        System.out.println(String.format("Slot number %d is free", allocatedSlot.getId()));

    }

    @Override
    public List<String> getRegistrationNumber(String color) {

        List<String> output= new ArrayList<>();

        for (Slot slot: usedSlot){
            List<String> vehicles = slot.getVehicles().stream()
                    .filter(vehicle -> vehicle.getColor().toLowerCase().equals(color))
                    .map(Vehicle::getRegistrationNumber)
                    .collect(Collectors.toList());
            output.addAll(vehicles);
        }

        System.out.println(output);
        return output;
    }

    @Override
    public Slot getSlot(String registration) {
        for (Slot slot: usedSlot){
            Optional<Vehicle> vehicle1 = slot.getVehicles().stream()
                    .filter(vehicle -> vehicle.getRegistrationNumber().equals(registration))
                    .findFirst();
            if (vehicle1.isPresent()){
                System.out.println(slot.getId());
                return slot;
            }
        }
        System.out.println("Not Found");
        throw new RuntimeException("Not Found");
    }

    @Override
    public List<Integer> getSlotsForColour(String color) {
        List<Integer> slotNumber = new ArrayList<>();
        for (Slot slot: usedSlot){
            Optional<Vehicle> vehicle1 = slot.getVehicles().stream()
                    .filter(vehicle -> vehicle.getColor().toLowerCase().equals(color.toLowerCase()))
                    .findFirst();
            if (vehicle1.isPresent()){
                 slotNumber.add(slot.getId());
            }
        }
        System.out.println(slotNumber);
        return slotNumber;
    }

    @Override
    public  void status(){
        System.out.println("Slot No.  " + "Registration No  " + "Colour");
        for (Slot slot: usedSlot){
            System.out.println(String.format("%d %22s %8s", slot.getId(), slot.getVehicles().get(0).getRegistrationNumber()
                    , slot.getVehicles().get(0).getColor()));
        }
    }



    private Ticket issueTicket(Vehicle vehicle, Slot slot) {
        long ticketNumber = random.nextLong() + 1;
        return new Ticket(ticketNumber, slot, vehicle);
    }
}
