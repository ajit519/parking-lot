package com.gojek.parking;

import com.gojek.parking.command.CreateParkingSlot;
import com.gojek.parking.command.ParkService;
import com.gojek.parking.command.impl.CreateParkingSlotImpl;
import com.gojek.parking.command.impl.ParkingServiceImpl;
import com.gojek.parking.entity.Slot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    private ParkService parkService;

    public static void main(String[] args) {

        String filePath;
        BufferedReader reader;
        String command = null;
        Main.CommandExecutor executor = new CommandExecutor();

        if (args.length > 0) {
            System.out.println("Reading from input command line parameter");
            filePath = args[0];
            if(filePath.trim().length() == 0){
                throw new IllegalArgumentException("Invalid path");
            }
            try {
                reader = new BufferedReader(new FileReader(filePath));
                while ((command = reader.readLine()) != null){
                    executor.execute(command.trim().toLowerCase());
                }
            }catch (Exception e){
                System.out.println(e);
            }


        } else {

            System.out.println("Please type the same options from below commands");
            System.out.println(" create_parking_lot");
            System.out.println(" park");
            System.out.println(" leave");
            System.out.println(" status");
            System.out.println(" registration_numbers_for_cars_with_colour");
            System.out.println(" slot_numbers_for_cars_with_colour");
            System.out.println(" slot_number_for_registration_number");
            System.out.println(" quit");

            do {
                try {
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    command = reader.readLine();
                    if(!command.toLowerCase().trim().equals("quit")){
                        executor.execute(command);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } while (command != null && !command.trim().toLowerCase().equals("quit"));
        }

    }


    static class CommandExecutor {

        private ParkService parkService;
        private CreateParkingSlot createParkingSlot;
        private Set<Slot> slots = new HashSet<>();
        private Map<String, Integer> commands = new HashMap<>();
        private boolean isInitialized = false;


        public CommandExecutor() {
            this.parkService = new ParkingServiceImpl(slots);
            commands.put("create_parking_lot", 1);
            commands.put("park", 2);
            commands.put("leave", 3);
            commands.put("status", 4);
            commands.put("registration_numbers_for_cars_with_colour", 5);
            commands.put("slot_numbers_for_cars_with_colour", 6);
            commands.put("slot_number_for_registration_number", 7);

        }


        public void execute(String command) {

            if (command == null || command.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid command");
            }
            String[] tokens = command.split(" ");

            if(!commands.containsKey(tokens[0].trim())){
                throw new IllegalArgumentException("Invalid Command");
            }
            switch (commands.get(tokens[0].trim())) {

                case 1:
                    createParkingSlot = new CreateParkingSlotImpl();
                    createParkingSlot.initializeParkingSlot(Integer.valueOf(tokens[1]));
                    slots.addAll(createParkingSlot.getSlots());
                    isInitialized = true;

                    break;

                case 2:
                    isParkingInitialized();
                    parkService.park(tokens[1], tokens[2]);
                    break;
                case 3:
                    isParkingInitialized();
                    parkService.unpark(Integer.parseInt(tokens[1]));
                    break;
                case 4:
                    isParkingInitialized();
                    parkService.status();
                    break;

                case 5:
                    isParkingInitialized();
                    parkService.getRegistrationNumber(tokens[1]);
                    break;
                case 6:
                    isParkingInitialized();
                    parkService.getSlotsForColour(tokens[1]);
                    break;
                case 7:
                    isParkingInitialized();
                    parkService.getSlot(tokens[1]);
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid command");

            }
        }

        private boolean isParkingInitialized() {
            if (!isInitialized) {
                throw new IllegalStateException("Before doing parking related task  configure parking ");
            }
            return true;
        }


    }

}
